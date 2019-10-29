package edu.kit.checkstyle.checks.lists;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class MethodListCheck extends BaseListCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[]{9};
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    @Override
    public void visitToken(final DetailAST ast) {
        log(ast.getLineNo(), ast.getColumnNo(), getScope(ast) + getNameOfMethodDef(ast));
    }
}
