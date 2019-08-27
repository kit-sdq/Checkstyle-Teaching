package edu.kit.informatik.entity.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import edu.kit.informatik.Terminal;
import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.IGamePieceProvider;
import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.board.BasicField;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.board.DestinationField;
import edu.kit.informatik.entity.board.FieldIdentifier;
import edu.kit.informatik.entity.movement.Movement;
import edu.kit.informatik.entity.rule.Rule;
import edu.kit.informatik.entity.strategy.DiceHookIdentifier;
import edu.kit.informatik.entity.strategy.StrategyManager;
import edu.kit.informatik.exception.IllegalInputException;
import edu.kit.informatik.exception.SemanticsException;
import edu.kit.informatik.exception.SyntaxException;

public class GameManager implements IGamePieceProvider {
    public static final GameManager DUMMY_INACTIVE_GAME_MANAGER = new InactiveGameManager();
    private final Board mBoard;
    private final Rule[] mRules;
    private Player mCurrentPlayer;
    private final HashMap<Player, ArrayList<GamePiece>> mGamePieces = new HashMap<>();
    private final StrategyManager mStrategyManager;
    private boolean mIsActive = false;
    private boolean mIsMoveTurn = false;
    private int mRemainingThrows = 0;
    private int mCurrentRoundPlayerThrowNumber = 0;
    private List<Movement> mCurrentMovementOffering = new ArrayList<>();
    private int mLastNumberOnDice = 0;
    private Player mWinner = null;
    
    private static final int ROLL_MIN = 1;
    private static final int ROLL_MAX = 6;
    
    /* stats */
    private int mNumberOfMoves = 0;
    private int mNumberOfThrows = 0;
    private int mNumberOfBeatings = 0;
    
    private GameManager() {
        this.mBoard = null;
        this.mRules = new Rule[] {};
        this.mStrategyManager = null;
    }
    
    public Rule[] getActiveRules() {
        return this.mRules.clone();
    }
    
    protected GameManager(Rule[] pRules, Map<Player, FieldIdentifier[]> pFieldIdentifiers, Player pStartPlayer) throws SemanticsException {
        this(pRules, pFieldIdentifiers, pStartPlayer, new StrategyManager(pRules));
    }
            
    protected GameManager(Rule[] pRules, Map<Player, FieldIdentifier[]> pFieldIdentifiers, Player pStartPlayer, StrategyManager pStrategyManager) throws SemanticsException {
        Stream.of(Player.values()).forEach(p -> this.mGamePieces.put(p, new ArrayList<>(Board.DESTINATION_FIELD_NUMBER)));
        this.mStrategyManager = pStrategyManager;
        this.mBoard = new Board(this, pFieldIdentifiers, this.mStrategyManager.getNumberOfMaxPiecesPerField(BasicField.class), this.mStrategyManager.getNumberOfMaxPiecesPerField(DestinationField.class));
        this.mRules = pRules;
        this.mRemainingThrows = 1;
        this.mIsActive = true;
        this.mCurrentPlayer = pStartPlayer;
        
        check();
    }
    
    public Player getNextPlayer() {
        return this.mCurrentPlayer.getNextPlayer();
    }
    
    public void abort() throws SemanticsException {
        if(!this.mIsActive) {
            throw new SemanticsException("Error, cannot abort a game that is not running!");
        }
        
        this.mIsActive = false;
    }
    
    // convenience method
    public void move(String pSourceIdentifierString,String pDestinationIdentifierString) throws SemanticsException, SyntaxException {
        move(new FieldIdentifier(pSourceIdentifierString), new FieldIdentifier(pDestinationIdentifierString));
    }
    
