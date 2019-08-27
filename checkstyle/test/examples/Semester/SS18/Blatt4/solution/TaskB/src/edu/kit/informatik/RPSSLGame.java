package edu.kit.informatik;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The main game implementation of the RPSSL game.
 * 
 * @author Peter
 * @version 1.0
 */
public class RPSSLGame implements RPSSLResult {
    /**
     * The number of iterations played in one run.
     */
    public static final int ITERATIONS_PER_RUN = 3;
    
    private Queue<RPSSLRun> runs;
    private RPSSLRun currentRun;
    private int runWins;
    private int runLosses;

    /**
     * The constructor for a new RPSSL game. Needs the computer input for all runs as input.
     * 
     * @param enemyRuns The computer behaviour for each run.
     */
    public RPSSLGame(List<List<Shape>> enemyRuns) {
        this.runs = new LinkedList<>();
        for (List<Shape> enemyRun : enemyRuns) {
            this.runs.add(new RPSSLRun(new LinkedList<>(enemyRun)));
        }
        this.currentRun = runs.remove();
    }

    /**
     * Gets the currently up to date prompt of the game.
     * 
     * @return The prompt.
     */
    public String getPrompt() {
        return "RPSSL " + currentRun.getIteration() + ":";
    }

    /**
     * Checks if the game is marked as being over (i.e. all runs are completed and there is a result ready).
     * 
     * @return true, if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return currentRun == null;
    }

    /**
     * Evaluates a move by the player against the computers move.
     * 
     * @param input The players shape used in the move.
     * @return a {@link RPSSLResult} to get a printable result.
     */
    public RPSSLResult evaluate(Shape input) {
        currentRun.checkForIterationWinner(input);
        
        if (currentRun.isRunOver()) {
            // add run win/loss
            switch (currentRun.getRunResult()) {
                case WIN:
                    runWins++;
                    break;
                case LOSE:
                    runLosses++;
                case DRAW:
                    // noone gets points
                    break;
                default:
                    // This should be impossible
                    throw new IllegalStateException();
            }
            
            RPSSLRun runToReturn = currentRun;
            currentRun = runs.poll();
            return runToReturn;
        } else {
            return currentRun;
        }
    }

    @Override
    public String displayResult() {
        if (!isGameOver()) {
            throw new IllegalStateException("The game is not over yet!");
        } else {
            return Result.standingsToResult(runWins, runLosses).toString().toLowerCase();
        }
    }


    /**
     * This models a single run consisting of an amount of iterations in the RPSSL game.
     */
    // This is a private nested class, because only the game should be able to create a run.
    private static final class RPSSLRun implements RPSSLResult {
        private static int runCounter = 1;
        
        private final Queue<Shape> enemyInput;
        private final int runNumber;
        private int wins;
        private int losses;
        
        private RPSSLRun(Queue<Shape> enemyInput) {
            this.enemyInput = enemyInput;
            this.runNumber = runCounter++;
        }

        @Override
        public String displayResult() {
            if (!isRunOver()) {
                return wins + "," + losses + "," + runNumber; 
            } else {
                return Result.standingsToResult(wins, losses).toString().toLowerCase();
            }
        }
        
        private boolean isRunOver() {
            return enemyInput.isEmpty();
        }
        
        private Result getRunResult() {
            if (!isRunOver()) {
                throw new IllegalStateException("The run is still ongoing.");
            }

            return Result.standingsToResult(wins, losses);
        }
        
        private int getIteration() {
            return ITERATIONS_PER_RUN - enemyInput.size() + 1;
        }
        
        private void checkForIterationWinner(Shape input) {
            if (isRunOver()) {
                throw new IllegalStateException("There are no iterations left in this run!");
            }
            
            switch(input.checkForWinner(enemyInput.remove())) {
                case WIN:
                    wins++;
                    break;
                case LOSE:
                    losses++;
                    break;
                case DRAW:
                    // Noone gets a point.
                    break;
                default:
                    // This should be impossible.
                    throw new IllegalStateException();
            }
        }
    } 
}
