package edu.kit.informatik.ludo.fields;

import edu.kit.informatik._intern.util.ObjectUtil;

/**
 * Field-content used to store the owner of a field.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public final class FieldContent {
    
    private static final int MIN_COUNT = 1;
    private static final int MAX_COUNT = 4;
    
    private static final FieldContent[] VALUES = new FieldContent[1 + Player.COUNT * (1 + MAX_COUNT - MIN_COUNT)];
    static {
        VALUES[0] = new FieldContent(null, 0);
        for (int i = 0; i < Player.COUNT; i++) {
            final Player player = Player.byIndex(i);
            for (int n = MIN_COUNT; n <= MAX_COUNT; n++) {
                VALUES[index(i, n)] = new FieldContent(player, n);
            }
        }
    }
    
    /** The player that owns the field (= the player that has at least one token placed onto it). */
    public final Player owner;
    /** The count of tokens on the field. */
    public final int count;
    
    private FieldContent(final Player owner, final int count) {
        this.owner = owner;
        this.count = count;
    }
    
    /**
     * Returns an empty field-content.
     * 
     * @return an empty field-content
     */
    public static FieldContent empty() {
        return VALUES[0];
    }
    
    /**
     * Returns a field-content for the provided player with a count of {@value #MIN_COUNT}.
     * 
     * @param  player the player
     * @return the field-content
     */
    public static FieldContent of(final Player player) {
        return of(player.ordinal(), MIN_COUNT);
    }
    
    /**
     * Returns a field-content for the provided player with the given count.
     * 
     * @param  player the player
     * @param  count the count
     * @return the field-content
     * @throws IllegalArgumentException if count is less than 1 or greater than 4
     */
    public static FieldContent of(final Player player, final int count) {
        ObjectUtil.requireNonNull(player);
        if (count < MIN_COUNT || count > MAX_COUNT) {
            throw new IllegalArgumentException();
        }
        return of(player.ordinal(), count);
    }
    
    private static int index(final int o, final int c) {
        return c < MIN_COUNT ? 0 : o * (1 + MAX_COUNT - MIN_COUNT) + (1 + c - MIN_COUNT);
    }
    
    private static FieldContent of(final int player, final int count) {
        return VALUES[index(player, count)];
    }
    
    /**
     * Returns a field-content with a token of the provided player added.
     * 
     * @param  player the player
     * @return the new field-content
     * @throws IllegalStateException if the new count is greater than 4 or if the owner of the field is neither {@code
     *         null} nor {@code player}
     */
    public FieldContent add(final Player player) {
        return add(player, 1);
    }
    
    /**
     * Returns a field-content with {@code add} tokens of the provided player added.
     * 
     * @param  player the player
     * @param  add the count to add
     * @return the new field-content
     * @throws IllegalStateException if the new count is greater than {@value #MAX_COUNT} or if the owner of the field
     *         is neither {@code null} nor {@code player}
     */
    public FieldContent add(final Player player, final int add) {
        if (count >= MIN_COUNT && (count + add > MAX_COUNT || player != owner)) {
            throw new IllegalStateException();
        }
        return of(player.ordinal(), count + add);
    }
    
    /**
     * Returns a field-content with a token removed.
     * 
     * @return the new field-content
     * @throws IllegalStateException if the count of the field is 0
     */
    public FieldContent remove() {
        if (owner == null) {
            throw new IllegalStateException();
        }
        return of(owner.ordinal(), count - 1);
    }
    
    /**
     * Returns whether the field-content is owned by a player other than the provided one.
     * 
     * @param  player the player to check for
     * @return {@code true} if the field-content is owned by a player so that {@code owner!=player}
     */
    public boolean isOwnedByOther(final Player player) {
        return owner != null && owner != player;
    }
    
    @Override public String toString() {
        return "Field[" + owner + ":" + count + "]";
    }
}