    public void move(FieldIdentifier pSourceFieldIdentifier, FieldIdentifier pDestinationFieldIdentifier) throws SemanticsException {
        assertActive();
        if(!this.mIsMoveTurn) {
            throw new SemanticsException("Error, you must roll the dice first!");
        }
        
        Movement selectedMovement = mCurrentMovementOffering.stream()
                .filter(m -> m.getSourceField(mBoard).toString().equals(pSourceFieldIdentifier.getName()))
                .filter(m -> m.getDestinationField(mBoard).toString().equals(pDestinationFieldIdentifier.getName()))
                .findAny()
                .orElseThrow(SemanticsException.createInstance("Error, please select an existing movement source."));
        
        /* after getting the movement which the user had in mind, determine the destination field */
        BasicField selectedDestinationField = selectedMovement.getDestinationField(this.mBoard);
        /* beat anything on destination before moving on */
        if(selectedDestinationField.canBeat(selectedMovement.getGamePiece())) {
            mBoard.beatAll(selectedDestinationField, selectedMovement.getGamePiece());
            this.mNumberOfBeatings++;
        }
        
        mBoard.placeGamePiece(selectedDestinationField, selectedMovement.getGamePiece());
        Terminal.printLine(selectedDestinationField.toString());
        
        moveFinished(selectedMovement);
        this.mNumberOfMoves++;
    }
    
    // pChosenMove isNull if movementOffering is empty and therefore the user decision was skipped
    protected void moveFinished(Movement pChosenMovement) {
        this.mIsMoveTurn = false;
        if(isGameOver()) {
            this.mWinner = this.mCurrentPlayer;
            Terminal.printLine(this.mCurrentPlayer + " winner");
            try {
                abort();    // TODO cancel game automatically if over?
            } catch (SemanticsException e) {
                // must not happen!
            }
        } else {
            this.mRemainingThrows = this.mStrategyManager.getThrowManager(DiceHookIdentifier.AFTER_MOVE).invoke(this.mRemainingThrows, 
                    this.mCurrentRoundPlayerThrowNumber,
                    pChosenMovement,
                    this.mGamePieces.get(this.mCurrentPlayer), 
                    this.mLastNumberOnDice);
            this.mCurrentPlayer = getNextThrower();
            Terminal.printLine(this.mCurrentPlayer.toString());
        }
    }
    
    // returns the player who rolls the dice next
    protected Player getNextThrower() {
        if(this.mRemainingThrows == 0) {
            Player nextPlayer = getNextPlayer();
            this.mCurrentRoundPlayerThrowNumber = 0;
            this.mRemainingThrows = this.mStrategyManager.getThrowManager(DiceHookIdentifier.PLAYER_CHANGE).invoke(this.mRemainingThrows);
            
            return nextPlayer;
        } else {
            return this.mCurrentPlayer;
        }
    }

    public void printx() throws SemanticsException {
        assertActive();
        this.mBoard.printx();
    }
    
    public void rollx() throws SemanticsException {
        Random rand = new Random();
        int num = rand.nextInt(ROLL_MAX) + 1;
        Terminal.printLine("DICE: " + num);
        
        roll(num);
    }
    
    public void roll(int pNum) throws SemanticsException {
        assertActive();
        if(this.mIsMoveTurn) {
            throw new SemanticsException("Error, you have already rolled the dice - now you must move!");
        }
        
        if(pNum < ROLL_MIN || pNum > ROLL_MAX) {
            throw new SemanticsException("Error, you must roll a number between " + ROLL_MIN + " and " + ROLL_MAX + ", not: " + pNum + ".");
        }
        
        // clear the list of possible moves to populate it freshly
        this.mCurrentMovementOffering.clear();
        
        computeMovements(pNum).forEach(movement -> {
            this.mCurrentMovementOffering.add(movement);
            Terminal.printLine(printMovement(movement));
        });
        
        rolled(pNum);
        this.mNumberOfThrows++;
    }
    
    protected String printMovement(Movement pMovement) {
        return pMovement.toString(mBoard);
    }

    protected void rolled(int pNum) {
        this.mCurrentRoundPlayerThrowNumber++;
        this.mIsMoveTurn = true;
        this.mRemainingThrows--;
        this.mLastNumberOnDice = pNum;
        
        /* finish move automatically if there aren't any possibilities for the player */
        if(this.mCurrentMovementOffering.isEmpty()) {
            moveFinished(null);
        } else {
            // show the current player's color (actually unnecessary IMHO, but well..)
            Terminal.printLine(this.mCurrentPlayer.toString());
        }
    }

