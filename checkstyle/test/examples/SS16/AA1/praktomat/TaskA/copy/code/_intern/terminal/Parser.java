package edu.kit.informatik._intern.terminal;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.kit.informatik._intern.terminal.analyzer.Analyzer;
import edu.kit.informatik._intern.util.collections.CNode;
import edu.kit.informatik._intern.util.invoke.Conversion;
import edu.kit.informatik._intern.util.syntax.Rule;
import edu.kit.informatik._intern.util.syntax.Syntax;
import edu.kit.informatik._intern.util.syntax.tokenizer.ModuleInformation;
import edu.kit.informatik._intern.util.syntax.tokenizer.Token;
import edu.kit.informatik._intern.util.syntax.tokenizer.Tokenizer;

public final class Parser implements Properties {
    
    private static final Pattern LB = Pattern.compile("\\R");
    private static final Pattern CR = Pattern.compile("\\r(?=\\n)|((?m)(?<=^)\\h*(\\/\\/.*|\\/\\*(?s:.*?)\\*\\/\\h*)$\\"
            + "R?|(?<!^)\\h*(\\/\\/.*|\\/\\*(?s:.*?)\\*\\/\\h*)$)");
    
    private final MethodSupport methods = new MethodSupport();
    private final Tokenizer<ConsumingInformation> t = new Tokenizer<>(TopLevel.asModulesFor(this));
    private final List<Entry> c = new ArrayList<>();
    private final Map<String, Object> var = new HashMap<>();
    private final Map<String, List<String>> files = new HashMap<>();
    private Parser() {
        
    }
    
    @Override
    public String toString() {
        return methods + System.lineSeparator() + var + System.lineSeparator() + files;
    }
    
    private interface ConsumingInformation extends ModuleInformation {
        
        void consume(final Token<ConsumingInformation> token);
    }
    
    public static Parser of(final Path... paths) throws IOException {
        final Parser reader = new Parser();
        for (final Path path : paths) {
            if (Files.isRegularFile(path)) {
                final String s = CR.matcher(new String(Files.readAllBytes(path), StandardCharsets.UTF_8))
                        .replaceAll("");
                reader.t.tokenize(s, 0, s.length()).forEach((t) -> t.module().consume(t));
            }
        }
        return reader;
    }
    
    @Override
    public boolean is(final String key, final Object value) {
        return value.equals(var.get(key));
    }
    
    public List<Entry> interaction() {
        return c;
    }
    
    @Override
    public List<String> getFile(final String filename) {
        return files.get(filename);
    }

