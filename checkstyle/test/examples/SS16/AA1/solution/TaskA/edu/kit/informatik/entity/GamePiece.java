package edu.kit.informatik.entity;

import edu.kit.informatik.entity.board.BasicField;
import edu.kit.informatik.entity.board.FieldIdentifier;

public class GamePiece implements IPositionAware {
    private final Player mOwner;
    private BasicField mCurrentField;    // null = start position
    
    public GamePiece(Player pOwner, BasicField pField) {
        this.mOwner = pOwner;
        this.mCurrentField = pField;
    }

    public BasicField getCurrentField() {
        return this.mCurrentField;
    }

    public Player getOwner() {
        return mOwner;
    }

    // returns FieldIdentifier.START_POSITION_INDEX if not on board
    public int getRelativePosition() {
        return (getCurrentField() == null) ? FieldIdentifier.START_POSITION_INDEX : getCurrentField().getRelativePosition(getOwner());
    }

    // returns FieldIdentifier.START_POSITION_INDEX if not on board
    @Override
    public int getAbsolutePosition() {
        return (getCurrentField() == null) ? FieldIdentifier.START_POSITION_INDEX : getCurrentField().getAbsolutePosition();
    }

    public boolean isOnBoard() {
        return !isOnStartField();
    }

    public boolean isOnStartField() {
        return getCurrentField() == null || getCurrentField().isStartField();
    }

    public void setCurrentField(BasicField pField) {
        this.mCurrentField = pField;
    }
}