    /* in Solr terminology this is an Analyzer */
    protected Stream<Movement> computeMovements(int pNum) throws SemanticsException {
        /* this is a typical TOKENIZER operation (for all folks which are familiar with Apache Solr) */
        Stream<Movement> offeredStream = 
                mStrategyManager.getMovementGenerator()
                .offer(pNum, this.mGamePieces.get(this.mCurrentPlayer).stream());
        
        /* this is the FILTER chain */
        offeredStream = this.mStrategyManager.filter(offeredStream, Collections.unmodifiableList(this.mGamePieces.get(this.mCurrentPlayer)), this.mBoard);
        
        return offeredStream.sorted(new Comparator<Movement>() {

            @Override
            public int compare(Movement m1, Movement m2) {
                BasicField s1 = m1.getSourceField(mBoard);
                BasicField s2 = m2.getSourceField(mBoard);
                
                if(s1.getAbsolutePosition() == s2.getAbsolutePosition()) {
                    /* sort by destination field if source field is identical */
                    BasicField d1 = m1.getDestinationField(mBoard);
                    BasicField d2 = m2.getDestinationField(mBoard); 
                    return d1.getAbsolutePosition() - d2.getAbsolutePosition();
                } else {
                    return s1.getAbsolutePosition() - s2.getAbsolutePosition();
                }
            }
        });   
    }
    
    public void print() throws SemanticsException {
        assertActive();
        for(Player cPlayer : Player.ORDERED_PLAYERS) {
            Terminal.printLine(listGamePiecePositions(cPlayer));
        }
        
        Terminal.printLine(this.mCurrentPlayer.toString());
    }
    
    public static GameManager start(Rule[] pRules, Map<Player, FieldIdentifier[]> pFieldIdentifiers, Player pStartPlayer, StrategyManager pStrategyManager) throws SemanticsException {
        return new GameManager(Optional.of(pRules).get(), Optional.of(pFieldIdentifiers).get(), Optional.of(pStartPlayer).get(), Optional.of(pStrategyManager).get());
    }
    
    // restart with same strategy manager
    public GameManager restart(Rule[] pRules, Map<Player, FieldIdentifier[]> pFieldIdentifiers, Player pStartPlayer) throws SemanticsException {
        if(this.mIsActive) {
            throw new SemanticsException("Error, you must abort a match or the match must be over before starting a new one.");
        }
        
        return start(pRules, pFieldIdentifiers, pStartPlayer, (this.mStrategyManager == null) ? new StrategyManager(pRules) : this.mStrategyManager);
    }
    
    // do some safety checks
    private void check() throws SemanticsException {
        // check if every player has 4 gamepieces placed
        if(!this.mGamePieces.values().stream().allMatch(v -> v.size() == Board.DESTINATION_FIELD_NUMBER)) {
            throw new IllegalStateException("Error, the player has to place all of its game pieces on the board before starting game.");
        }
        
        // check if no one has already won at the beginning
        if(isGameOver()) {
            throw new SemanticsException("Error, the game must not be over at start!");
        }
    }
    
    protected boolean isGameOver() {
        // check for every game piece of the player if it is at a destination field
        return this.mGamePieces.values().stream().map(List::stream).anyMatch(v -> v.map(GamePiece::getCurrentField).allMatch(field -> (field instanceof DestinationField)));
    }

    // pField = null for start field
    public GamePiece createGamePiece(Player pPlayer, BasicField pField) {
        if(this.mGamePieces.get(pPlayer).size() >= Board.DESTINATION_FIELD_NUMBER) {
            throw new IllegalStateException("Error, no more game pieces can be created for user: " + toString() + ".");
        }
        GamePiece cGamePiece = new GamePiece(pPlayer, pField);
        mGamePieces.get(pPlayer).add(cGamePiece);
        
        return cGamePiece;
    }
    
