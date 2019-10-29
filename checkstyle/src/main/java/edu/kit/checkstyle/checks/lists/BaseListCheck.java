package edu.kit.checkstyle.checks.lists;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public abstract class BaseListCheck extends AbstractCheck {
    protected static String getScope(final DetailAST ast) {
        final DetailAST classParent = getParent(ast, 14);

        if (classParent == null) {
            return "";
        }

        if (ast.getType() == 14) {
            return getScope(classParent) + getNameOfClassDef(classParent) + "$";
        } else {
            return getScope(classParent) + getNameOfClassDef(classParent) + ".";
        }
    }

    protected static DetailAST getParent(final DetailAST ast, final int tokenType) {
        DetailAST current = ast.getParent();

        while (current != null && current.getType() != tokenType) {
            current = current.getParent();
        }

        return current;
    }

    protected static String getNameOfPackageDef(DetailAST ast) {
        switch (ast.getType()) {
            case 16:
                return getNameOfPackageDef(ast.getFirstChild().getNextSibling());
            case 58:
                return ast.getText();
            case 59:
                DetailAST left = ast.getFirstChild();
                DetailAST right = left.getNextSibling();
                return getNameOfPackageDef(left) + "." + getNameOfPackageDef(right);
            default:
                throw new RuntimeException("Failed parsing package declaration");
        }
    }

    protected static String getNameOfClassDef(final DetailAST ast) {
        return ast.getFirstChild().getNextSibling().getNextSibling().getText();
    }

    protected static String getNameOfMethodDef(final DetailAST ast) {
        return ast.getFirstChild().getNextSibling().getNextSibling().getText();
    }
}
