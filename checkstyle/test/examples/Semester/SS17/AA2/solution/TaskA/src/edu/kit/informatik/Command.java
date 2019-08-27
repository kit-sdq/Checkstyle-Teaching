package edu.kit.informatik;

import edu.kit.informatik.aggregation.AggregationType;
import edu.kit.informatik.goal.Goal;
import edu.kit.informatik.goal.GoalType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public enum Command {
    ADD("add (" + Helper.TYPE_NAME + ") (" + Helper.AGGREGATIONS + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            String name = matcher.group(1);
            AggregationType aggregationType = AggregationType.fromString(matcher.group(2));
            tracker.addType(name, aggregationType);
            Terminal.printLine("Added " + name + " with aggregation " + matcher.group(2) + ".");
        }
    },
    
    GOAL("goal (" + Helper.TYPE_NAME + ") " + Helper.GOAL_DESCRIPTION) {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Type type = tracker.getTypeByName(matcher.group(1));
            GoalType goalType = GoalType.fromString(matcher.group(2));
            Goal goal = goalType.createInstance(type, Helper.goalValueParser(matcher.group(3)));
            tracker.addGoal(type, goal);
            
            Terminal.printLine("New goal for " + goal.toString() + ".");
        }
    },
    
    REMOVE_GOAL("remove-goal (" + Helper.TYPE_NAME + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            String typeName = matcher.group(1);
            tracker.removeGoal(typeName);
            Terminal.printLine("Removed goal for " + typeName + ".");
        }
    },
    
    GOALS("goals") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Terminal.printLine("Number of goals: " + tracker.getGoalCount());
            String goals = tracker.listGoals();
            if (!goals.isEmpty()) {
                Terminal.printLine(goals);
            }
        }
    },
    
    RECORD("record (" + Helper.TYPE_NAME + ") (" + Helper.REAL_NUMBER + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Type type = tracker.getTypeByName(matcher.group(1));
            double value = Double.parseDouble(matcher.group(2));
            
            Terminal.printLine(tracker.recordValue(type, value));
        }
    },
    
    STATE("state") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Terminal.printLine(tracker.stateToString());      
        }
    },
    
    PROGRESS("progress") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Terminal.printLine(tracker.progressToString());
        }
    },
    
    TOP("top (\\d+) (" + Helper.TYPE_NAME + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            int amount = Integer.parseInt(matcher.group(1));
            Type type = tracker.getTypeByName(matcher.group(2));
            
            if (amount < 1) {
                throw new InteractionException("invalid amount of days.");
            }
            
            SortedSet<Map.Entry<Integer, Double>> topDays = tracker.getTopDays(type);
            String output = topDays.stream()
                    .limit(amount)
                    .map(this::printDay)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(System.lineSeparator()));
            if (!output.isEmpty()) {
                Terminal.printLine(output);
            }
        }
        
        private String printDay(Map.Entry<Integer, Double> entry) {
            if (entry == null) {
                return null;
            }
            
            return "Day " + entry.getKey() + " - " + Helper.formatDouble(entry.getValue());
        }
    },
    
    GOALS_WEEK("goals-week ([1-9]\\d*)") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            int week = Integer.parseInt(matcher.group(1));
            
            if (week < 1) {
                throw new InteractionException("invalid week number");
            }
            
            Terminal.printLine("Number of goals: " + tracker.getGoalCount());
            Map<Goal, Integer> stats = tracker.goalsReachedInWeek(week);
            
            String output = stats.entrySet().stream()
                    .map(entry -> entry.getKey().toString() + " - " + entry.getValue())
                    .collect(Collectors.joining(System.lineSeparator()));
            
            if (!output.isEmpty()) {
                Terminal.printLine(output);
            }
        }
    },
    
    NEXT_DAY("next-day") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Terminal.printLine(tracker.nextDay());
        }
    },
    
    SET_DAY("set-day (\\d+)") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            Terminal.printLine(tracker.setDay(Integer.parseInt(matcher.group(1))));
        }
    },
    
    SLIDING_WINDOW("sliding-window ([1-9]\\d*) (" + Helper.DATA + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            int windowSize = Integer.parseInt(matcher.group(1));
            List<Double> input = parseInputList(matcher.group(2));
            
            if (windowSize > input.size()) {
                throw new InteractionException("window size too big for the amount of datapoints.");
            }
            
            List<Double> result = tracker.slidingWindow(input, windowSize);
            Terminal.printLine(resultListToString(result));
        }
    },
    
    NORMALIZE("normalize (" + Helper.NATURAL_NUMBER + ") (" + Helper.NATURAL_NUMBER + ") (" + Helper.DATA + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            int lower = Integer.parseInt(matcher.group(1));
            int upper = Integer.parseInt(matcher.group(2));
            List<Double> input = parseInputList(matcher.group(3));
            List<Double> result = tracker.normalize(input, lower, upper);
            Terminal.printLine(resultListToString(result));
        }
    },
    
    PEAKS("peaks (" + Helper.REAL_NUMBER + ") ([1-9]\\d*) (" + Helper.DATA + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            double threshold = Double.parseDouble(matcher.group(1));
            int duration = Integer.parseInt(matcher.group(2));
            List<Double> input = parseInputList(matcher.group(3));
            List<Integer> result = tracker.detectPeaks(input, threshold, duration);
            String toPrint = result.stream().map(String::valueOf).collect(Collectors.joining(";"));
            if (!toPrint.isEmpty()) {
                Terminal.printLine(toPrint);
            }
        }
    },
    
    PULSE("pulse (" + Helper.DATA + ")") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            List<Double> input = parseInputList(matcher.group(1));
            Terminal.printLine(tracker.recordPulse(input));
        }
    },
    
    QUIT("quit") {
        @Override
        public void execute(MatchResult matcher, Tracker tracker) throws InteractionException {
            isRunning = false;
            Terminal.printLine("Quit (" + tracker.getCurrentDay().toString() + ")");
        }
    };

    private static boolean isRunning = true;
    private Pattern pattern;

    Command(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    public static Command executeMatching(String input, Tracker tracker) throws InteractionException {
        for (Command command : Command.values()) {
            Matcher matcher = command.pattern.matcher(input);
            if (matcher.matches()) {
                command.execute(matcher, tracker);
                return command;
            }
        }

        throw new InteractionException("not a valid command!");
    }

    public abstract void execute(MatchResult matcher, Tracker tracker) throws InteractionException;

    public boolean isRunning() {
        return isRunning;
    }

    private static List<Double> parseInputList(String input) {
        List<Double> result = new ArrayList<>();
        for (String current : input.split(";")) {
            result.add(Double.parseDouble(current));
        }
        return result;
    }

    private static String resultListToString(List<Double> result) {
        return result.stream().map(Helper::formatDouble).collect(Collectors.joining(";"));
    }
}