    // returns the one out of the list which is at given field index or null if none found
    public static GamePiece getGamePieceAtRelativePosition(List<GamePiece> pGamePieces, int pRelativePosition) {
        return pGamePieces.stream().filter(piece -> piece.getRelativePosition() == pRelativePosition).findAny().orElse(null);
    }
    
    public String listGamePiecePositions(Player pPlayer) {
         return this.mGamePieces.get(pPlayer).stream()
                .map(GamePiece::getCurrentField)
                .sorted(Comparator.nullsFirst(Comparator.comparing(BasicField::getAbsolutePosition)))
                .map((BasicField f) -> ((f == null) ? Board.getStartFieldName(pPlayer) : f.toString()))
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    
    private void assertActive() throws SemanticsException {
        if(!this.mIsActive) {
            throw new SemanticsException("Error, this operation requires a running game!");
        }
    }
    
    /*** TODO DEBUG ***/
    public void debug_relativeGPPositions(Player pPlayer) {
        for(Player cPlayer : Player.ORDERED_PLAYERS) {
            Terminal.printLine(cPlayer.toString());
            Terminal.printLine(">" + listRelativePositions(pPlayer));
            Terminal.printLine(listGamePiecePositions(pPlayer));
            Terminal.printLine("");
        }
        
        Terminal.printLine(this.mCurrentPlayer.toString());
    }
    
    /*** TODO DEBUG ***/
    public List<Integer> getStats() {
        List<Integer> stats = new ArrayList<>(3);
        stats.add(this.mNumberOfMoves);
        stats.add(this.mNumberOfThrows);
        stats.add(this.mNumberOfBeatings);
        
        return stats;
    }
    
    // TODO test method
    public String listRelativePositions(Player pPlayer) {
        return this.mGamePieces.get(pPlayer).stream()
                .map(GamePiece::getCurrentField)
                .filter(f -> !f.isStartField())
                .map(f -> f.getRelativePosition(pPlayer))
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
    
    private static final class InactiveGameManager extends GameManager {
        private InactiveGameManager() {
            super();
            super.mIsActive = false;
        }
    }
    
    // TODO we could restrict player number... but this would imply major changes
    private static final class KIGameManager extends GameManager {
        private final Player mHumanPlayer;
        private boolean mSkipAlways = false;
        
        private KIGameManager(Rule[] pRules, Player pHumanPlayer, boolean pSkipUserEntries, Player pStartPlayer) throws SemanticsException, SyntaxException {
            super(pRules, FieldIdentifier.fromCliArgument(new String[] { Board.STD_FIELD }), pStartPlayer);
            this.mHumanPlayer = pHumanPlayer;
            
            if(pSkipUserEntries) {
                skipAlways();
            }
            
            simulate();
        }

        private void simulate() {
            while(!super.isGameOver()) {
                if(super.mCurrentPlayer == this.mHumanPlayer) {
                    doHumanMove();
                    continue;
                }
                
                try {
                    super.rollx();
                    // we could implement a true KI at this point...
                    if(super.mCurrentMovementOffering.size() > 0) {
                        super.move(super.mCurrentMovementOffering.get(0).getSourceField(super.mBoard).toString(),
                                super.mCurrentMovementOffering.get(0).getDestinationField(super.mBoard).toString());
                    }
                } catch (SemanticsException | SyntaxException e) {
                    // should never happen!!!
                    throw new RuntimeException(e);
                }
            }
            
            Terminal.printLine("--------------------------------------------------------------------------------");
            Terminal.printLine("");
            Terminal.printLine("Game Statistics:");
            Terminal.printLine("moves: " + super.mNumberOfMoves);
            Terminal.printLine("throws: " + super.mNumberOfThrows);
            Terminal.printLine("quote: " + Math.round((((double) super.mNumberOfMoves) / ((double) super.mNumberOfThrows)) * 100) / 100d);
            Terminal.printLine("");
            Terminal.printLine("beatings:" + super.mNumberOfBeatings);
            Terminal.printLine("quote: " + Math.round((((double) super.mNumberOfBeatings) / ((double) super.mNumberOfMoves)) * 100) / 100d);
        }
        
        private void doHumanMove() {
            // show the current board to our human player
            try {
                Terminal.printLine("----------------------------------------------------------------------------------");
                printx();
                Terminal.printLine("----------------------------------------------------------------------------------");
            } catch (SemanticsException e) {
                // won't happen because game is going on...
            }
            
            // roll the dice for our human player...
            try {
                rollx();
            } catch (SemanticsException e) {
                // should never happen!!!
                throw new RuntimeException(e);
            }
            if(super.mCurrentMovementOffering.size() == 0) {
                System.out.println("Sorry, you cannot do anything bro. [PRESS ENTER]");
                if(!this.mSkipAlways) {
                    Terminal.readLine();
                } else {
                    Terminal.printLine("ENTER");
                }
            } else {
                boolean success = false;
                
                do {
                    try {
                        Terminal.printLine("I choose move with index [0]: ");
                        String input = (this.mSkipAlways) ? "" : Terminal.readLine();
                        if(input.trim().isEmpty()) {
                            input = "0";    // 0 is default, for faster gaming!
                            Terminal.printLine("ENTER");
                        }
                        
                        int index = Integer.parseInt(input);
                        if(index < super.mCurrentMovementOffering.size()) {
                            Movement selection = super.mCurrentMovementOffering.get(index);
                            move(selection.getSourceField(super.mBoard).toString(), selection.getDestinationField(super.mBoard).toString());
                            success = true;
                        } else {
                            throw new SemanticsException("Error, select a movement, providing index within interval: [0, " + (super.mCurrentMovementOffering.size() - 1)  + "]");
                        }
                    } catch(IllegalInputException e) {
                        Terminal.printLine(e.getMessage());
                    } catch(NumberFormatException e) {
                        Terminal.printLine("Error, input an Integer as index!");
                    }
                } while(!success);
            }
        }
        
        public void skipAlways() {
            this.mSkipAlways = true;
        }
        
        @Override
        protected Stream<Movement> computeMovements(int pNum) throws SemanticsException {
            if(super.mCurrentPlayer != this.mHumanPlayer) {
                // note: rule at the bottom is the most important (this is a stable sort)
                return super.computeMovements(pNum)
                        // 3.) use the piece which traveled the most distance
                        .sorted(Comparator.comparingInt(Movement::getDestinationIndex).reversed())
                        // 2.) play more aggressive! yeah!! beat as soon as you can!
                        .sorted(Comparator.comparing(m -> !m.canBeat(super.mBoard)))
                        // 1.) enter destination field as soon as possible
                        .sorted(Comparator.comparing(m -> !(m.getDestinationField(super.mBoard) instanceof DestinationField)));
            }
            else {
                /* define a custom strategy for the human moves which are offered first */
                return super.computeMovements(pNum)
                        // 3.) use the piece which traveled the most distance
                        .sorted(Comparator.comparingInt(Movement::getDestinationIndex).reversed())
                        // 2.) play as defensive as possible!
                        .sorted(Comparator.comparing(m -> !m.canBeat(super.mBoard)))
                        // 1.) enter destination field as soon as possible
                        .sorted(Comparator.comparing(m -> !(m.getDestinationField(super.mBoard) instanceof DestinationField)));              
            }
        }
        
        @Override
        protected String printMovement(Movement pMovement) {
            /* add the hint if player can beat another one */
            return pMovement.toString(super.mBoard) + (!(pMovement.getDestinationField(super.mBoard).canBeat(pMovement.getGamePiece())) ? "" : " (BEAT " + pMovement.getDestinationField(super.mBoard).getGamePieceOwner() + ")");
        }
    }

    public static GameManager createKI(Rule[] pRules, Player pHumanPlayer, boolean pSkipUserEntries, Player pStartPlayer) throws SemanticsException, SyntaxException {
        return new KIGameManager(pRules, pHumanPlayer, pSkipUserEntries, pStartPlayer);
    }

    public Player getWinner() {
        return this.mWinner;
    }
}
