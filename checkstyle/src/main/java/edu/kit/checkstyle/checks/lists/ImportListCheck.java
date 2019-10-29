package edu.kit.checkstyle.checks.lists;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class ImportListCheck extends BaseListCheck {
    @Override
    public int[] getDefaultTokens() {
        return new int[]{30};
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
        log(ast.getLineNo(), ast.getColumnNo(), getImportString(ast));
    }

    private static String getImportString(final DetailAST ast) {
        switch (ast.getType()) {
            case 30:
                return getImportString(ast.getFirstChild());
            case 59:
                return getImportString(ast.getFirstChild()) + "." + getImportString(ast.getLastChild());
            default:
                return ast.getText();
        }
    }
}
