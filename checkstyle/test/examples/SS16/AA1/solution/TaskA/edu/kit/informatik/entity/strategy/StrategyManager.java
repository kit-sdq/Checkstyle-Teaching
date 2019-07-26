package edu.kit.informatik.entity.strategy;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.board.BasicField;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.board.DestinationField;
import edu.kit.informatik.entity.board.FieldIdentifier;
import edu.kit.informatik.entity.manager.GameManager;
import edu.kit.informatik.entity.movement.Movement;
import edu.kit.informatik.entity.movement.generator.GenericMovementGenerator;
import edu.kit.informatik.entity.movement.generator.IMovementGeneration;
import edu.kit.informatik.entity.rule.Rule;
import edu.kit.informatik.entity.strategy.FilterChain.Filter;

// TODO explain concepts: whitelisting (order matters), blacklisting, chaining
// for whitelisting: rules must be put in chain before blacklisting rules
// for blacklisting: the order of rues does not matter in terms of correctness, but you can gain performance if you arrange them well
public class StrategyManager {
    public static final int BRING_INTO_GAME_NUMBER = 6;
    
    private final Map<DiceHookIdentifier<?>, Object> mDiceHooks = new HashMap<>();
    private final IMovementGeneration mMovementGenerator;
    private final FilterChain<Boolean> mFilterChain;
    private final List<Rule> mRules;
    protected static final ITokenizer CREATE_FORWARD_MOVE = new ITokenizer() {
        public Stream<Movement> apply(Integer pDistance, GamePiece pGamePiece) {
            if(pGamePiece.isOnBoard()) {
                int destIndex = pGamePiece.getRelativePosition() + pDistance;
                
                return Stream.of(new Movement(pGamePiece.getRelativePosition(), destIndex, pGamePiece, pDistance, this));
            }
            
            return Stream.empty();
        };
    };
    
    public static FilterChain<Boolean> createFilterChainWhitelist() {
        return new FilterChain<>(false);
    }
    
    public static FilterChain<Boolean> createFilterChainBlacklist() {
        return new FilterChain<>(true);
    }
    
    protected static final ITokenizer CREATE_BACKWARD_MOVE = new ITokenizer() {
        public Stream<Movement> apply(Integer pDistance, GamePiece pGamePiece) {
            // backward moves only apply to game pieces on the board
            if(pGamePiece.isOnBoard()) {    // TODO implement here or in filter? && !(pGamePiece.getCurrentField() instanceof DestinationField)
                int destIndex = pGamePiece.getRelativePosition() - pDistance;
                if(destIndex < 0) {
                    destIndex = Board.SIZE + destIndex;
                }
                
                return Stream.of(new Movement(pGamePiece.getRelativePosition(), destIndex, pGamePiece, pDistance, false, this));
            }
            
            return Stream.empty(); 
        };
    };
    
    protected static final ITokenizer CREATE_BRING_IN_GAME = new ITokenizer() {
        public Stream<Movement> apply(Integer pDistance, GamePiece pGamePiece) {
            if (pDistance == BRING_INTO_GAME_NUMBER) {
                if(!pGamePiece.isOnBoard()) {
                    return Stream.of(new Movement(FieldIdentifier.START_POSITION_INDEX, 0, pGamePiece, pDistance, this));
                }
            }
            
            return Stream.empty();
        }
    };
    
