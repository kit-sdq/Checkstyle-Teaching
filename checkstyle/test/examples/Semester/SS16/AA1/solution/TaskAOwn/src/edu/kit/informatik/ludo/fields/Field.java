package edu.kit.informatik.ludo.fields;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Should be replaced with a normal class in case that the count of players may differ to allow creating dynamic tracks
 * for the playing players.
 */
/**
 * Field for a board.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public enum Field {
    /** */
    /** Base of the first player.  */   B_00("SR",  0, -1, -1, -1, -1, -1, -1, -1),
    /** Base of the second player. */   B_10("SB", -1, 10, -1, -1, -1, -1, -1, -1),
    /** Base of the third player.  */   B_20("SG", -1, -1, 20, -1, -1, -1, -1, -1),
    /** Base of the fourth player. */   B_30("SY", -1, -1, -1, 30, -1, -1, -1, -1),
    
    /** Field 00. */    F_00( "0",  1,  1,  1,  1, -1, 39, 39, 39),
    /** Field 01. */    F_01( "1",  2,  2,  2,  2, -1,  0,  0,  0),
    /** Field 02. */    F_02( "2",  3,  3,  3,  3,  1,  1,  1,  1),
    /** Field 03. */    F_03( "3",  4,  4,  4,  4,  2,  2,  2,  2),
    /** Field 04. */    F_04( "4",  5,  5,  5,  5,  3,  3,  3,  3),
    /** Field 05. */    F_05( "5",  6,  6,  6,  6,  4,  4,  4,  4),
    /** Field 06. */    F_06( "6",  7,  7,  7,  7,  5,  5,  5,  5),
    /** Field 07. */    F_07( "7",  8,  8,  8,  8,  6,  6,  6,  6),
    /** Field 08. */    F_08( "8",  9,  9,  9,  9,  7,  7,  7,  7),
    /** Field 09. */    F_09( "9", 10, 44, 10, 10,  8,  8,  8,  8),
    /** Field 10. */    F_10("10", 11, 11, 11, 11,  9, -1,  9,  9),
    /** Field 11. */    F_11("11", 12, 12, 12, 12, 10, -1, 10, 10),
    /** Field 12. */    F_12("12", 13, 13, 13, 13, 11, 11, 11, 11),
    /** Field 13. */    F_13("13", 14, 14, 14, 14, 12, 12, 12, 12),
    /** Field 14. */    F_14("14", 15, 15, 15, 15, 13, 13, 13, 13),
    /** Field 15. */    F_15("15", 16, 16, 16, 16, 14, 14, 14, 14),
    /** Field 16. */    F_16("16", 17, 17, 17, 17, 15, 15, 15, 15),
    /** Field 17. */    F_17("17", 18, 18, 18, 18, 16, 16, 16, 16),
    /** Field 18. */    F_18("18", 19, 19, 19, 19, 17, 17, 17, 17),
    /** Field 19. */    F_19("19", 20, 20, 48, 20, 18, 18, 18, 18),
    /** Field 20. */    F_20("20", 21, 21, 21, 21, 19, 19, -1, 19),
    /** Field 21. */    F_21("21", 22, 22, 22, 22, 20, 20, -1, 20),
    /** Field 22. */    F_22("22", 23, 23, 23, 23, 21, 21, 21, 21),
    /** Field 23. */    F_23("23", 24, 24, 24, 24, 22, 22, 22, 22),
    /** Field 24. */    F_24("24", 25, 25, 25, 25, 23, 23, 23, 23),
    /** Field 25. */    F_25("25", 26, 26, 26, 26, 24, 24, 24, 24),
    /** Field 26. */    F_26("26", 27, 27, 27, 27, 25, 25, 25, 25),
    /** Field 27. */    F_27("27", 28, 28, 28, 28, 26, 26, 26, 26),
    /** Field 28. */    F_28("28", 29, 29, 29, 29, 27, 27, 27, 27),
    /** Field 29. */    F_29("29", 30, 30, 30, 52, 28, 28, 28, 28),
    /** Field 30. */    F_30("30", 31, 31, 31, 31, 29, 29, 29, -1),
    /** Field 31. */    F_31("31", 32, 32, 32, 32, 30, 30, 30, -1),
    /** Field 32. */    F_32("32", 33, 33, 33, 33, 31, 31, 31, 31),
    /** Field 33. */    F_33("33", 34, 34, 34, 34, 32, 32, 32, 32),
    /** Field 34. */    F_34("34", 35, 35, 35, 35, 33, 33, 33, 33),
    /** Field 35. */    F_35("35", 36, 36, 36, 36, 34, 34, 34, 34),
    /** Field 36. */    F_36("36", 37, 37, 37, 37, 35, 35, 35, 35),
    /** Field 37. */    F_37("37", 38, 38, 38, 38, 36, 36, 36, 36),
    /** Field 38. */    F_38("38", 39, 39, 39, 39, 37, 37, 37, 37),
    /** Field 39. */    F_39("39", 40,  0,  0,  0, 38, 38, 38, 38),
    
    /** First goal field of the first player.   */  G_00("AR", 41, -1, -1, -1, -1, -1, -1, -1),
    /** Second goal field of the first player.  */  G_01("BR", 42, -1, -1, -1, -1, -1, -1, -1),
    /** Third goal field of the first player.   */  G_02("CR", 43, -1, -1, -1, -1, -1, -1, -1),
    /** Fourth goal field of the first player.  */  G_03("DR", -1, -1, -1, -1, -1, -1, -1, -1),
    /** First goal field of the second player.  */  G_10("AB", -1, 45, -1, -1, -1, -1, -1, -1),
    /** Second goal field of the second player. */  G_11("BB", -1, 46, -1, -1, -1, -1, -1, -1),
    /** Third goal field of the second player.  */  G_12("CB", -1, 47, -1, -1, -1, -1, -1, -1),
    /** Fourth goal field of the second player. */  G_13("DB", -1, -1, -1, -1, -1, -1, -1, -1),
    /** First goal field of the third player.   */  G_20("AG", -1, -1, 49, -1, -1, -1, -1, -1),
    /** Second goal field of the third player.  */  G_21("BG", -1, -1, 50, -1, -1, -1, -1, -1),
    /** Third goal field of the third player.   */  G_22("CG", -1, -1, 51, -1, -1, -1, -1, -1),
    /** Fourth goal field of the third player.  */  G_23("DG", -1, -1, -1, -1, -1, -1, -1, -1),
    /** First goal field of the fourth player.  */  G_30("AY", -1, -1, -1, 53, -1, -1, -1, -1),
    /** Second goal field of the fourth player. */  G_31("BY", -1, -1, -1, 54, -1, -1, -1, -1),
    /** Third goal field of the fourth player.  */  G_32("CY", -1, -1, -1, 55, -1, -1, -1, -1),
    /** Fourth goal field of the fourth player. */  G_33("DY", -1, -1, -1, -1, -1, -1, -1, -1);
    
    private static final Field[] VALUES = values();
    private static final Map<String, Field> BY_NAME = Arrays.stream(VALUES)
            .collect(Collectors.toMap((f) -> f.name, Function.identity()));
    private static final Field[] MAP_VIEW = {
            null, null, null, null, F_08, F_09, F_10, null, null, null, null,
            null, B_00, null, null, F_07, G_10, F_11, null, null, B_10, null,
            null, null, null, null, F_06, G_11, F_12, null, null, null, null,
            null, null, null, null, F_05, G_12, F_13, null, null, null, null,
            F_00, F_01, F_02, F_03, F_04, G_13, F_14, F_15, F_16, F_17, F_18,
            F_39, G_00, G_01, G_02, G_03, null, G_23, G_22, G_21, G_20, F_19,
            F_38, F_37, F_36, F_35, F_34, G_33, F_24, F_23, F_22, F_21, F_20,
            null, null, null, null, F_33, G_32, F_25, null, null, null, null,
            null, null, null, null, F_32, G_31, F_26, null, null, null, null,
            null, B_30, null, null, F_31, G_30, F_27, null, null, B_20, null,
            null, null, null, null, F_30, F_29, F_28, null, null, null, null};
    
    private final String name;
    private final int next;
    private final int prev;
    
    /**
     * Creates a field with the provided parameters.
     * 
     * @param strRep the name of the field
     * @param v an array containing the next and previous field for each player
     */
    Field(final String strRep, final int... v /* Restricted to max. 7 parameters thus using var-args. */) {
        name = strRep;
        next = IntStream.range(0, 4).filter((i) -> v[i] >= 0).map((i) -> 5 + v[i] << 8 * i).sum();
        prev = IntStream.range(4, 8).filter((i) -> v[i] >= 0).map((i) -> 5 + v[i] << 8 * i).sum();
    }
    
    /**
     * Returns the base field of the provided player.
     * 
     * @param  player the player
     * @return the base field
     */
    public static Field base(final Player player) {
        return byIndex(player.ordinal());
    }
    
    /**
     * Returns the field with the provided index.
     * 
     * <p>The index is equal to the value returned by the {@link #ordinal()} method.
     * 
     * @param  index the index
     * @return the field with the provided index
     */
    public static Field byIndex(final int index) {
        return VALUES[index];
    }
    
    /**
     * Returns a sorted stream containing all fields.
     * 
     * @return a stream containing all fields
     */
    public static Stream<Field> stream() {
        return Arrays.stream(VALUES);
    }
    
    /**
     * Returns a stream containing all goal fields of the specific player.
     * 
     * <p>The returned stream is equal to the result of
     * <blockquote><pre>
     * stream().filter(Field::isGoal).filter((goal) -> goal.isUsableBy(player));</pre>
     * </blockquote>
     * 
     * @param  player the player
     * @return a stream containing all goal fields of {@code player}
     */
    public static Stream<Field> streamGoal(final Player player) {
        final int offset = 4 + 40 + 4 * player.ordinal();
        return Arrays.stream(VALUES, offset, offset + 4);
    }
    
    /**
     * Returns the field with the provided name.
     * 
     * <p>The name of a field is equal to the string returned by the {@link #toString()} method.
     * 
     * @param  name the name
     * @return the field
     * @throws IllegalArgumentException if no field with the provided name exists
     */
    public static Field of(final String name) {
        final Field field = BY_NAME.get(name);
        if (field == null) {
            throw new IllegalArgumentException("invalid name " + name);
        }
        return field;
    }
    
    /**
     * Returns a string-representation for the given function.
     * 
     * @param  function the function
     * @return the string representation
     */
    public static String toString(final Function<? super Field, ? extends FieldContent> function) {
        final StringJoiner sj = new StringJoiner(System.lineSeparator());
        final int size = 11;
        final int ctr = size * 5 - 4;
        final String header = "+ " + String.join(" ", Collections.nCopies(ctr, "+")) + " +";
        final String empty  = "+ " + String.join(" ", Collections.nCopies(ctr, " ")) + " +";
        sj.add(header);
        sj.add(empty);
        for (int i = 0, hs = size >> 1; i < size; i++) {
            final StringJoiner a = new StringJoiner("  ", "+   ", "   +");
            final StringJoiner b = new StringJoiner("  ", "+   ", "   +");
            final StringJoiner c = new StringJoiner("  ", "+   ", "   +");
            for (int n = 0; n < size; n++) {
                final Field f = MAP_VIEW[i * size + n];
                if (f == null) {
                    a.add("       ");
                    b.add("       ");
                    c.add("       ");
                } else {
                    final FieldContent fc = function.apply(f);
                    final char cornerU = f.isGoal() ? n == hs ? '|' : (char) 8254 /* Praktomat struggling... */ : ' ';
                    final char cornerL = f.isGoal() ? n == hs ? '|' : '_' : ' ';
                    a.add(cornerU + "/‾‾‾\\" + cornerU);
                    b.add("|  " + (fc.owner != null ? fc.owner.toString().charAt(0) : ' ') + "  |");
                    c.add(cornerL + "\\___" + (fc.count > 1 ? (char) (fc.count + '0') : '/') + cornerL);
                }
            }
            sj.add(a.toString());
            sj.add(b.toString());
            sj.add(c.toString());
            sj.add(empty);
        }
        sj.add(header);
        return sj.toString();
    }
    
    /**
     * Returns whether the field is a base field.
     * 
     * <p>The fields {@link #B_00}, {@link #B_10}, {@link #B_20} and {@link #B_30} are base fields.
     * 
     * @return {@code true} if the field is a base field, {@code false} otherwise
     */
    public boolean isBase() {
        return ordinal() < 4;
    }
    
    /**
     * Returns whether the field is a goal field.
     * 
     * This method behaves exactly as if the following code got executed
     * <blockquote><pre>
     * return isGoal(Player.stream());</pre>
     * </blockquote>
     * 
     * @return {@code true} if the field is a goal field, {@code false} otherwise
     */
    public boolean isGoal() {
        return ordinal() >= 4 + 40;
    }
    
    /**
     * Returns whether the field is a normal field.
     * 
     * <p>A normal field is neither {@link #isBase()}, {@link #isStart()} or {@link #isGoal()}.
     * 
     * @return {@code true} if the field is a normal field, {@code false} otherwise
     */
    public boolean isNormal() {
        return !isBase() && !isGoal() && (ordinal() - 4) % 10 != 0;
    }
    
    /**
     * Returns whether the field is a start field.
     * 
     * <p>The fields {@link #F_00}, {@link #F_10}, {@link #F_20} and {@link #F_30} are start fields.
     * 
     * @return {@code true} if the field is a start field, {@code false} otherwise
     */
    public boolean isStart() {
        return !isBase() && !isGoal() && (ordinal() - 4) % 10 == 0;
    }
    
    /**
     * Returns whether the provided player is allowed to place on the field.
     * 
     * @param  player the player
     * @return {@code true} if the player is allowed to place on the field, {@code false} otherwise
     */
    public boolean isUsableBy(final Player player) {
        return isBase() ? player.ordinal() == ordinal()
             : isGoal() ? player.ordinal() == (ordinal() - 4 - 40) / 4
             : true;
    }
    
    /**
     * Returns a move for the provided player with the given amount of steps.
     * 
     * @param  player the player
     * @param  dist the distance
     * @return the move, or {@code null} if no move with the given amount of steps is possible
     */
    public Move move(final Player player, final int dist) {
        if (dist != 0) {
            final int count = Math.abs(dist);
            final int o = player.ordinal();
            final ToIntFunction<Field> f = dist > 0 ? (field) -> field.next : (field) -> field.prev;
            final Collection<Field> c = new ArrayList<>(count);
            for (int n = ordinal();; c.add(byIndex(n)), n = ((f.applyAsInt(byIndex(n)) >> 8 * o) & (1 << 8) - 1) - 1) {
                if (c.size() > count) return new Move(c);
                if (n < 0) break;
            }
        }
        return null;
    }
    
    @Override public String toString() {
        return name;
    }
}
