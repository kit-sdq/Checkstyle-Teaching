package edu.kit.informatik.entity;

import edu.kit.informatik.entity.board.BasicField;

public interface IGamePieceProvider {
    GamePiece createGamePiece(Player pPlayer, BasicField pField) ;
}
