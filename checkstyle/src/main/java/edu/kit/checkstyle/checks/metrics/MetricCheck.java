package edu.kit.checkstyle.checks.metrics;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.SeverityLevel;

public abstract class MetricCheck extends AbstractCheck {

    public final void init() {
        this.setSeverity(SeverityLevel.INFO.getName());
    }

    public final void visitToken(DetailAST ast) {
        try {
            this.execute(ast);
        } catch (Exception var3) {
            System.err.println("Error in AST \'" + ast + "\' in file \'" + this.getFileContents().getFileName() + "\'");
            var3.printStackTrace();
        }
    }

    protected abstract void execute(DetailAST var1);

    protected final void logMetric(DetailAST ast, Object value) {
        this.log(ast, "metric:" + this.metric() + ":" + value, new Object[0]);
    }

    protected abstract String metric();

    protected int countTokenType(DetailAST ast, int type) {
        int counter = 0;

        for (DetailAST cur = ast.findFirstToken(type); cur != null; cur = cur.getNextSibling()) {
            if (cur.getType() == type) {
                ++counter;
            }
        }

        return counter;
    }
}
