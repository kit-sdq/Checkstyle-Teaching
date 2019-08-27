package edu.kit.informatik.entity.strategy;

import java.util.ArrayList;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.board.DestinationField;
import edu.kit.informatik.entity.movement.Movement;

public class DiceHookAfterMoveTriply extends DiceHookAfterMove {

    @Override
    public Integer invoke(int pRemainingThrows, int pCurrentRoundPlayerThrowNumber, Movement pChosenMovement, ArrayList<GamePiece> pGamePieces, int pLastRolledNumber) {
        
        boolean hasPiecesOnRegularFields = pGamePieces.stream().anyMatch(piece -> piece.isOnBoard() && !(piece.getCurrentField() instanceof DestinationField));
        boolean anyPieceCanMove = pChosenMovement != null;
        
        /* give 2 extra throws if player cannot move any dice and has no pieces on regular fields */
        if(pCurrentRoundPlayerThrowNumber == 1 && !hasPiecesOnRegularFields && !anyPieceCanMove) {
            return pRemainingThrows + 2;
        }
        
        Integer parentHookValue = super.invoke(pRemainingThrows, pCurrentRoundPlayerThrowNumber, pChosenMovement, pGamePieces, pLastRolledNumber);

        /* take away extra throws, if player can move */
        if(pCurrentRoundPlayerThrowNumber > 1 && anyPieceCanMove) {
            return parentHookValue - pRemainingThrows;
        }
        
        return parentHookValue;
    }

}
