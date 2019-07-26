package edu.kit.informatik.entity.strategy;

import java.util.ArrayList;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.movement.Movement;

public class DiceHookAfterMove {

    public Integer invoke(int pRemainingThrows, int pCurrentRoundPlayerThrowNumber, Movement pChosenMovement, ArrayList<GamePiece> pGamePieces, int pLastRolledNumber) {
        /* are all game pieces out of start position?? and did player throw the special BRING_INTO_GAME_NUMBER? */
        if(pLastRolledNumber == StrategyManager.BRING_INTO_GAME_NUMBER
                && (pChosenMovement == null || !pChosenMovement.isStartMove())) {

            /* give player an extra throw */
            return pRemainingThrows + 1;
        }
                    
        return pRemainingThrows;
    }

}
