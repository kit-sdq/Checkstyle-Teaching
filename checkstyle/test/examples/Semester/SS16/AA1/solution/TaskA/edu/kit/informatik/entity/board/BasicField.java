package edu.kit.informatik.entity.board;

import java.util.HashSet;
import java.util.Set;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.Player;
import edu.kit.informatik.entity.IPositionAware;
import edu.kit.informatik.exception.SemanticsException;

public class BasicField implements IPositionAware {
    private final int mAbsoluteIndex;
    private final int mMaxPiecesPerField;
    private Set<GamePiece> mGamePieces = new HashSet<>();
  
    public BasicField(int pAbsoluteIndex, int pMaxPiecesPerField) {
        this.mAbsoluteIndex = pAbsoluteIndex;
        this.mMaxPiecesPerField = pMaxPiecesPerField;
    }
    
    public static BasicField getStartField(Player pPlayer) {
        return new StartField(pPlayer);
    }

    public void putGamePiece(GamePiece pGamePiece) throws SemanticsException {
        checkPlacementValidity(pGamePiece);
        this.mGamePieces.add(pGamePiece);
    }
    
    private void checkPlacementValidity(GamePiece pGamePiece) throws SemanticsException {
        SemanticsException exception = getPlacementException(pGamePiece);
        if(exception != null) {
            throw exception;
        }
    }
    
    public boolean isPlacementValid(GamePiece pGamePiece) {
        return getPlacementException(pGamePiece) == null;
    }
    
    // must contain a game piece which does not belong to attacker
    public boolean canBeat(GamePiece pGamePiece) {
        if(this.hasGamePiece()) {
            // must not beat own pieces
            if(this.getGamePieces().iterator().next().getOwner() != pGamePiece.getOwner()) {
                return true;
            }
        }
        
        return false;
    }
    
    // TODO implement constraints
    protected SemanticsException getPlacementException(GamePiece pGamePiece) {
        if(hasGamePiece()) {
            Player otherPlayer = getGamePieces().iterator().next().getOwner();
            if(otherPlayer != pGamePiece.getOwner()) {
                return new SemanticsException("Error, cannot place an enemy (" + pGamePiece.getOwner() + ") on a field (" + toString() + ") that is owned by another player (" + otherPlayer + ")!");
            }
            
            if(getGamePieces().size() >= this.mMaxPiecesPerField) {
                return new SemanticsException("Error, a player can only place max. " + this.mMaxPiecesPerField + " pieces per field.");
            }
            
            // if this field is a player's start position, than we cannot place more than one piece here (i.e. cannot build barrier)
            if(Player.isStartPositionIndex(getAbsolutePosition()) && hasGamePiece()) {
                return new SemanticsException("Error, cannot place more than one piece on a player's start position.");
            }
        }
        
        return null;
    }

    public Set<GamePiece> getGamePieces() {
        return this.mGamePieces;
    }
    
    public boolean hasGamePiece() {
        return !this.mGamePieces.isEmpty();
    }
    
    // note: there can be only one owner, no matter how many pieces we have there
    public Player getGamePieceOwner() {
        return (hasGamePiece()) ? this.getGamePieces().iterator().next().getOwner() : null;
    }

    @Override
    public int getAbsolutePosition() {
        return this.mAbsoluteIndex;
    }

    public int getRelativePosition(Player pOtherPlayer) {
        if(pOtherPlayer.getAbsolutePosition() > getAbsolutePosition()) {
            return Board.SIZE - (pOtherPlayer.getAbsolutePosition() - getAbsolutePosition());
        } else {
            return getAbsolutePosition() - pOtherPlayer.getAbsolutePosition();
        }
    }
        
    @Override
    public String toString() {
        if(!(this instanceof StartField) && this.mAbsoluteIndex < 0) {
            throw new IllegalStateException("Error, this field is a StartField. It must be of type StartField!");
        }
        
        return String.valueOf(this.mAbsoluteIndex); 
    }
    
    public String toStringX() {
        if(hasGamePiece()) {
            String gamePiecesStr = ""; 
            int i = 0;
            for(GamePiece gps : getGamePieces()) {
                gamePiecesStr += ((i > 0) ? " " : "") + gps.getOwner().getId();
                i++;
            }
            
            return "_" + gamePiecesStr + "_";
        } else {
            return String.valueOf(this.mAbsoluteIndex);           
        }
    }
    
    public boolean isStartField() {
        return this instanceof StartField;
    }
    
    private static class StartField extends BasicField {
        private final Player mPlayer;
        
        public StartField(Player pPlayer) {
            super(FieldIdentifier.START_POSITION_INDEX, 1);
            this.mPlayer = pPlayer;
        }
        
        @Override
        public String toString() {
            return Board.getStartFieldName(this.mPlayer);
        }
    }

    // clear all pieces on this field whilst returning old pieces
    public Set<GamePiece> removeAllPieces() {
        Set<GamePiece> tempPieces = this.mGamePieces;
        this.mGamePieces = new HashSet<>();
        
        return tempPieces;
    }

    public void removePiece(GamePiece pGamePiece) {
        this.mGamePieces.remove(pGamePiece);
    }

    public boolean isBlockedByOwnPiece(Player pPlayer) {
        return hasGamePiece() && getGamePieceOwner() == pPlayer;
    }
}
