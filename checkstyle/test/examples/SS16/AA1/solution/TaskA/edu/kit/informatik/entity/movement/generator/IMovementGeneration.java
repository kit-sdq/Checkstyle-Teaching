package edu.kit.informatik.entity.movement.generator;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.movement.Movement;

public interface IMovementGeneration {
    Stream<Movement> offer(int pNum, Stream<GamePiece> pGamePieces);
    void addToChainEnd(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator);
    void addToChainFront(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator);
    // caution! index must be in bounds!!
    void addToChain(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator, int index);
    int getChainSize();
}
