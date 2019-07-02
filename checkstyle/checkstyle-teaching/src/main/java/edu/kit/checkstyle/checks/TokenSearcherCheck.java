package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

import java.util.HashSet;
import java.util.Set;

public abstract class TokenSearcherCheck extends AbstractCheck {

    protected void require(boolean requirement, String message) {
        if (!requirement) {
            throw new AssertionError(message);
        }
    }

    protected Set modifierNames(DetailAST ast) {
        DetailAST modAst = ast.findFirstToken(5);
        this.require(modAst != null, "AST doesn\'t contain modifiers");
        HashSet mods = new HashSet();

        for (DetailAST mod = modAst.getFirstChild(); mod != null; mod = mod.getNextSibling()) {
            mods.add(mod.getText());
        }

        return mods;
    }

    protected boolean hasModifiers(DetailAST ast) {
        return ast.findFirstToken(5) != null;
    }

    protected boolean eqName(DetailAST ast, String name) {
        return ast.getText().equals(name);
    }

    protected DetailAST getContainingMethod(DetailAST ast) {
        if (ast.getType() == 9) {
            return ast;
        } else {
            DetailAST methodDef;
            for (
                    methodDef = ast.getParent();
                    methodDef != null && methodDef.getType() != 9; methodDef = methodDef.getParent()) {
                ;
            }

            return methodDef;
        }
    }
}
