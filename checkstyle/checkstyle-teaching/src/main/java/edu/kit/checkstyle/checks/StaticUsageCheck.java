package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

import java.util.Set;

public class StaticUsageCheck extends TokenSearcherCheck {

    private static final String msg = "\'static\' may only be used on variables together with \'final\'";

    public int[] getDefaultTokens() {
        return new int[]{64};
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
        int line = ast.getLineNo();
        int column = ast.getColumnNo();
        if (ast.getParent().getType() != 152) {
            DetailAST block = ast.getParent().getParent();
            if (block.getType() == 10) {
                Set mods = this.modifierNames(block);
                if (!mods.contains("final")) {
                    this.log(line, column, "\'static\' may only be used on variables together with \'final\'",
                            new Object[0]);
                }
            }
        }
    }
}
