package edu.kit.checkstyle.checks;

import com.google.common.collect.Sets;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.utils.CommonUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class CallHierarchyComplexityCheck extends TokenSearcherCheck {

    private static final Ref THIS_REF = new VariableRef("this");
    private final SymbolTable symbolTable = new SymbolTable();

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

    public void visitToken(DetailAST ast) {
        try {
            this.symbolTable.init();
            DetailAST e = ast.findFirstToken(7);
            this.findAllMethodChains(e);
            this.findDuplicates();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    private void findDuplicates() {
        Object duplicates = CollectionUtils.mkSet(new Object[0]);
        Iterator lineStarts = this.symbolTable.table.entrySet().iterator();

        while (lineStarts.hasNext()) {
            Entry i$ = (Entry) lineStarts.next();
            Set ast = (Set) i$.getValue();
            if (ast.size() > 1 && !((Ref) i$.getKey()).equals(THIS_REF)) {
                duplicates = Sets.union((Set) duplicates, ast);
            }
        }

        Set lineStarts1 = (Set) this.symbolTable.table.get(THIS_REF);
        Iterator i$1 = this.differentLineNo((Set) duplicates).iterator();

        while (i$1.hasNext()) {
            DetailAST ast1 = (DetailAST) i$1.next();
            DetailAST start = this.find(lineStarts1, ast1.getLineNo());
            this.log(start.getLineNo(), start.getColumnNo(), "duplicate found", new Object[0]);
        }
    }

    private DetailAST find(Set set, int lineNo) {
        Iterator i$ = set.iterator();

        DetailAST ast;
        do {
            if (!i$.hasNext()) {
                throw new IllegalArgumentException("line number \'" + lineNo + "\' not found");
            }

            ast = (DetailAST) i$.next();
        } while (ast.getLineNo() != lineNo);

        return ast;
    }

    private Set differentLineNo(Set set) {
        if (set.isEmpty()) {
            return set;
        } else {
            List elems = CollectionUtils.mkList(new Object[0]);
            elems.addAll(set);
            Collections.sort(elems, new Comparator<DetailAST>() {
                public int compare(DetailAST a1, DetailAST a2) {
                    return a1.getColumnNo() - a2.getColumnNo();
                }
            });
            DetailAST first = (DetailAST) elems.get(0);
            TreeSet ret = new TreeSet(new Comparator<DetailAST>() {
                public int compare(DetailAST a1, DetailAST a2) {
                    return a1.getLineNo() - a2.getLineNo();
                }
            });
            ret.add(first);
            int i = 1;

            for (int end = elems.size(); i < end; ++i) {
                if (first.getLineNo() != ((DetailAST) elems.get(i)).getLineNo()) {
                    ret.add(elems.get(i));
                }
            }

            return ret;
        }
    }

    private void findAllMethodChains(DetailAST ast) {
        for (DetailAST child = ast.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child.getType() == 27) {
                this.findMethodChain(child);
            } else {
                this.findAllMethodChains(child);
            }
        }
    }

    private void findMethodChain(DetailAST ast) {
        this.require(ast.getType() == 27, "AST is not a method call");
        DetailAST dot = ast.findFirstToken(59);
        if (dot == null) {
            DetailAST ident = ast.findFirstToken(58);
            this.symbolTable.addSymbol(THIS_REF, ident);
        } else {
            this.nextMethodChain(dot, (DetailAST) null);
        }
    }

    private void nextMethodChain(DetailAST dot, DetailAST parent) {
        this.require(dot.getType() == 59, "AST is not a dot");
        DetailAST lhs = dot.getFirstChild();
        DetailAST rhs = dot.getLastChild();
        if (lhs.getType() == 58) {
            this.symbolTable.addSymbol(THIS_REF, lhs);
            this.symbolTable.addSymbol(new VariableRef(lhs.getText()), rhs);
            this.symbolTable.addSymbol(new MethodRef(rhs.getText()), parent);
        } else {
            DetailAST nextDot = lhs.findFirstToken(59);
            if (nextDot == null) {
                DetailAST ident = lhs.findFirstToken(58);
                this.symbolTable.addSymbol(THIS_REF, ident);
                this.symbolTable.addSymbol(new MethodRef(ident.getText()), rhs);
                this.symbolTable.addSymbol(new MethodRef(rhs.getText()), parent);
            } else {
                this.symbolTable.addSymbol(new MethodRef(rhs.getText()), parent);
                this.nextMethodChain(nextDot, rhs);
            }
        }
    }

    static class SymbolTable {

        final Map table = CollectionUtils.mkMap();

        void addSymbol(Ref ref, DetailAST ast) {
            if (this.table.containsKey(ref)) {
                ((Set) this.table.get(ref)).add(ast);
            } else {
                this.table.put(ref, CollectionUtils.mkSet(new DetailAST[]{ast}));
            }
        }

        void addSymbol(Ref ref) {
            if (this.table.containsKey(ref)) {
                throw new UnsupportedOperationException("symbol table already contains \'" + ref + "\'");
            } else {
                this.table.put(ref, CollectionUtils.mkSet(new DetailAST[0]));
            }
        }

        void init() {
            this.table.clear();
        }

        public String toString() {
            return "SymbolTable" + this.table;
        }
    }

    static class VariableRef extends Ref {

        public VariableRef(String name) {
            super(name);
        }

        public boolean isMethod() {
            return false;
        }
    }

    abstract static class Ref {

        public final String name;

        public Ref(String name) {
            this.name = name;
        }

        abstract boolean isMethod();

        public int hashCode() {
            return this.name.hashCode() + (this.isMethod() ? 1231 : 1237) * 31;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Ref)) {
                return false;
            } else {
                Ref ref = (Ref) obj;
                return this.isMethod() == ref.isMethod() && this.name.equals(ref.name);
            }
        }

        public String toString() {
            return this.name;
        }
    }

    static class MethodRef extends Ref {

        public MethodRef(String name) {
            super(name);
        }

        public boolean isMethod() {
            return true;
        }
    }
}
