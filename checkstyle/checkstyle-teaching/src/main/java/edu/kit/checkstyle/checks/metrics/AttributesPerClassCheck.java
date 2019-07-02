package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class AttributesPerClassCheck extends MetricCheck {

    public static final String METRIC = "attributes-per-class";

    public int[] getDefaultTokens() {
        return new int[]{14, 154};
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
        return "attributes-per-class";
    }

    protected void execute(DetailAST ast) {
        DetailAST body = ast.findFirstToken(6);
        int count = this.countTokenType(body, 10);
        this.logMetric(ast, Integer.valueOf(count));
    }
}
