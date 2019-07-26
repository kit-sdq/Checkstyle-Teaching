package edu.kit.informatik.entity.board;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.Player;
import edu.kit.informatik.exception.SemanticsException;

public class DestinationField extends BasicField {
    private final Player mPlayer;

    public DestinationField(int pAbsoluteIndex, int pMaxPiecesPerField, Player pPlayer) {
        super(pAbsoluteIndex, 1);
        this.mPlayer = pPlayer;
    }
    
    public Player getOwner() {
        return this.mPlayer;
    }
    
    @Override
    protected SemanticsException getPlacementException(GamePiece pGamePiece) {
        /*
         * 1.) There must be no other game piece on this field
         * 2.) The game piece must belong to the player which owns this field
         * 3.) Apply all rules of basic fields
         */
        if(hasGamePiece()) {
            return new SemanticsException("Error, a destination field can only hold one game piece at a time.");
        } else if(pGamePiece.getOwner() != getOwner()) {
            return new SemanticsException("Error, the owner of the destination field is: " + getOwner() + " - you cannot put a game piece of " + pGamePiece.getOwner() + " there.");
        }
        
        return super.getPlacementException(pGamePiece);
    }
    
    // TODO destination fields have other behaviour!! consider composition instead of inheritance...
    @Override
    public int getRelativePosition(Player pOtherPlayerBasePosition) {
        return getAbsolutePosition();
    }
    
    @Override
    public String toString() {
        return indexToName();
    }

    @Override
    public String toStringX() {
        if(hasGamePiece()) {
            return super.toStringX();
        } else {
            return indexToName();
        }
    }

    private String indexToName() {
        return Character.toString((char) (this.getAbsolutePosition() - Board.SIZE + 'A')) + this.mPlayer.getId();
    }
}
