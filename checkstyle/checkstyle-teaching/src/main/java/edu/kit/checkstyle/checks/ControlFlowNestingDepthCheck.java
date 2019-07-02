package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class ControlFlowNestingDepthCheck extends AbstractCheck {

    private int max = 4;

    public void setMax(int limit) {
        this.max = limit;
    }

    private boolean isControlFlowLiteral(DetailAST ast) {
        int[] arr$ = this.getDefaultTokens();
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            int controlFlowToken = arr$[i$];
            if (ast.getType() == controlFlowToken) {
                return true;
            }
        }

        return false;
    }

    public int[] getDefaultTokens() {
        return new int[]{83, 92, 95, 96, 84, 91, 89};
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    public DetailAST skipIfElse(DetailAST ast) {
        DetailAST parent = ast.getParent();
        return parent == null ? ast :
                ((ast.getType() != 83 || parent.getType() != 92) && (ast.getType() != 92 || parent.getType() != 83) ?
                        ast : this.skipIfElse(parent));
    }

    public DetailAST skipTryCatch(DetailAST ast) {
        DetailAST parent = ast.getParent();
        return parent == null ? ast :
                (ast.getType() == 96 && (parent.getType() == 96 || parent.getType() == 95) ? this.skipTryCatch(parent) :
                        ast);
    }

    public DetailAST skipGroup(DetailAST ast) {
        DetailAST skipped = this.skipTryCatch(ast);
        if (skipped != ast) {
            return skipped;
        } else {
            skipped = this.skipIfElse(ast);
            return skipped != ast ? skipped : ast;
        }
    }

    public DetailAST getControlFlowParent(DetailAST ast) {
        DetailAST current = ast;

        do {
            current = this.skipGroup(current);
            current = current.getParent();
        } while (current != null && !this.isControlFlowLiteral(current));

        return current;
    }

    public void visitToken(DetailAST ast) {
        int line = ast.getLineNo();
        int column = ast.getColumnNo();
        int depth = 1;
        String nesting = new String(ast.getText());

        for (DetailAST token = this.getControlFlowParent(ast);
                token != null; token = this.getControlFlowParent(token)) {
            nesting = token.getText() + "/" + nesting;
            ++depth;
        }

        if (ast.getType() != 92 || ast.getFirstChild().getType() != 83) {
            if (depth > this.max) {
                this.log(line, column, "control.flow.nesting.depth",
                        new Object[]{Integer.valueOf(depth), Integer.valueOf(this.max)});
            }
        }
    }
}
