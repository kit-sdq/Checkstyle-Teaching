package edu.kit.informatik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Enum of all possible shapes in the RPSSL game, namely rock, paper, scissors, spock and lizard.
 *
 * @author Peter
 * @version 1.0
 */
public enum Shape {
    /**
     * The rock shape.
     */
    ROCK(0),
    /**
     * The paper shape.
     */
    PAPER(2),
    /**
     * The scissors shape.
     */
    SCISSORS(4),
    /**
     * The spock shape.
     */
    SPOCK(1),
    /**
     * The lizard shape.
     */
    LIZARD(3);
    
    private int numberRepresentation;
    
    // Enum constructors are private by default, so no modifier here.

    /**
     * Creates a shape with a specific number representation that is needed for the winner calculation.
     *
     * @param numberRepresentation The specific number representation of the shape.
     */
    Shape(int numberRepresentation) {
        this.numberRepresentation = numberRepresentation;
    }

    /**
     * This calculates the winner of two shapes based on the cyclic behaviour of the RPSSL rules.
     * Credit for this idea goes to 
     * <a target="_blank" href="https://github.com/IkerGarcia/RPSSL/blob/master/RPSSL.py">IkerGarcia</a>.
     * 
     * @param other The other shape to compare against.
     * @return The result of the duel as instance of the {@link Result} enum.
     *         WIN if this shape wins, LOSE if the other shape wins or DRAW if noone wins.
     */
    public Result checkForWinner(Shape other) {
        int result = this.numberRepresentation - other.numberRepresentation;
        if (result == 0) {
            return Result.DRAW;
        } else if (Math.floorMod(result, Shape.values().length) < 3) {
            return Result.WIN;
        } else {
            return Result.LOSE;
        }
    }

    /**
     * Converts an array of strings that represent a shape into a list of actual shapes.
     * 
     * @param shapesArray The array of string representations.
     * @return A list of shapes.
     * @throws ShapeConversionException If a string representation does not match any shape.
     */
    public static List<Shape> convertStringsToShapes(String... shapesArray) throws ShapeConversionException {
        List<Shape> shapes = new ArrayList<>();
        for (String shape : shapesArray) {
            try {
                shapes.add(Shape.valueOf(shape.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ShapeConversionException("shape '" + shape + "' does not exist.");
            }
        }
        
        return shapes;
    }

    /**
     * Creates a capturing group regex of all shapes.
     *
     * @return the capturing group regex of all shapes.
     */
    public static String getCapturingRegex() {
        return "("
                + Arrays.stream(values())
                .map(Shape::toString)
                .collect(Collectors.joining("|"))
                + ")";
    }
}
