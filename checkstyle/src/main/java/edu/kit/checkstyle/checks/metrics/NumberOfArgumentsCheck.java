package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class NumberOfArgumentsCheck extends MetricCheck {

    public static final String METRIC = "number-of-arguments";

    public int[] getDefaultTokens() {
        return new int[]{9, 8};
    }

    @Override
    public int[] getRequiredTokens() {
        return CommonUtil.EMPTY_INT_ARRAY;
    }

    @Override
    public int[] getAcceptableTokens() {
        return getDefaultTokens();
    }

    protected String metric() {
        return "number-of-arguments";
    }

    protected void execute(DetailAST ast) {
        int count = this.parameterCount(ast.findFirstToken(20));
        this.logMetric(ast, Integer.valueOf(count));
    }

    private int parameterCount(DetailAST ast) {
        int count = 0;

        for (DetailAST cur = ast.getFirstChild(); cur != null; cur = cur.getNextSibling()) {
            if (cur.getType() == 21) {
                ++count;
            }
        }

        return count;
    }
}
