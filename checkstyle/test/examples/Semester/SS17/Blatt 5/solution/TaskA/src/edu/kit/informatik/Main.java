package edu.kit.informatik;

import jdk.nashorn.internal.ir.Terminal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final String PRODUCT_ID = "[0-9]+";
    private static final String CATEGORY_PRODUCT = "[a-zA-Z0-9]+";
    private static final String PRODUCT = CATEGORY_PRODUCT + " *\\( *id *= *" + PRODUCT_ID + " *\\)";
    private static final String PREDICATE = "contains|contained-in|successor-of|predecessor-of";
    private static final String SUBJECT_OBJECT = PRODUCT + "|" + CATEGORY_PRODUCT;
    private static final String LINE = " *(" + SUBJECT_OBJECT + ") +(" + PREDICATE + ") +(" + SUBJECT_OBJECT + ") *";
    
    private static final String PRODUCT_ID_GROUP = "([0-9]+)";
    private static final String CATEGORY_PRODUCT_GROUP = "([a-zA-Z0-9]+)";
    private static final String PRODUCT_GROUP = CATEGORY_PRODUCT_GROUP + " *\\( *id *= *" + PRODUCT_ID_GROUP + " *\\)";
    private static final String SUBJECT_OBJECT_GROUP = PRODUCT_GROUP + "|" + CATEGORY_PRODUCT_GROUP;
    
    
    public static void main(String[] args) {        
        if (!checkArgs(args)) {
            return;
        }

        Graph graph = parseFile(Terminal.readFile(args[0]));
        if (graph == null) {
            return;
        }
        
        Command command = null;
        do {
            try {
                //TODO no null
                command = Command.executeMatching(Terminal.readLine(), graph);
            } catch (InputException e) {
                Terminal.printError(e.getMessage());
            }
        } while (command == null || command.isRunning());
    }

    private static boolean checkArgs(String[] args) {
        if (args.length != 1) {
            Terminal.printError("unexpected number of parameters.");
            return false;
        }
        
        return true;
    }

    private static Graph parseFile(String[] lines) {
        if (lines.length == 0) {
            Terminal.printError("file can't be empty");
            return null;
        }
        
        Graph graph = new Graph();

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                printLineError(i, "line can't be empty.");
                return null;
            }
            
            Matcher lineMatcher = Pattern.compile(LINE).matcher(lines[i]);
            boolean isCorrect = lineMatcher.matches();
            
            if (!isCorrect) {
                printLineError(i, "syntax error.");
                return null;
            }
            
            Node source = createNode(lineMatcher.group(1));
            Node target = createNode(lineMatcher.group(3));
            
            try {
                graph.addNodesAndEdge(Relation.fromString(lineMatcher.group(2)), source, target);
            } catch (GraphException e) {
                printLineError(i, e.getMessage());
                return null;
            }
        }

        return graph;
    }
    
    private static Node createNode(String nodeString) {
        Matcher matcher = Pattern.compile(SUBJECT_OBJECT_GROUP).matcher(nodeString);
        matcher.matches();
        
        if (matcher.group(3) != null) {
            return new Category(matcher.group(3));
        } else {
            return new Product(matcher.group(1), Integer.parseInt(matcher.group(2)));
        }
    }

    private static void printLineError(int lineNumber, String message) {
        Terminal.printError("line " + (lineNumber + 1) + ": " + message);
    }
}
