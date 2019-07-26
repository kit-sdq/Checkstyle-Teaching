package edu.kit.informatik;

import java.util.Objects;

public class StandingEntry implements Comparable<StandingEntry> {
    private int teamID;
    private int points;
    private int goalDifference;

    /**
     * Creates a new standings entry. There should be one for each team in the DEL.
     * 
     * @param teamID The id of the team that is bound to this DEL entry.
     * @param points The points that the team currently has.
     * @param goalDifference The goal difference that the team currently has.
     */
    public StandingEntry(int teamID, int points, int goalDifference) {
        this.teamID = teamID;
        this.points = points;
        this.goalDifference = goalDifference;
    }

    /**
     * Gets the id of the team.
     * 
     * @return The teams id.
     */
    public int getTeamID() {
        return teamID;
    }

    /**
     * Gets the current points of the team.
     * 
     * @return The points of the team.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds an amount of points to the teams total points.
     * 
     * @param points The points to add.
     */
    public void addPoints(int points) {
        this.points += points;
    }

    /**
     * Gets the goal difference of the team.
     * 
     * @return The goal difference of the team.
     */
    public int getGoalDifference() {
        return goalDifference;
    }

    /**
     * Adds goals to the goal difference of the team.
     * 
     * @param goalDifference The goals to add.
     */
    public void addGoalDifference(int goalDifference) {
        this.goalDifference += goalDifference;
    }
    
    @Override
    public int compareTo(StandingEntry other) {
        if (points == other.points) {
            if (goalDifference == other.goalDifference) {
                if (teamID == other.teamID) {
                    return 0;
                } else {
                    return teamID - other.teamID;
                }
            } else {
                return other.goalDifference - goalDifference;
            }
        } else {
            return other.points - points;
        }
        
//        Possible with ternary operator, but not that readable
//        int compPoints = Integer.compare(points, other.points);
//        int compGD = Integer.compare(goalDifference, other.goalDifference);
//        return compPoints == 0 ? (compGD == 0 ? Integer.compare(teamID, other.teamID) : compGD) : compPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StandingEntry that = (StandingEntry) o;
        return teamID == that.teamID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamID);
    }
}
