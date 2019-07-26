package edu.kit.informatik.ludo.fields;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import edu.kit.informatik._intern.util.ObjectUtil;

/**
 * A move on a board.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/07/14
 */
public final class Move {
    
    private static final Field[] EMPTY = new Field[0];
    private final Field[] fields;
    
    /**
     * Creates a new move with the provided parameters.
     * 
     * @param fields the fields
     */
    Move(final Collection<? extends Field> fields) {
        if (fields.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.fields = ObjectUtil.requireNonNull(fields.toArray(EMPTY));
    }
    
    /**
     * Returns the starting field.
     * 
     * @return the starting field
     */
    public Field from() {
        return fields[0];
    }
    
    /**
     * Returns the target field.
     * 
     * @return the target field
     */
    public Field to() {
        return fields[distance()];
    }
    
    /**
     * Returns the distance.
     * 
     * @return the distance
     */
    public int distance() {
        return fields.length - 1;
    }
    
    /**
     * Returns a stream containing the fields of the move, in order.
     * 
     * @return a stream containing the fields of the move
     */
    public Stream<Field> fields() {
        return Arrays.stream(fields);
    }
    
    @Override public boolean equals(final Object obj) {
        return obj instanceof Move && Arrays.equals(fields, ((Move) obj).fields);
    }
    
    @Override public int hashCode() {
        return Arrays.hashCode(fields);
    }
    
    @Override public String toString() {
        return from() + "->" + to() + "(" + distance() + ")";
    }
}
