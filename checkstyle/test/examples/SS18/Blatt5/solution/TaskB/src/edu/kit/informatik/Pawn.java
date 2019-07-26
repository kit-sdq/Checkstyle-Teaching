package edu.kit.informatik;

import edu.kit.informatik.exceptions.IllegalMoveException;

import java.util.Objects;

import static java.lang.Math.abs;
import static java.lang.String.format;

/**
 * A pawn in a UI of santorini.
 * 
 * @see SantoriniGame
 * 
 * @author Peter Oettig
 * @version 1.0
 */
public class Pawn {

    private final String name;
    private Player owner;
    private Cell visitedCell;

    /**
     * Creates a pawn named {@code name}.
     * 
     * @param name
     *            The new pawn's name.
     */
    public Pawn(String name) {
        this.name = name;
    }

    /**
     * Internal Method: Registers {@code player} as the owner of this pawn.
     * 
     * @param owner
     *            This pawn's owner.
     */
    void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Internal Method: Registers {@code cell} as the cell this pawn is visiting in the moment, without running any
     * checks. Meant to be used after the pawn was created.
     * 
     * @param cell
     *            The cell this pawn is currently visiting.
     */
    void startOnCell(Cell cell) {
        visitedCell = cell;
    }

    /**
     * @return This pawn's name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return This pawn's owner.
     */
    Player getOwner() {
        return owner;
    }

    /**
     * Internal Method: Moves this pawn to {@code cellToVisit}. Performs checks whether the pawn is allowed to enter the
     * cell, but not whether the pawn is allowed to move at all.
     * 
     * @param cellToVisit
     *            The cell this pawn should visit.
     * @throws IllegalMoveException
     *             If the move to {@code cellToVisit} is not valid. This may for example be the case if:
     *             <ul>
     *             <li> {@code cellToVisit}'s tower is to high
     *             <li> {@code cellToVisit} is not a neighbour cell of the cell this pawn is currently visiting.
     *             <li> {@code cellToVisit.enterBy(this)} throws a {@code InvalidMoveException}
     *             </ul>
     */
    void moveTo(Cell cellToVisit) throws IllegalMoveException {
        assertNeighbor(cellToVisit);
        if (cellToVisit.getTowerHeight() > (visitedCell.getTowerHeight() + 1)) {
            throw new IllegalMoveException(format("%s is too high to move to it. It is %d pieces high, "
                    + "%s momentary visited by '%s' is only %d pieces high", cellToVisit.getDescription(),
                cellToVisit.getTowerHeight(), visitedCell.getDescription(), this, visitedCell.getTowerHeight()));
        }
        cellToVisit.enterBy(this);
        visitedCell.leave();
        visitedCell = cellToVisit;
    }

    /**
     * Internal Method: lets this pawn build a dome on {@code cellToBuildOn}. Checks whether this pawn can build a dome
     * on {@code cellToBuildOn}, but no whether this pawn is allowed to build at all.
     * 
     * @param cellToBuildOn
     *            The cell this pawn shall build a dome on.
     * @throws IllegalMoveException
     *             If this pawn cannot be build a dome on {@code cellToBuildOn}. This may be the case if
     *             <ul>
     *             <li> {@code cellToBuildOn} is not a neighbour cell of the cell this pawn is currently visiting.
     *             <li>{@code cellToBuildOn.buildDome()} throws an {@code InvalidMoveException}.
     *             </ul>
     */
    void buildDome(Cell cellToBuildOn) throws IllegalMoveException {
        assertNeighbor(cellToBuildOn);
        cellToBuildOn.buildDome();
    }

    /**
     * Internal Method: lets this pawn build a cuboid on {@code cellToBuildOn}. Checks whether this pawn can build a
     * cuboid on {@code cellToBuildOn}, but no whether this pawn is allowed to build at all.
     * 
     * @param cellToBuildOn
     *            The cell this pawn shall build a cuboid on.
     * @throws IllegalMoveException
     *             If this pawn cannot be build a cuboid on {@code cellToBuildOn}. This may be the case if
     *             <ul>
     *             <li> {@code cellToBuildOn} is not a neighbour cell of the cell this pawn is currently visiting.
     *             <li>{@code cellToBuildOn.buildCuboid()} throws an {@code InvalidMoveException}.
     *             </ul>
     */
    void buildCuboid(Cell cellToBuildOn) throws IllegalMoveException {
        assertNeighbor(cellToBuildOn);
        cellToBuildOn.buildCuboid();
    }

    private void assertNeighbor(Cell cell) throws IllegalMoveException {
        if (cell.getColumn() == visitedCell.getColumn() && cell.getRow() == visitedCell.getRow()) {
            throw new IllegalMoveException(format("%s is the cell visited by '%s' itself", cell.getDescription(),
                this));
        }
        if ((abs(visitedCell.getColumn() - cell.getColumn()) > 1)
                || (abs(visitedCell.getRow() - cell.getRow()) > 1)) {
            throw new IllegalMoveException(format("%s is not a neighbour of %s momentarily " + "visited by '%s'.",
                cell.getDescription(), visitedCell.getDescription(), this));
        }
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pawn pawn = (Pawn) o;
        return Objects.equals(name, pawn.name) && Objects.equals(owner, pawn.owner);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, owner);
    }
}