    private enum TopLevel {
        BLOCK {
            
            @Override String format() {
                return "<(\\w+)(=([^>]+))?>\\h*\\R?((?s).*?)\\R?\\h*</(?i:\\1)>"; // 1: id, 3(+2): args, 4: content
            }
            
            @Override void consume(final Parser r, final Token<ConsumingInformation> token) {
                switch (token.group(1).toLowerCase(Locale.ENGLISH)) {
                    case "file":
                        r.files.put(token.group(3), Arrays.asList(LB.split(token.group(4), -1)));
                        break;
                    case "code":
                        r.codeSyntax().process(token.group(4)).compute();
                        break;
                    default:
                        throw new IllegalStateException("unknown block identifier '" + token.group(1) + "'");
                }
            }
        },
        CODELINE {

            @Override String format() {
                return ">(.*)"; // 1: content
            }

            @Override void consume(final Parser r, final Token<ConsumingInformation> token) {
                r.codeSyntax().process(token.group(1)).compute();
            }
        },
        INTERACTION {
            
            private final Pattern p = Pattern.compile("\\$(\\w+)");
            
            @Override String format() {
                return "((.+)\\R?)+"; // 0: content
            }
            
            @Override void consume(final Parser r, final Token<ConsumingInformation> token) {
                final Entry.Builder builder = Entry.builder();
                LB.splitAsStream(token.group(0)).forEachOrdered((line) -> {
                    final String s;
                    final Matcher m = p.matcher(line);
                    if (m.find()) {
                        final StringBuilder sb = new StringBuilder();
                        int pos = 0;
                        do {
                            sb.append(line, pos, m.start());
                            final Object val = r.var.getOrDefault(m.group(1), m.group(0));
                            if (val instanceof Analyzer) {
                                builder.addAnalyzer((Analyzer) val);
                                if (pos == 0 && m.end() == line.length()) return;
                            } else if (val instanceof Info) {
                                // XXX This seems like the best way to do it as it allows explicit differentiating
                                // between interaction and info without relying on any kind of tag etc.
                                builder.addInformation(val.toString());
                                if (pos == 0 && m.end() == line.length()) return;
                            } else {
                                sb.append(val);
                            }
                            pos = m.end();
                        } while (m.find());
                        s = sb.append(line, pos, line.length()).toString();
                    } else {
                        s = line;
                    }
                    builder.addInteraction(s);
                });
                if (r.c.isEmpty() && builder.containsInput()) {
                    r.c.add(Entry.builder().build());
                }
                r.c.add(builder.build());
            }
        },
        LINEBREAK {
            
            @Override String format() {
                return "\\R+";
            }
            
            @Override void consume(final Parser r, final Token<ConsumingInformation> token) {
                // Empty on purpose
            }
        };
        
        abstract String format();
        
        abstract void consume(final Parser r, final Token<ConsumingInformation> token);
    
        public static Stream<ConsumingInformation> asModulesFor(final Parser p) {
            return Arrays.stream(values()).map((m) -> (ConsumingInformation) new ConsumingInformation() {
                
                @Override public String format() {
                    return m.format();
                }
                
                @Override public void consume(final Token<ConsumingInformation> token) {
                    m.consume(p, token);
                }
                
                @Override public String toString() {
                    return m.toString();
                }
            });
        }
    }
    
    private Syntax codeSyntax;
    private Syntax codeSyntax() {
        if (codeSyntax == null) {
            codeSyntax = CodeLevel.asSyntax(this);
        }
        return codeSyntax;
    }
    
    @SuppressWarnings({"static-method", "unused"})
    private enum CodeLevel {
        
        LINEBREAK {
            
            @Override String format() {
                return "\\R+";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                final CNode<Token<Rule>> c = n.leave((v) -> true);
                c.add(t);
                return c;
            }
        },
        
