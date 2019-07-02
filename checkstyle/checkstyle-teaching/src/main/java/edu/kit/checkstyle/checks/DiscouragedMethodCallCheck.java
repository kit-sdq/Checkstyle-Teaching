package edu.kit.checkstyle.checks;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;
import org.apache.commons.lang.StringUtils;

import java.util.Iterator;
import java.util.List;

public class DiscouragedMethodCallCheck extends TokenSearcherCheck {

    private List props = CollectionUtils.mkList(new Object[0]);

    public void setCheckedMethods(String value) {
        List props = CollectionUtils.mkList(new Object[0]);
        String[] arr$ = StringUtils.split(value, ",");
        int len$ = arr$.length;

        for (int i$ = 0; i$ < len$; ++i$) {
            String p = arr$[i$];
            String[] parts = p.split("[:.]");
            if (parts.length != 2 && parts.length != 3) {
                throw new IllegalArgumentException("format of value \'" + value + "\' is not supported");
            }

            boolean hasMethodScope = parts.length == 3;
            props.add(hasMethodScope ? new Prop(parts[0], parts[1], parts[2]) : new Prop("", parts[0], parts[1]));
        }

        this.props = props;
    }

    public int[] getDefaultTokens() {
        return new int[]{58};
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
        Iterator i$ = this.props.iterator();

        while (i$.hasNext()) {
            Prop prop = (Prop) i$.next();
            if (this.propFound(ast, prop) && !this.isInAllowedMethod(ast, prop)) {
                String context = prop.allowedMethod.isEmpty() ? "" :
                        String.format(" outside of the %s method", new Object[]{prop.allowedMethod});
                String msg = String.format("The usage of \'\'%s.%s\'\'%s is discouraged",
                        new Object[]{prop.className, prop.methodName, context});
                this.log(line, column, msg, new Object[0]);
            }
        }
    }

    private boolean propFound(DetailAST ast, Prop prop) {
        return this.eqName(ast, prop.methodName) && ast.getPreviousSibling() != null &&
                this.eqName(ast.getPreviousSibling(), prop.className);
    }

    private boolean isInAllowedMethod(DetailAST ast, Prop prop) {
        return !prop.allowedMethod.isEmpty() && this.eqName(this.getContainingMethodName(ast), prop.allowedMethod);
    }

    private DetailAST getContainingMethodName(DetailAST ast) {
        return this.getContainingMethod(ast).findFirstToken(58);
    }

    static class Prop {

        final String allowedMethod;
        final String className;
        final String methodName;

        public Prop(String allowedMethod, String className, String methodName) {
            this.allowedMethod = allowedMethod;
            this.className = className;
            this.methodName = methodName;
        }
    }
}
