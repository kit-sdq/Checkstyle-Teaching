package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class MethodsPerClassCheck extends MetricCheck {

    public static final String METRIC = "methods-per-class";

    protected String metric() {
        return "methods-per-class";
    }

    public int[] getDefaultTokens() {
        return new int[]{14, 15};
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
        DetailAST body = ast.findFirstToken(6);
        int count = this.countTokenType(body, 9);
        this.logMetric(ast, Integer.valueOf(count));
    }
}
