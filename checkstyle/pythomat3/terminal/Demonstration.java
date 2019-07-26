import edu.kit.informatik.Terminal;

/**
 * This is a demonstration on how to use the instrumented Terminal class to
 * create protocols of program runs
 */
public class Demonstration
{
    private Demonstration() {}

    private static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (Exception e) {
        }
    }

    // TODO: add some more commands to demonstrate this a little better
    public static void main(String args[]) {
        Terminal.setTimeout(1);
        Terminal.printLine("Enter any of {|error|info|exit|sleep|ctrl+d}:");
        String str = Terminal.readLine();
        if (str == null) {
            Terminal.printLine("Doing null pointer dereference");
            str = str.trim();
        } else if (str.equals("")) {
            Terminal.printLine("Response");
        } else if (str.equals("error")) {
            Terminal.printError("Error: something happened");
        } else if (str.equals("info")) {
            Terminal.printInfo("Annotation");
        } else if (str.equals("exit")) {
            Terminal.exit(1);
        } else if (str.equals("sleep")) {
            sleep(2);
        }

        Terminal.exit(0);
    }
}

