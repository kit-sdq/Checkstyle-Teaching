package edu.kit.informatik;

import edu.kit.informatik.exceptions.IllegalMoveException;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal Class: The cell of a santorini UI's board.
 * 
 * @see SantoriniGame
 * 
 * @author Peter Oettig
 * @version 1.0
 */
class Cell {
    /**
     * The maximum height of the tower of cuboids on a cell. A dome can only be build on a cell that has a tower of
     * excatly this height.
     */
    static final int MAX_TOWER_HEIGHT = 3;
    private Pawn visitor = null;
    private int tower = 0;
    private boolean dome = false;
    private final int row;
    private final int column;

    /**
     * Creates a cell that's located at {@code row:column} in the UI of santorini {@code UI}.
     * 
     * @param row
     *            The new cell's row number.
     * @param column
     *            The new cell's column number.
     */
    Cell(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * @return The number of the row this cell is placed in.
     */
    int getRow() {
        return row;
    }

    /**
     * @return The number of the column this cell is placed in.
     */
    int getColumn() {
        return column;
    }

    /**
     * Sets this cell's visitor to {@code visitor}. Does not perform any checks.Only meant to be used if {@code visitor}
     * newly enters the cell. Use {@link #enterBy(Pawn)} for regular moves.
     * 
     * @param visitor
     *            A pawn to be placed on this cell.
     */
    void setVisitor(Pawn visitor) {
        this.visitor = visitor;
    }

    /**
     * Lets {@code pawn} enter this cell. Checks whether this cell can be entered.
     * 
     * @param pawn
     *            The pawn trying to enter this cell.
     * @throws IllegalMoveException
     *             If this cell cannot be entered. This may for example be the case if
     *             <ul>
     *             <li>this cell is already visited.
     *             <li>this cell has a dome.
     *             </ul>
     */
    void enterBy(Pawn pawn) throws IllegalMoveException {
        if (hasDome()) {
            throw new IllegalMoveException(format("%s cannot be moved to, as it has a dome", getDescription()));
        }
        if (isVisited()) {
            throw new IllegalMoveException(format("%s is already visited by '%s'", getDescription(), getVisitor()));
        }
        visitor = pawn;
    }

    /**
     * Registers that this cell's momentary visitor has left the cell.
     */
    void leave() {
        visitor = null;
    }

    /**
     * Builds a cuboid on this cell. Checks whether a cuboid can be built on this cell.
     * 
     * @throws IllegalMoveException
     *             If no cuboid can be build on this cell. This if for example the case if
     *             <ul>
     *             <li>this cell is visited.
     *             <li>this cell has a dome.
     *             <li>this cell's tower height is already {@link #MAX_TOWER_HEIGHT}.
     *             </ul>
     */
    void buildCuboid() throws IllegalMoveException {
        assertLegalBuild();
        if (tower >= MAX_TOWER_HEIGHT) {
            throw new IllegalMoveException(format("%s has already reached the maximum tower height, %d",
                getDescription(), MAX_TOWER_HEIGHT));
        }
        tower++;
    }

    /**
     * Builds a dome on this cell. Checks whether a cuboid can be built on this cell.
     * 
     * @throws IllegalMoveException
     *             If no cuboid can be build on this cell. This if for example the case if
     *             <ul>
     *             <li>this cell is visited.
     *             <li>this cell already has a dome.
     *             <li>this cell's tower height is not {@link #MAX_TOWER_HEIGHT}.
     *             </ul>
     */
    void buildDome() throws IllegalMoveException {
        assertLegalBuild();
        if (tower < MAX_TOWER_HEIGHT) {
            throw new IllegalMoveException(format("%s has a tower height of only %d, "
                    + "a height of %d is required to build a dome", getDescription(), tower, MAX_TOWER_HEIGHT));
        }
        dome = true;
    }

    /**
     * @return {@code true} only if this cell is visited by a pawn in the moment.
     */
    boolean isVisited() {
        return visitor != null;
    }

    /**
     * @return This cell's tower's height.
     */
    int getTowerHeight() {
        return tower;
    }

    /**
     * @return If {@link #isVisited()} returns {@code true} the pawn visiting this cell in the moment. {@code null}
     *         otherwise.
     */
    Pawn getVisitor() {
        return visitor;
    }

    /**
     * @return A String in the form of {@code the cell(row:column)} where {@code row} and {@code column} are filled in
     *         with the corresponding values of {@link #getRow()} and {@link #getColumn()}.
     */
    String getDescription() {
        return format("the cell (%d:%d)", getRow(), getColumn());
    }

    private void assertLegalBuild() throws IllegalMoveException {
        if (isVisited()) {
            throw new IllegalMoveException(format("%s is visited by %s and can hence not be built on",
                getDescription(), getVisitor()));
        }
        if (hasDome()) {
            throw new IllegalMoveException(format("%s already has a dome and can hence not be built on",
                getDescription()));
        }
    }

    /**
     * @return {@code true} only if a dome is build on this cell.
     */
    private boolean hasDome() {
        return dome;
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder();
        if ((visitor == null) && (tower == 0)) {
            return "Empty";
        }
        List<Object> toPrint = new ArrayList<>();
        for (int c = 0; c < tower; c++) {
            toPrint.add("C");
        }
        if (visitor != null) {
            toPrint.add(visitor);
        }
        if (dome) {
            toPrint.add("D");
        }
        for (Object o : toPrint) {
            if (o != null) {
                if (resultBuilder.length() > 0) {
                    resultBuilder.append(",");
                }
                resultBuilder.append(o);
            }
        }
        return resultBuilder.toString();
    }
}