        IMPORT {
            
            @Override String format() {
                return "import\\h+"
                        + "(([a-z_][a-zA-Z0-9_]*\\.)+"          // Package
                        + "([A-Z][a-zA-Z0-9_]*\\.)*"            // Outer classes
                        + "[A-Z][a-zA-Z0-9_]*)";                // 1 (+2,+3,+4), class name, may be fully specified
            }
            
            @Override void stay(final CNode<Token<Rule>> n, final Token<Rule> t) {
                n.add(t);
            }
            
            @Override void consume(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                a.methods.importClass(t.group(1));
            }
        },
        STATICDEFINITION {
            
            @Override String format() {
                return "static\\h+"
                        + "((([a-z_][a-zA-Z0-9_]*\\.)+"         // Package
                        + "([A-Z][a-zA-Z0-9_]*\\.)*)?"          // Outer classes
                        + "[A-Z][a-zA-Z0-9_]*)"                 // 1 (+2,+3,+4), class name, may be fully specified
                        + "\\."
                        + "([a-z_][A-Za-z0-9_]*)"               // 5, method name
                        + "(\\(.*?\\)\\w*)"                     // 6, method descriptor
                        + "(\\h*->\\h*(\\(.*?\\)\\w*))?";       // 8, optional narrowing method descriptor
            }
            
            @Override void stay(final CNode<Token<Rule>> n, final Token<Rule> t) {
                n.add(t);
            }
            
            @Override void consume(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                a.methods.addStaticMethod(t.group(1), t.group(5), t.group(6), t.group(8));
            }
        },
        VIRTUALDEFINITION {
            
            @Override String format() {
                return "virtual\\h+"
                        + "((([a-z_][a-zA-Z0-9_]*\\.)+"         // Package
                        + "([A-Z][a-zA-Z0-9_]*\\.)*)?"          // Outer classes
                        + "[A-Z][a-zA-Z0-9_]*)"                 // 1 (+2,+3,+4), class name, may be fully specified
                        + "\\."
                        + "([a-z_][A-Za-z0-9_]*)"               // 5, method name
                        + "(\\(.*?\\)\\w*)"                     // 6, method descriptor
                        + "(\\h*->\\h*(\\(.*?\\)\\w*))?";       // 8, optional narrowing method descriptor
            }
            
            @Override void stay(final CNode<Token<Rule>> n, final Token<Rule> t) {
                n.add(t);
            }
            
            @Override void consume(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                a.methods.addVirtualMethod(t.group(1), t.group(5), t.group(6), t.group(8));
            }
        },
        CONSTRUCTORDEFINITION {
            
            @Override String format() {
                return "constructor\\h+"
                        + "((([a-z_][a-zA-Z0-9_]*\\.)+"         // Package
                        + "([A-Z][a-zA-Z0-9_]*\\.)*)?"          // Outer classes
                        + "[A-Z][a-zA-Z0-9_]*)"                 // 1 (+2,+3,+4), class name, may be fully specified
                        + "(\\(.*?\\)\\w*)"                     // 5, method descriptor
                        + "(\\h*->\\h*(\\(.*?\\)\\w*))?";       // 7, optional narrowing method descriptor;
            }
            
            @Override void stay(final CNode<Token<Rule>> n, final Token<Rule> t) {
                n.add(t);
            }
            
            @Override void consume(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                a.methods.addConstructor(t.group(1), t.group(5), t.group(7));
            }
        },
        
        METHODCALL {
            
            @Override String format() {
                return "\\.([a-z_][A-Za-z0-9_]*)\\(";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                final CNode<Token<Rule>> c = n.leave((v) -> v.module().id() == VARIABLE).add(t);
                c.pull(1);
                return c;
            }
            
            @Override Object compute(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                final List<Object> l = s.collect(Collectors.toList());
                final Object target = l.get(0);
                final String method = t.group(1);
                try {
                    return a.methods.invoke(target.getClass().getName(), method, l);
                } catch (final TypeNotPresentException | NoSuchElementException e) { // TODO yeah...
                    l.remove(0);
                    return a.methods.invoke(target.toString(), method, l);
                }
            }
        },
        CONSTRUCTORCALL {
            
            @Override String format() { // LOWPRIO Nested classes
                return "new\\h+(([_a-z][_\\w]*\\.)*[A-Z][_\\w]*)\\(";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                return n.add(t);
            }
            
            @Override Object compute(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                return a.methods.invoke(t.group(1), null, s.collect(Collectors.toList()));
            }
        },
        COMMA {
            
            @Override String format() {
                return ",";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                final CNode<Token<Rule>> c = n.leave((v) -> v.module().id() == VARIABLE);
                return c.value().module().id() == ASSIGNMENT ? c.parent() : c;
            }
        },
        BRACKET_CLOSING {
            
            @Override String format() {
                return "\\)";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                final CNode<Token<Rule>> c = n.leave((v) -> v.module().id() == VARIABLE);
                return c.parent();
            }
        },
        
        WHITESPACE {
            
            @Override String format() {
                return "(?<=\\R|\\A)\\h+";
            }
        },
        
