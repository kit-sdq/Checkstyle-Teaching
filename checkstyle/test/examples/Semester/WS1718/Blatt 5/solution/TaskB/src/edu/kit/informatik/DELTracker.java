package edu.kit.informatik;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DELTracker {
    private Map<Integer, String> teams;
    private Map<Integer, StandingEntry> standings;

    /**
     * Constructs a new, empty tracker for the DEL. 
     */
    public DELTracker() {
        this.teams = new TreeMap<>();
        this.standings = new HashMap<>();
    }

    /**
     * Adds a new team to the DEL.
     * 
     * @param id The id of the team to add.
     * @param name The name of the team to add.
     * @throws InputException if the input is invalid.
     */
    public void addTeam(int id, String name) throws InputException {
        if (teams.containsKey(id) || teams.containsValue(name)) {
            throw new InputException("a team with this ID or name already exists.");
        } else if (id < 10 || id > 99) {
            throw new InputException("the ID of the team is not in the interval [10,99].");
        }
        
        teams.put(id, name);
        standings.put(id, new StandingEntry(id, 0, 0));
    }

    /**
     * Lists all teams in the DEL.
     * 
     * @return A string representation of the list of DEL teams.
     */
    public String listTeams() {
        return teams.entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    /**
     * Adds a new ice hockey match.
     * 
     * @param idHome The id of the home team.
     * @param goalsHome The number of goals that the home team scored.
     * @param idGuest The id of the guest team.
     * @param goalsGuest The number of goals that the guest team scored.
     * @param time The time the match lasted.
     * @throws InputException if the input is invalid.
     */
    public void addMatch(int idHome, int goalsHome, int idGuest, int goalsGuest, int time) throws InputException {
        if (!teams.containsKey(idHome)) {
            throw new InputException("home team does not exist.");
        } else if (!teams.containsKey(idGuest)) {
            throw new InputException("guest team does not exist.");
        } else if (goalsHome == goalsGuest) {
            throw new InputException("draws are not allowed.");
        } else if (time < 60 || time > 120) {
            throw new InputException("game time is not in range [60;120]");
        }

        StandingEntry home = standings.get(idHome);
        StandingEntry guest = standings.get(idGuest);
        int goalDifference = goalsHome - goalsGuest;
        
        if (time == 60) {
            if (goalDifference < 0) {
                guest.addPoints(3);
                home.addGoalDifference(goalDifference);
                guest.addGoalDifference(-goalDifference);
            } else {
                home.addPoints(3);
                home.addGoalDifference(goalDifference);
                guest.addGoalDifference(-goalDifference);
            }
        } else {
            if (goalDifference < 0) {
                home.addPoints(1);
                guest.addPoints(2);
                home.addGoalDifference(goalDifference);
                guest.addGoalDifference(-goalDifference);
            } else {
                home.addPoints(2);
                guest.addPoints(1);
                home.addGoalDifference(goalDifference);
                guest.addGoalDifference(-goalDifference);
            }
        }
    }

    /**
     * Calculates the current standings of the DEL and returns a string representation. 
     * 
     * @return The string representation of the DEL standings.
     */
    public String listStandings() {
        List<StandingEntry> sortedStandings = new LinkedList<>(standings.values());
        Collections.sort(sortedStandings);

        StringJoiner result = new StringJoiner("\n");
        for (int i = 0; i < sortedStandings.size(); i++) {
            StandingEntry entry = sortedStandings.get(i); 
            StringJoiner entryJoiner = new StringJoiner(",", "(", ")");
            
            entryJoiner.add(String.valueOf(i + 1));
            entryJoiner.add(teams.get(entry.getTeamID()));
            entryJoiner.add(String.valueOf(entry.getPoints()));
            entryJoiner.add(String.valueOf(entry.getGoalDifference()));
            
            result.add(entryJoiner.toString());
        }
        
        return result.toString();
    }
}
