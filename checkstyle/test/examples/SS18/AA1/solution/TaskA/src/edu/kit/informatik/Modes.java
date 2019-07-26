package edu.kit.informatik;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Modes {
    STANDARD("standard", new HashMap<State, List<Command>>() {
        {
            put(State.PLAYING, Arrays.asList(
                    Command.PRINT, Command.STATE, Command.THROWIN, Command.TOKEN, Command.QUIT
            ));
            put(State.WIN, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
            put(State.DRAW, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
        }
    }),
    FLIP("flip", new HashMap<State, List<Command>>() {
        {
            put(State.PLAYING, Arrays.asList(
                    Command.PRINT, Command.STATE, Command.THROWIN, Command.FLIP, Command.TOKEN, Command.QUIT
            ));
            put(State.WIN, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
            put(State.DRAW, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
        }
    }),
    REMOVE("remove", new HashMap<State, List<Command>>() {
        {
            put(State.PLAYING, Arrays.asList(
                    Command.PRINT, Command.STATE, Command.THROWIN, Command.TOKEN, Command.REMOVE, Command.QUIT
            ));
            put(State.WIN, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
            put(State.DRAW, Arrays.asList(Command.PRINT, Command.STATE, Command.TOKEN, Command.QUIT));
        }
    });

    private String literal;
    private Map<State, List<Command>> availableCommands;

    Modes(String literal, Map<State, List<Command>> commands) {
        this.literal = literal;
        this.availableCommands = commands;
    }

    public String getLiteral() {
        return literal;
    }

    public List<Command> getAvailableCommands(State state) {
        return availableCommands.get(state);
    }

    public static Modes parseString(String toParse) throws InputException {
        if (toParse.equals(STANDARD.literal)) return STANDARD;
        else if (toParse.equals(FLIP.literal)) return FLIP;
        else if (toParse.equals(REMOVE.literal)) return REMOVE;
        else throw new InputException("Invalid Mode Tag: " +  toParse);
    }
}
