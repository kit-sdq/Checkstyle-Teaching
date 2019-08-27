package edu.kit.informatik.entity.strategy;

public class DiceHookPlayerChange {

    public Integer invoke(int pRemainingThrows) {
        assert pRemainingThrows == 0;
        
        // TODO debug: throw exception, so we see this happen in simulation
        if(pRemainingThrows != 0) {
            throw new IllegalStateException("Error, the remaining throw number should be 0 at player change, but appearently it is not.");
        }
        
        return 1;   // we do not need to take pRemainingThrows into account, because it should be 0 for a player change!
    }

}
