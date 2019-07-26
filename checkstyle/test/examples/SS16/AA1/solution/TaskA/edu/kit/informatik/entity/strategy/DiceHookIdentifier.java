package edu.kit.informatik.entity.strategy;

import java.util.Map;
import java.util.Objects;

public final class DiceHookIdentifier<T> { 
    public static final DiceHookIdentifier<DiceHookPlayerChange> PLAYER_CHANGE = new DiceHookIdentifier<>(DiceHookPlayerChange.class);
    public static final DiceHookIdentifier<DiceHookAfterMove> AFTER_MOVE = new DiceHookIdentifier<>(DiceHookAfterMove.class);
    private Class<T> mHookImplementationClass;
    
    private DiceHookIdentifier(Class<T> pDiceHookClass) {
        this.mHookImplementationClass = pDiceHookClass;
    }
    
    // make sure the object was assigned to this hook identifier!
    private T cast(Object pDiceHook) {
        return this.mHookImplementationClass.cast(pDiceHook);
    }

    public T retreiveImplementation(Map<DiceHookIdentifier<?>, Object> mDiceHooks, DiceHookIdentifier<T> pHookId) {
        return cast(mDiceHooks.get(Objects.requireNonNull(pHookId)));
    }
}
