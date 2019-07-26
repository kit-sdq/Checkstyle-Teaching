package edu.kit.informatik.ludo;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import edu.kit.informatik.ludo.fields.Field;
import edu.kit.informatik.ludo.fields.Move;
import edu.kit.informatik.ludo.fields.Player;
import edu.kit.informatik.ludo.rules.IRule;
import edu.kit.informatik.ludo.rules.Rule;

/**
 * Skeleton implementation of the board interface to lower the effort required when implementing it.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
abstract class BoardSkeleton implements IBoard {
    
    private static final Supplier<Stream<Move>> NO_MOVES = Stream::empty;
    private static final Random RANDOM = new Random();
    
    private Player player;
    private Phase phase;
    
    private int rollCount;
    private int lastRolled;
    
    private Supplier<Stream<Move>> moves;
    
    private void checkInProcess(final boolean is) {
        checkIfTrue(is == inProcess());
    }
    
    private void checkPhase(final Phase is) {
        checkIfTrue(is == phase());
    }
    
    private void checkIfTrue(final boolean is) {
        if (!is) {
            throw new IllegalStateException();
        }
    }
    
    /**
     * Returns the rules of the board.
     * 
     * @return the rules of the board
     */
    protected abstract Stream<IRule> rules();
    
    @Override public final boolean inProcess() {
        return moves != null;
    }
    
    @Override public final Phase phase() {
        checkInProcess(true);
        
        return phase;
    }
    
    @Override public final void start(final Stream<? extends IRule> rules,
            final Stream<? extends Stream<? extends Field>> positions) {
        checkInProcess(false);
        
        start0(Stream.concat(Stream.of(Rule.BASE), rules), positions);
        
        player = Player.byIndex(0);
        phase = Phase.ROLL;
        
        rollCount = 0;
        lastRolled = -1;
        
        moves = NO_MOVES;
        
        if (Field.stream().anyMatch((field) -> rules().noneMatch((rule) -> rule.allows(this, field)))
                || Player.stream().anyMatch(this::isWonBy)) {
            abort();
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * Starts a new game with the provided rules and starting situation.
     * 
     * @param rules the rules
     * @param positions the starting situation
     */
    protected abstract void start0(final Stream<? extends IRule> rules,
            final Stream<? extends Stream<? extends Field>> positions);
    
    @Override public final void abort() {
        checkInProcess(true);
        
        abort0();
        
        moves = null;
    }
    
    /**
     * Aborts the current game.
     */
    protected abstract void abort0();
    
    @Override public final Player player() {
        checkInProcess(true);
        
        return player;
    }
    
    @Override public final int lastRolled() {
        checkInProcess(true);
        
        return lastRolled;
    }
    
    @Override public final int rollCount() {
        checkInProcess(true);
        
        return rollCount;
    }
    
    @Override public final Stream<Move> roll(final int value) {
        checkInProcess(true);
        checkPhase(Phase.ROLL);
        
        if (value < LOWEST_ROLL || value > HIGHEST_ROLL) {
            throw new IllegalArgumentException();
        }
        
        assert moves == NO_MOVES;
        
        lastRolled = value;
        rollCount++;
        
        final List<Move> allowed = positions0(player())
                .map((start) -> rules().map((rule) -> rule.allowed(this, start))
                        .filter((move) -> move != null)/*.distinct()*/
                        .filter((move) -> rules().noneMatch((rule) -> rule.blocks(this, move)))
                        .sorted(Comparator.comparing(Move::from).thenComparing(Move::to)))
                .reduce(Stream::concat).get().collect(Collectors.toList());
        if (!allowed.isEmpty()) {
            final Move first = allowed.get(0);
            if (first.from().isBase()) {
                moves = () -> Stream.of(first);
            } else {
                final Optional<Move> clearBase = lastRolled == MOVE_OUT ? allowed.stream().filter((m) -> {
                    final Field start = Field.base(player).move(player, 1).to();
                    for (Field c = start, check = m.from(); c != null && c != check; c = c.move(player, MOVE_OUT).to())
                        if (owner0(c) != player)
                            return false;
                    return true;
                }).findAny() : Optional.empty();
                if (clearBase.isPresent()) {
                    final Move move = clearBase.get();
                    moves = () -> Stream.of(move);
                } else {
                    moves = () -> allowed.stream();
                }
            }
            phase = Phase.MOVE;
        } else if (rules().noneMatch((rule) -> rule.rollAgain(this))) {
            player = player.next();
            rollCount = 0;
        }
        
        return moves.get();
    }
    
    @Override public final Stream<Move> rollx() {
        return roll(RANDOM.nextInt(1 + HIGHEST_ROLL - LOWEST_ROLL) + LOWEST_ROLL);
    }
    
    @Override public final boolean canMove(final Field start, final Field end) {
        checkInProcess(true);
        
        return moves.get().anyMatch((m) -> m.from() == start && m.to() == end);
    }
    
    @Override public final Player move(final Field start, final Field end) {
        checkInProcess(true);
        checkPhase(Phase.MOVE);
        
        final Move move = moves.get().filter((m) -> m.from() == start && m.to() == end).findAny()
                .orElseThrow(NoSuchElementException::new);
        
        move0(move);
        
        
        if (isWonBy(player())) {
            abort();
        } else {
            if (rules().noneMatch((rule) -> rule.moveAgain(this, move))) {
                player = player.next();
            }
            phase = Phase.ROLL;
            rollCount = 0;
            moves = NO_MOVES;
        }
        
        return player;
    }
    
    /**
     * Moves the provided move.
     * 
     * @param move the move
     */
    protected abstract void move0(final Move move);
    
    private boolean isWonBy(final Player player) {
        return Field.streamGoal(player).map(this::owner0).allMatch(player::equals);
    }
    
    @Override public final Stream<Field> positions(final Player player) {
        checkInProcess(true);
        
        return positions0(player).flatMap((f) -> IntStream.range(0, count0(f)).mapToObj((i) -> f));
    }
    
    /**
     * Returns the fields the provided player has his tokens placed onto. The returned stream contains no duplicate
     * fields.
     * 
     * @param  player the player
     * @return the fields
     */
    protected final Stream<Field> positions0(final Player player) {
        return Field.stream().filter((field) -> owner0(field) == player);
    }
    
    @Override public final Player owner(final Field field) {
        checkInProcess(true);
        
        return owner0(field);
    }
    
    /**
     * Returns the owner of the field.
     * 
     * @param  field the field
     * @return the owner
     */
    protected abstract Player owner0(final Field field);
    
    @Override public final int count(final Field field) {
        checkInProcess(true);
        
        return count0(field);
    }
    
    /**
     * Returns the count of tokens on the specific field.
     * 
     * @param  field the field
     * @return the count of tokens
     */
    protected abstract int count0(final Field field);
}
