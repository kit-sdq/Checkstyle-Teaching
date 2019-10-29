package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class NumberOfMethodsPerClassCheck extends MetricCheck {

    public static final String METRIC = "number-of-methods-per-class";

    protected String metric() {
        return "number-of-methods-per-class";
    }

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

    protected void execute(DetailAST ast) {
        this.logMetric(ast, Integer.valueOf(1));
    }
}
