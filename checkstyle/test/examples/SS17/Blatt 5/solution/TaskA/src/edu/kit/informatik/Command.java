package edu.kit.informatik;

import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Command {
    NODES("nodes") {
        @Override
        public void execute(MatchResult matcher, Graph graph) throws InputException {
            Terminal.printLine(graph.listNodes());
        }
    },
    
    EDGES("edges") {
        @Override
        public void execute(MatchResult matcher, Graph graph) throws InputException {
            Terminal.printLine(graph.listEdges());
        }
    },
    
    RECOMMEND("recommend *(S1|S2|S3) *([0-9]+)") {
        @Override
        public void execute(MatchResult matcher, Graph graph) throws InputException {
            Strategy strategy = StrategyFactory.createStrategy(matcher.group(1));
            
            Product start;
            try {
                start = graph.getProductById(Integer.parseInt(matcher.group(2)));
            } catch (GraphException e) {
                throw new InputException(e.getMessage());
            }
            
            List<Product> result = strategy.execute(graph, start);
            Collections.sort(result);
            Terminal.printLine(result.stream().map(Product::toString).collect(Collectors.joining(",")));
        }
    },
    EXPORT("export") {
        @Override
        public void execute(MatchResult matcher, Graph graph) throws InputException {
            Terminal.printLine(graph.toDOT());
        }
    },
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, Graph graph) throws InputException {
            isRunning = false;
        }
    };
    
    private static boolean isRunning = true;
    private Pattern pattern;

    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static Command executeMatching(String input, Graph graph) throws InputException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, graph);
                return command;
            }
        }

        throw new InputException("not a valid command!");
    }

    public abstract void execute(MatchResult matcher, Graph graph) throws InputException;

    public boolean isRunning() {
        return isRunning;
    }
}
