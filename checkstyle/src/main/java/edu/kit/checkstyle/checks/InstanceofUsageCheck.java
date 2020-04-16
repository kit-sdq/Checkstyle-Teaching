package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

import java.util.Set;

public class InstanceofUsageCheck extends TokenSearcherCheck {

    public int[] getDefaultTokens() {
        return new int[]{121};
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    public void visitToken(DetailAST ast) {
        DetailAST methodDef = this.getContainingMethod(ast);
        boolean isEqualsMethod = methodDef == null ? false : this.isEqualsMethod(methodDef);
        if (!isEqualsMethod) {
            this.log(ast.getLineNo(), ast.getColumnNo(), "instanceof should only be used in the equals method.",
                    new Object[0]);
        }
    }

    private boolean isEqualsMethod(DetailAST methodDef) {
        DetailAST methodName = methodDef.findFirstToken(58);
        if (!this.eqName(methodName, "equals")) {
            return false;
        } else {
            DetailAST methodType = methodDef.findFirstToken(13);
            if (!this.eqTypeASTName(methodType, "boolean")) {
                return false;
            } else {
                DetailAST params = methodDef.findFirstToken(20);
                if (!this.eqParamCount(params, 1)) {
                    return false;
                } else {
                    Set mods = this.modifierNames(methodDef);
                    return mods.contains("public") && !mods.contains("static");
                }
            }
        }
    }

    private boolean eqParamCount(DetailAST ast, int count) {
        this.require(ast.getType() == 20, "not a parameter list");
        return ast.getChildCount() == count;
    }

    private boolean eqTypeASTName(DetailAST ast, String name) {
        this.require(ast.getType() == 13, "not a type");
        return ast.getFirstChild().getText().equals(name);
    }
}
