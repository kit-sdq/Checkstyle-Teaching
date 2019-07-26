
package _edu.kit.informatik;

import java.util.function.Function;

public final class Main {

    private Main() {}

    static {
        Util.init();
        final Function<String, RuntimeException> f = s -> {
            Util.printErr(s);
            Runtime.getRuntime().halt(1);
            return null;
        };
        edu.kit.informatik._intern.terminal.TerminalUtil.blockSystemOut(f);
        edu.kit.informatik._intern.terminal.TerminalUtil.blockSystemIn(f);
    }

    public static final boolean PREFIX = true;

    public static void main(String[] args) throws Throwable {
        for (int i = 0; i < args.length; i++)
            Util.run(Class.forName(args[i]));
    }
}
