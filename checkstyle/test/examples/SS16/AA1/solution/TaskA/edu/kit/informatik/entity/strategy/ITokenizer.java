package edu.kit.informatik.entity.strategy;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.movement.Movement;

public interface ITokenizer extends BiFunction<Integer, GamePiece, Stream<Movement>> { }
