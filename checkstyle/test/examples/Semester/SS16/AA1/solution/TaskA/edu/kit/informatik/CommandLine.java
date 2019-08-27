package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.board.FieldIdentifier;
import edu.kit.informatik.entity.manager.GameManager;
import edu.kit.informatik.entity.rule.Rule;
import edu.kit.informatik.exception.IllegalInputException;
import edu.kit.informatik.exception.SyntaxException;

// TODO replace with a list structure?
public final class CommandLine {
    private static final Pattern POSITION_PATTERN = Pattern.compile("^(?:.{1,2},.{1,2},.{1,2},.{1,2};){3}(?:.{1,2},.{1,2},.{1,2},.{1,2})$");
    
    enum Command {
        ROLL("roll"), ROLLX("rollx"), QUIT("quit"), START("start"), ABORT("abort"), PRINT("print"), PRINTX("printx"), MOVE("move"),
        
        // custom commands
        INIT("init"), KI("ki");
        
        private final String mName;
        
        private Command(String pName) {
            this.mName = pName;
        }
        
        public String getName() {
            return this.mName;
        }
        
        public static Command fromName(String pName) throws SyntaxException {
            for(Command cCommand : values()) {
                if(cCommand.getName().equals(pName)) {
                    return cCommand;
                }
            }
            
            if(pName.length() == 0) {
                throw new SyntaxException("Error, you have to enter a command.");
            }
            
            throw new SyntaxException("Error, a command with given name: '" + pName + "' is not available.");
        }
        
        public static String getAllCommandsAsString() {
            return Stream.of(values()).map(Command::getName).collect(Collectors.joining(","));
        }
        
        @Override
        public String toString() {
            return getName();
        }
    }    
    private CommandLine() {}
    
