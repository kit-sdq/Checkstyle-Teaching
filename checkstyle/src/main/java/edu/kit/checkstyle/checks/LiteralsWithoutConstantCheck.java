package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

import java.util.HashMap;
import java.util.Map;

public class LiteralsWithoutConstantCheck extends AbstractCheck {

    private Map counts = new HashMap();
    private int stringMax = -1;
    private int max = 5;

    public void setStringMax(int maximum) {
        this.stringMax = maximum;
    }

    public void setMax(int maximum) {
        this.max = maximum;
    }

    private boolean isInAttributeDefinition(DetailAST ast) {
        for (DetailAST current = ast; current != null; current = current.getParent()) {
            if (current.getType() == 10 && current.getParent().getType() == 6) {
                return true;
            }
        }

        return false;
    }

    public int[] getDefaultTokens() {
        return new int[]{137, 138, 139, 140, 141, 142};
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
        String text = ast.getText();
        if (!text.equals("0") && !text.equals("1") && !text.equals("0L") && !text.equals("1L") && !text.equals("0.0") &&
                !text.equals("1.0") && !text.equals("")) {
            if (!this.isInAttributeDefinition(ast)) {
                int maximum = ast.getType() == 139 && this.stringMax != -1 ? this.stringMax : this.max;
                int count = this.counts.containsKey(text) ? ((Integer) this.counts.get(text)).intValue() + 1 : 1;
                if (count == maximum + 1) {
                    this.log(line, column, "literals.without.constant", new Object[]{text, Integer.valueOf(maximum)});
                }

                this.counts.put(text, Integer.valueOf(count));
            }
        }
    }
}
