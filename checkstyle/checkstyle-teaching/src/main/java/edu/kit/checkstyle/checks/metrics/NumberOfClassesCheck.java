package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class NumberOfClassesCheck extends MetricCheck {

    public static final String METRIC = "number-of-classes";

    public int[] getDefaultTokens() {
        return new int[]{14, 15, 153};
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
        return "number-of-classes";
    }

    protected void execute(DetailAST ast) {
        this.logMetric(ast, Integer.valueOf(1));
    }
}