    // restricts moving backward only if allowed
    protected static final Filter<Boolean> FILTER_BACKWARD_MOVE = new Filter<Boolean>() {

        @Override
        protected Boolean doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<Boolean>> pExcludedFilter) {
            BasicField destination = movement.getDestinationFieldRelative(pBoard);
            BasicField source = movement.getSourceFieldRelative(pBoard);
                        
            /* check if backward move at all */
            if(!movement.isForward()) {
                    /* disallow if no bashing */
                    if (!destination.hasGamePiece() || destination.getGamePieceOwner() == movement.getOwner()) {
                        return false;
                    }
                    
                    /* cannot move on or over own start position */
                    List<BasicField> fields = pBoard.getFieldsRelative(movement.getOwner(), movement.getSourceIndex(), movement.getDestinationIndex(), false, false);
                    if(fields.stream().filter(f -> f.getRelativePosition(movement.getOwner()) == 0).findAny().isPresent()) {
                        return false;
                    }
                    
                    /* moving backward from source field is not allowed */
                    if(source instanceof DestinationField) {
                        return false;
                    }
            }
            
            return next(movement, pGamePieces, pBoard, pExcludedFilter);
        }
    };
    
    protected static final Filter<Boolean> FILTER_BARRIER = new Filter<Boolean>() {

        @Override
        protected Boolean doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<Boolean>> pExcludedFilter) {            
            // iterate over all fields that are traversed
            // must enforce: do not bash barrier fields & do not traverse barrier fields
            List<BasicField> fields = pBoard.getFieldsRelative(movement.getOwner(), movement.getSourceIndex(), movement.getDestinationIndex(), movement.isForward(), true);
            
            // TODO debug fields.stream().forEach(s -> System.out.println(s.getAbsolutePosition()));
            // TODO debug System.out.println();
            
            // path has barrier?
            if(fields.stream().filter(field -> field.hasGamePiece() && field.getGamePieces().size() == 2).findAny().isPresent()) {
                return false;
            }

            //System.out.println(fields.stream().map(BasicField::getAbsolutePosition).map(String::valueOf).collect(Collectors.joining(","))); // TODO debug print path
            return next(movement, pGamePieces, pBoard, pExcludedFilter);
        }
    };
    
    protected static final Filter<Boolean> FILTER_DESTINATION_FIELD_BLOCKED = new Filter<Boolean>() {
        @Override
        protected Boolean doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<Boolean>> pExcludedFilter) {
            BasicField destination = movement.getDestinationFieldRelative(pBoard);
     
            /* the field itself enforces some constraints (such as max pieces per field, not bashing own pieces, etc.) */
            if(!destination.isPlacementValid(movement.getGamePiece()) && !destination.canBeat(movement.getGamePiece())) {
                return false;
            }
            
            return next(movement, pGamePieces, pBoard, pExcludedFilter);
        }
    };
    
    protected static final Filter<Boolean> FILTER_NOJUMP = new Filter<Boolean>() {
        @Override
        protected Boolean doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<Boolean>> pExcludedFilter) {
            List<BasicField> fields = pBoard.getFieldsRelative(movement.getOwner(), movement.getSourceIndex(), movement.getDestinationIndex(), movement.isForward(), true);
            boolean isViolated = fields.stream().limit(movement.getDistance() - 1).filter(m -> m instanceof DestinationField).filter(BasicField::hasGamePiece).findAny().isPresent();
            
            if(isViolated) {
                return false;
            }
            
            return next(movement, pGamePieces, pBoard, pExcludedFilter);
        }
    };
    
    // note: filter is quite complex: cannot be static
    protected final Filter<Boolean> FILTER_ROLL = new Filter<Boolean>() {
        @Override
        protected Boolean doFilter(Movement movement, List<GamePiece> pGamePieces, Board pBoard, Collection<Filter<Boolean>> pExcludedFilter) {
            /* active if the special number was rolled */
            if(movement.getDistance() == BRING_INTO_GAME_NUMBER) {
                /* are there any pieces in the start fields? */
                if(pGamePieces.stream().filter(field -> field.isOnStartField()).findAny().isPresent()) {
                    BasicField playersStartField = pBoard.getField(movement.getOwner(), movement.getOwner().getStartPosition());

                    /* start position is not blocked? */
                    if(!playersStartField.isBlockedByOwnPiece(movement.getOwner())) {
                        /* no -> player must bring piece in game; filter out any other moves */
                        if(movement.getSourceIndex() != FieldIdentifier.START_POSITION_INDEX) {
                            return false;
                        }
                    } else {
                        /* yes -> player must move blocking pieces */
                        int cIndex = BRING_INTO_GAME_NUMBER;
                        GamePiece blockingGamePiece = playersStartField.getGamePieces().iterator().next();
                        boolean canAnyoneMove = true;
                        while(blockingGamePiece != null && !canGamePieceMove(blockingGamePiece, movement.getDistance(), pGamePieces, pBoard)) {
                            GamePiece tempBlockingGamePiece = GameManager.getGamePieceAtRelativePosition(pGamePieces, cIndex);
                            if(tempBlockingGamePiece != null) {
                                blockingGamePiece = tempBlockingGamePiece;
                            } else {
                                canAnyoneMove = false;
                                break;
                            }
                            cIndex += BRING_INTO_GAME_NUMBER;
                        }
                        
                        if(!canAnyoneMove) {
                            // TODO all right here? do we need to roll the dice once more or so?
                        }
                        
                        /* move not valid if this is not the blocking piece  */
                        if(!movement.getGamePiece().equals(blockingGamePiece)) {
                            return false;
                        }
                    }
                }
            }
            
            return next(movement, pGamePieces, pBoard, pExcludedFilter);
        }
        
        // this is quite complex: we apply the whole chain on the game piece which we want to examine, but exclude the current role!! -> TODO explain why!
        private boolean canGamePieceMove(GamePiece pBlockingGamePiece, int pDistance, List<GamePiece> pGamePieces, Board pBoard) {
            Stream<Movement> offeredMovements = StrategyManager.this.mMovementGenerator.offer(pDistance, Stream.of(pBlockingGamePiece));
            return offeredMovements.filter(movement -> StrategyManager.this.mFilterChain.doFilter(movement, pGamePieces, pBoard, Arrays.asList(this))).findAny().isPresent();
        }
    };
                
    public StrategyManager(Rule[] pRules) {
        this(pRules, createFilterChainBlacklist());
    }
                
    public StrategyManager(Rule[] pRules, FilterChain<Boolean> pFilterChain) {
        this.mRules = Arrays.asList(pRules);
        this.mFilterChain = pFilterChain;
        this.mMovementGenerator = new GenericMovementGenerator();
        
        applyCustomMovementsAndFilters(this.mFilterChain);
        applyCustomDiceHooks(new DiceHookPlayerChange(), new DiceHookAfterMove());
    }
    
    protected void applyCustomDiceHooks(DiceHookPlayerChange pPlayerChange, DiceHookAfterMove pAfterThrow) {
        addDiceHook(DiceHookIdentifier.PLAYER_CHANGE, Objects.requireNonNull(pPlayerChange));
        if(this.mRules.contains(Rule.TRIPLY)) {
            addDiceHook(DiceHookIdentifier.AFTER_MOVE, new DiceHookAfterMoveTriply());
        } else {
            addDiceHook(DiceHookIdentifier.AFTER_MOVE, Objects.requireNonNull(pAfterThrow));
        }
    }

    protected void applyCustomMovementsAndFilters(FilterChain<Boolean> pFilterChain) {
        /* prepare generators */
        getMovementGenerator().addToChainFront(CREATE_BRING_IN_GAME);
        getMovementGenerator().addToChainEnd(CREATE_FORWARD_MOVE);
        if(getRules().contains(Rule.BACKWARD)) {
            getMovementGenerator().addToChainEnd(CREATE_BACKWARD_MOVE);
        }
        
        /* prepare filters */
        if(getRules().contains(Rule.BACKWARD)) {
            pFilterChain.addFront(FILTER_BACKWARD_MOVE);
        }
        
        pFilterChain.addFront(FILTER_DESTINATION_FIELD_BLOCKED);
        
        if(getRules().contains(Rule.BARRIER)) {
            pFilterChain.addFront(FILTER_BARRIER);
        }
        
        pFilterChain.addFront(FILTER_ROLL);
        
        if(getRules().contains(Rule.NOJUMP)) {
            pFilterChain.addFront(FILTER_NOJUMP);
        }
    }
    
    // caution: overwrites hook at current pId!!
    protected <T> void addDiceHook(DiceHookIdentifier<T> pId, T pHook) {
        this.mDiceHooks.put(pId, pHook);
    }
    
    protected List<Rule> getRules() {
        return Collections.unmodifiableList(this.mRules);
    }
    
    public Stream<Movement> filter(Stream<Movement> pOfferedMovements, List<GamePiece> pGamePieces, Board pBoard) {
        // TODO debug System.out.println(pOfferedMovements.map(Movement::getDestinationIndex).map(String::valueOf).collect(Collectors.joining(",")));
        return pOfferedMovements.filter(movement -> this.mFilterChain.doFilter(movement, pGamePieces, pBoard, Collections.emptyList()));
    }

    public int getNumberOfMaxPiecesPerField(Class<? extends BasicField> pFieldClass) {
        /* special rule for destination fields */
        if(pFieldClass.equals(DestinationField.class)) {
            return 1;
        }
        
        // all other fields
        return (this.mRules.contains(Rule.BARRIER)) ? 2 : 1;
    }

    // TODO programmatically assert at compile time that all hooks are assigned!
    public <T> T getThrowManager(DiceHookIdentifier<T> pHookId) {  
        return pHookId.retreiveImplementation(this.mDiceHooks, pHookId);
    }

    public IMovementGeneration getMovementGenerator() {
        return this.mMovementGenerator;
    }
}
