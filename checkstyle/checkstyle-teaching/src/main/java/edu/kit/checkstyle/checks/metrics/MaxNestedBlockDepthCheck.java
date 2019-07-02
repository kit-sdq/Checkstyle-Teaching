package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class MaxNestedBlockDepthCheck extends MetricCheck {

    public static final String METRIC = "max-nested-block-depth";

    protected String metric() {
        return "max-nested-block-depth";
    }

    public int[] getDefaultTokens() {
        return new int[]{9, 8, 11, 12};
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
        DetailAST body = ast.findFirstToken(7);
        if (body != null) {
            this.logMetric(ast, Integer.valueOf(this.maxDepthOfAST(body, 0)));
        }
    }

    private int maxDepthOfAST(DetailAST ast, int depth) {
        int max = depth;

        for (DetailAST cur = ast.getFirstChild(); cur != null; cur = cur.getNextSibling()) {
            int m = this.maxDepthOfToken(cur, depth);
            max = m > max ? m : max;
        }

        return max;
    }

    private int maxDepthOfToken(DetailAST ast, int depth) {
        int m;
        int ifMax1;
        DetailAST elseBody1;
        switch (ast.getType()) {
            case 33:
            case 67:
            case 84:
            case 85:
            case 91:
            case 96:
            case 97:
                return this.maxDepthOfAST(ast.findFirstToken(7), depth + 1);
            case 72:
                return this.maxDepthOfAST(ast, depth + 1);
            case 83:
                ifMax1 = depth;

                for (elseBody1 = ast.getFirstChild(); elseBody1 != null; elseBody1 = elseBody1.getNextSibling()) {
                    switch (elseBody1.getType()) {
                        case 7:
                            m = this.maxDepthOfAST(elseBody1, depth + 1);
                            ifMax1 = m > ifMax1 ? m : ifMax1;
                            break;
                        case 92:
                            int n = this.maxDepthOfToken(elseBody1, depth);
                            ifMax1 = n > ifMax1 ? n : ifMax1;
                    }
                }

                return ifMax1;
            case 89:
                int switchMax = depth + 1;

                for (DetailAST tryMax1 = ast.getFirstChild(); tryMax1 != null; tryMax1 = tryMax1.getNextSibling()) {
                    if (tryMax1.getType() == 33) {
                        ifMax1 = this.maxDepthOfToken(tryMax1, switchMax);
                        switchMax = ifMax1 > switchMax ? ifMax1 : switchMax;
                    }
                }

                return switchMax;
            case 92:
                elseBody1 = ast.findFirstToken(7);
                return elseBody1 == null ? this.maxDepthOfToken(ast.findFirstToken(83), depth) :
                        this.maxDepthOfAST(elseBody1, depth + 1);
            case 95:
                int tryMax = depth;

                for (DetailAST ifMax = ast.getFirstChild(); ifMax != null; ifMax = ifMax.getNextSibling()) {
                    switch (ifMax.getType()) {
                        case 7:
                            int elseBody = this.maxDepthOfAST(ifMax, depth + 1);
                            tryMax = elseBody > tryMax ? elseBody : tryMax;
                            break;
                        case 96:
                        case 97:
                            m = this.maxDepthOfToken(ifMax, depth);
                            tryMax = m > tryMax ? m : tryMax;
                    }
                }

                return tryMax;
            default:
                return 0;
        }
    }
}
