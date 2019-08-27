package edu.kit.informatik.entity.movement;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.board.BasicField;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.board.FieldIdentifier;
import edu.kit.informatik.entity.strategy.ITokenizer;

/* please note that only source and destination index are taken into account when computing equals and hashCode
 * we do this because a user can only move one game piece at once which means that the identity of a movement
 * is not affected by the game piece identity for two game pieces at the same source index
 */
// please note that all field indices are relative to the owner, that is why we need to pass the board reference to make them absolute
public class Movement {
    // relative indices!!
    private final int mSourceIndex;
    private final int mDestinationIndex;
    private final GamePiece mGamePiece;
    private final boolean mIsForward;
    private final int mDistance;
    private final ITokenizer mMovementStrategy;
    
    public Movement(int pSourceIndex, int pDestinationIndex, GamePiece pGamePiece, int pDistance, ITokenizer pMovementStrategy) {
        this(pSourceIndex, pDestinationIndex, pGamePiece, pDistance, true, pMovementStrategy);
    }
    
    public Movement(int pSourceIndex, int pDestinationIndex, GamePiece pGamePiece, int pDistance, boolean pIsForward, ITokenizer pMovementStrategy) {
        this.mSourceIndex = pSourceIndex;
        this.mDestinationIndex = pDestinationIndex;
        this.mGamePiece = pGamePiece;
        this.mIsForward = pIsForward;
        this.mDistance = pDistance;
        this.mMovementStrategy = pMovementStrategy;
    }
    
    public ITokenizer getMovementStrategy() {
        return this.mMovementStrategy;
    }

    public int getSourceIndex() {
        return mSourceIndex;
    }

    public int getDestinationIndex() {
        return mDestinationIndex;
    }
    
    public GamePiece getGamePiece() {
        return this.mGamePiece;
    }
    
    public Player getOwner() {
        return this.mGamePiece.getOwner();
    }
    
    public int getDistance() {
        return this.mDistance;
    }
    
    public boolean canBeat(Board pBoard) {
        return getDestinationFieldRelative(pBoard).canBeat(getGamePiece());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mDestinationIndex;
        result = prime * result + mSourceIndex;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Movement other = (Movement) obj;
        if (mDestinationIndex != other.mDestinationIndex)
            return false;
        if (mSourceIndex != other.mSourceIndex)
            return false;
        return true;
    }
    
    public String toString(Board pBoard) {
        BasicField sField = getSourceField(pBoard);
        BasicField dField = getDestinationField(pBoard);
        
        return sField + "-" + dField;
    }
    
    // caution!! these are the relative, absolute indices!! i.e. no conversion to FieldIdentifier!!! use toString(Board) instead!!
    @Override
    @Deprecated
    public String toString() {
        return getSourceIndex() + "-" + getDestinationIndex();
    }

    public boolean isForward() {
        return this.mIsForward;
    }
    
    // convenience method
    public BasicField getDestinationField(Board pBoard) {
        return pBoard.getField(getOwner(), Board.getAbsoluteFromRelativePosition(getOwner(), getDestinationIndex()));
    }
    
    // convenience method
    public BasicField getSourceField(Board pBoard) {
        return pBoard.getField(getOwner(), Board.getAbsoluteFromRelativePosition(getOwner(), getSourceIndex()));
    }

    // convenience method
    public BasicField getDestinationFieldRelative(Board pBoard) {
        return pBoard.getFieldRelative(getOwner(), getDestinationIndex());
    }
    
    // convenience method
    public BasicField getSourceFieldRelative(Board pBoard) {
        return pBoard.getFieldRelative(getOwner(), getSourceIndex());
    }
    
    // convenience method
    public boolean isStartMove() {
        return getSourceIndex() == FieldIdentifier.START_POSITION_INDEX;
    }
}
