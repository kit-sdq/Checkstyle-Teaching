package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

public class NumberOfStatementsPerMethodCheck extends MetricCheck {

    public static final String METRIC = "number-of-statements-per-class";

    protected String metric() {
        return "number-of-statements-per-class";
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
            this.logMetric(ast, Integer.valueOf(this.countOfAST(body)));
        }
    }

    private int countOfAST(DetailAST ast) {
        int counter = 0;

        for (DetailAST cur = ast.getFirstChild(); cur != null; cur = cur.getNextSibling()) {
            counter += this.countOfToken(cur);
        }

        return counter;
    }

    private int countOfToken(DetailAST ast) {
        DetailAST elseBody;
        switch (ast.getType()) {
            case 10:
            case 28:
            case 136:
            case 151:
            case 178:
                return 1;
            case 33:
            case 67:
            case 84:
            case 85:
            case 91:
            case 96:
            case 97:
                return this.countOfAST(ast.findFirstToken(7));
            case 72:
                return this.countOfAST(ast);
            case 83:
                int ifCounter1 = 0;

                for (elseBody = ast.getFirstChild(); elseBody != null; elseBody = elseBody.getNextSibling()) {
                    switch (elseBody.getType()) {
                        case 7:
                            ifCounter1 += this.countOfAST(elseBody);
                            break;
                        case 92:
                            ifCounter1 += this.countOfToken(elseBody);
                    }
                }

                return ifCounter1;
            case 89:
                int switchCounter = 0;

                for (DetailAST tryCounter1 = ast.getFirstChild();
                        tryCounter1 != null; tryCounter1 = tryCounter1.getNextSibling()) {
                    if (tryCounter1.getType() == 33) {
                        switchCounter += this.countOfToken(tryCounter1);
                    }
                }

                return switchCounter;
            case 92:
                elseBody = ast.findFirstToken(7);
                return elseBody == null ? this.countOfToken(ast.findFirstToken(83)) : this.countOfAST(elseBody);
            case 95:
                int tryCounter = 0;

                for (DetailAST ifCounter = ast.getFirstChild();
                        ifCounter != null; ifCounter = ifCounter.getNextSibling()) {
                    switch (ifCounter.getType()) {
                        case 7:
                            tryCounter += this.countOfAST(ifCounter);
                            break;
                        case 96:
                        case 97:
                            tryCounter += this.countOfToken(ifCounter);
                    }
                }

                return tryCounter;
            case 176:
                return this.countOfAST(ast.findFirstToken(177));
            default:
                return 0;
        }
    }
}