        ADD {
            
            @Override String format() {
                return "\\h*\\+\\h*";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                final CNode<Token<Rule>> c = n.leave((v) -> v.module().id() == VARIABLE).add(t);
                c.pull(1);
                return c;
            }
            
            @Override Object compute(final Parser a, final CNode<Token<Rule>> n, final Stream<Object> s) {
                final Object[] o = s.toArray();
                if (o.length != 2) {
                    throw new IllegalStateException("invalid count");
                }
                final Object o1 = o[0];
                final Object o2 = o[1];
                if (o1 != null && o2 != null && Number.class.isInstance(o1) && Number.class.isInstance(o2)) {
                    final Class<?> cl = Conversion.convertibleTo(o1.getClass(), o2.getClass()) ? o1.getClass()
                            : o2.getClass(); // Does not support char
                    final Number n1 = (Number) o1;
                    final Number n2 = (Number) o2;
                    if (cl.equals(Integer.class)) return n1.intValue()    + n2.intValue();
                    if (cl.equals(Long.class))    return n1.longValue()   + n2.longValue();
                    if (cl.equals(Double.class))  return n1.doubleValue() + n2.doubleValue();
                    if (cl.equals(Float.class))   return n1.floatValue()  + n2.floatValue();
                    if (cl.equals(Byte.class))    return n1.byteValue()   + n2.byteValue();
                    if (cl.equals(Short.class))   return n1.shortValue()  + n2.shortValue();
                }
                return String.valueOf(o1) + String.valueOf(o2);
            }
        },
        
        ASSIGNMENT {
            
            @Override public String format() {
                return "(.+?)\\h*=\\h*";
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                return n.add(t);
            }
            
            @Override Object compute(final Parser a, final Token<Rule> t, final Stream<Object> s) {
                return a.var.compute(t.group(1), (u, v) -> s.findAny().get());
            }
        },
        
        VARIABLE {
            
            @Override String format() {
                return "((?<!\\\\)\".*?(?<!\\\\)\"|.)"; // XXX Inefficient but by far the most flexible solution
            }
            
            @Override CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
                return n.add(t);
            }
            
            @Override Object compute(final Parser a, final CNode<Token<Rule>> n, final Stream<Object> s) {
                // LOWPRIO Use Deque<String>/custom StringBuilder-implementation that allows insert(0) with O(1)
                final StringBuilder t = s.findAny().map((o) -> (StringBuilder) o).orElseGet(StringBuilder::new)
                        .insert(0, n.value().group(0));
                
                if (n.parent().isRoot() || n.parent().value().module().id() == VARIABLE) {
                    return t;
                }
                int start = 0;
                int end = t.length() - 1;
                for (; start < end && Character.isWhitespace(t.charAt(start)); start++) { }
                for (; end >= start && Character.isWhitespace(t.charAt(end)); end--) { }
                if (start != end && t.charAt(start) == '"' && t.charAt(end) == '"') {
                    start++;
                    end--;
                }
                final String target = t.substring(start, end + 1);
                return a.var.getOrDefault(target, target);
            }
        };
        
        public static Syntax asSyntax(final Parser target) {
            return new Syntax(Arrays.stream(values())
                    .map((r) -> new Rule(r, r.format(), (n, s) -> r.compute(target, n, s), r::move)));
        }
        
        void consume(final Token<Rule> t, final Stream<Object> s) {
            s.forEach((o) -> { });
        }
        
        void consume(final Parser a, final Token<Rule> t, final Stream<Object> s) {
            consume(t, s);
        }
        
        Object compute(final Parser a, Token<Rule> t, final Stream<Object> s) {
            consume(a, t, s);
            return null;
        }
        
        Object compute(final Parser a, final CNode<Token<Rule>> n, final Stream<Object> s) {
            return compute(a, n.value(), s);
        }
        
        void stay(final CNode<Token<Rule>> n, final Token<Rule> t) {
            // Empty on purpose
        }
        
        CNode<Token<Rule>> move(final CNode<Token<Rule>> n, final Token<Rule> t) {
            stay(n, t);
            return n;
        }
        
        abstract String format();
    }
}