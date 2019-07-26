package edu.kit.informatik.entity.movement.generator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import edu.kit.informatik.entity.GamePiece;
import edu.kit.informatik.entity.board.Board;
import edu.kit.informatik.entity.movement.Movement;

// generic movement generator uses the observer pattern
public class GenericMovementGenerator implements IMovementGeneration {    
    private final List<BiFunction<Integer, GamePiece, Stream<Movement>>> mCustomFunctions = new LinkedList<>();
    
    @Override
    public void addToChainEnd(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator) {
        addToChain(pCustomGenerator, this.mCustomFunctions.size());
    }
   
    @Override
    public void addToChainFront(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator) {
        addToChain(pCustomGenerator, 0);
    }
    
    @Override
    public void addToChain(BiFunction<Integer, GamePiece, Stream<Movement>> pCustomGenerator, int index) {
        this.mCustomFunctions.add(index, pCustomGenerator);
    }
    
    @Override
    public Stream<Movement> offer(int pNum, Stream<GamePiece> pGamePieces) {
        // distinct removes all duplicate movement offerings / please note that Movement.hashCode() must be implemented properly for this to work
        Stream<Movement> stream = pGamePieces.flatMap(gp -> process(gp, pNum))
                .filter(m -> Board.isValidFieldIndex(m.getDestinationIndex()))
                .distinct();
  
        return stream;
    }
    
    private Stream<Movement> process(GamePiece pGamePiece, int pDistance) {
        Stream<Movement> stream = Stream.empty();
        
        /* add custom generators */
        for(BiFunction<Integer, GamePiece, Stream<Movement>> cGenerator : this.mCustomFunctions) {
            stream = Stream.concat(stream, cGenerator.apply(pDistance, pGamePiece));
        }
        
        return stream;
    }

    @Override
    public int getChainSize() {
        return this.mCustomFunctions.size();
    }
}