    public static final void run(final GameManager pGameManager) {
        GameManager currentGameManager = pGameManager;
        while(true) {
            String cliIn = Terminal.readLine();
            if(cliIn == null) {
                continue;
            }
            
            String[] parts = cliIn.split("\\s", 2);
            String[] argsList = (parts.length > 1) ? parts[1].split("\\s", -1) : new String[] {};
            try {    
                Command command = Command.fromName(parts[0]);
                switch(command) {
                    // TODO DEBUG START
                    case KI:
                        if(argsList.length > 0 && argsList[0].equals("auto")) {
                            int rounds = 200;
                            List<List<Integer>> statsList = new ArrayList<>(rounds);
                            Map<Player, Integer> winnerMap = new HashMap<>();
                            Map<Player, Integer> beginnerMap = new HashMap<>();
                            Random rand = new Random();
                            Rule[] rules = (argsList.length < 2) ? new Rule[] {} : Rule.parseRules(argsList[1].split(","));
                            
                            for(int i = 0; i < rounds; i++) {
                                Player humanPlayer = Player.RED;
                                Player randomPlayer = Player.ORDERED_PLAYERS.stream().skip(rand.nextInt(Player.values().length)).findFirst().get();
                                currentGameManager = GameManager.createKI(rules, humanPlayer, true, randomPlayer);
                                statsList.add(currentGameManager.getStats());
                                Player winner = currentGameManager.getWinner();
                                if(winnerMap.get(winner) == null) {
                                    winnerMap.put(winner, 1);
                                } else {
                                    winnerMap.compute(winner, (k, ov) -> ov + 1);
                                }
                                
                                if(beginnerMap.get(randomPlayer) == null) {
                                    beginnerMap.put(randomPlayer, 1);
                                } else {
                                    beginnerMap.compute(randomPlayer, (k, ov) -> ov + 1);
                                }
                            }
                            
                            /* analyze stats */
                            Terminal.printLine("--------------------------------------");
                            double moveNum = statsList.stream().mapToInt(l -> l.get(0)).average().getAsDouble();
                            double throwNum = statsList.stream().mapToInt(l -> l.get(1)).average().getAsDouble();
                            double beatingNum = statsList.stream().mapToInt(l -> l.get(2)).average().getAsDouble();
                            Terminal.printLine("Moves: " + moveNum);
                            Terminal.printLine("Throws: " + throwNum);
                            Terminal.printLine("Beatings: " + beatingNum);
                            Terminal.printLine("Moving Quote:" + Math.round((((double) moveNum) / ((double) throwNum)) * 100) / 100d);
                            Terminal.printLine("Beating Quote:" + Math.round((((double) beatingNum) / ((double) moveNum)) * 100) / 100d);
                            Terminal.printLine("");
                            winnerMap.entrySet().stream().forEach(e -> Terminal.printLine("Winner " + e.getKey().toString() + ":" + e.getValue() + " (" + Math.round((((double) e.getValue()) / ((double) rounds)) * 100) / 100d + " %)"));
                            Terminal.printLine("");
                            beginnerMap.entrySet().stream().forEach(e -> Terminal.printLine("Beginner " + e.getKey().toString() + ":" + e.getValue() + " (" + Math.round((((double) e.getValue()) / ((double) rounds)) * 100) / 100d + " %)"));
                        } else {
                            Rule[] rules = (argsList.length < 1) ? new Rule[] {} : Rule.parseRules(argsList[0].split(","));
                            currentGameManager = GameManager.createKI(rules, Player.RED, false, Player.RED);     
                        }
                        break;
                    case INIT:
                        String[] rules = new String[] { "BARRIER" };
                        String sep = (rules.length == 0) ? "" : " ";
                        String args = String.join(" ", rules) + sep + Board.STD_FIELD;
                        currentGameManager = currentGameManager.restart(Rule.fromCliArgument(args.split("\\s", -1)), FieldIdentifier.fromCliArgument(args.split("\\s", -1)), Player.ORDERED_PLAYERS.stream().findFirst().get());
                        break;
                    // TODO DEBUG END
                    case MOVE:
                        if(argsList.length != 2) {
                            throw new SyntaxException("Error, move <SOURCE_FIELD_ID> <DESTINATION_FIELD_ID> expects exactly two arguments.");   
                        }
                        
                        currentGameManager.move(argsList[0], argsList[1]);
                        break;
                    case PRINTX:
                        if(argsList.length != 0) {
                            throw new SyntaxException("Error, printx does not expect any argument.");   
                        }
                        
                        currentGameManager.printx();
                        break;
                    case PRINT:
                        if(argsList.length != 0) {
                            throw new SyntaxException("Error, print does not expect any argument.");   
                        }
                        
                        currentGameManager.print();
                        break;
                    case ABORT:
                        if(argsList.length != 0) {
                            throw new SyntaxException("Error, abort does not expect any argument.");   
                        }
                        
                        currentGameManager.abort();                        
                        break;
                    case ROLLX:
                        if(argsList.length != 0) {
                            throw new SyntaxException("Error, rollx does not expect any argument.");   
                        }
                        
                        currentGameManager.rollx();
                        break;
                    case ROLL:
                        try {
                            if(argsList.length != 1) {
                                throw new SyntaxException("Error, roll <Integer> needs a valid Integer as one and only argument.");   
                            }
                            
                            int num = Integer.valueOf(argsList[0]);
                            currentGameManager.roll(num);
                        } catch(NumberFormatException e) {
                            throw new SyntaxException("Error, roll <NUMBER> needs a valid Integer as argument.");
                        }
                        break;
                    case QUIT:
                        if(argsList.length != 0) {
                            throw new SyntaxException("Error, quit does not expect any argument.");   
                        }
                        
                        return;
                    case START:
                        /* here we determine whether to use standard values or user passed something */
                        Map<Player, FieldIdentifier[]> cIdentifiers = null;
                        Rule[] cRules = null;
                        if(argsList.length == 0) {
                            // use no rules and standard board
                            cIdentifiers = FieldIdentifier.parseFieldIdentifiers(Board.STD_FIELD);
                            cRules = new Rule[0];
                        } else {
                            // check if there are positions available in the last argument
                            if(POSITION_PATTERN.matcher(argsList[argsList.length - 1]).matches()) {
                                cIdentifiers = FieldIdentifier.fromCliArgument(argsList);
                                cRules = Rule.fromCliArgument(argsList);
                            } else {
                                cIdentifiers = FieldIdentifier.parseFieldIdentifiers(Board.STD_FIELD);
                                cRules = Rule.parseRules(argsList);
                            }
                        }
                        
                        currentGameManager = currentGameManager.restart(cRules, cIdentifiers, Player.ORDERED_PLAYERS.stream().findFirst().get());
                        ok();
                        break;
                }
            } catch(IllegalInputException e) {
                Terminal.printLine(e.getMessage());
            }
        }
    }
    
    private static void ok() {
        Terminal.printLine("OK");
    }
}
