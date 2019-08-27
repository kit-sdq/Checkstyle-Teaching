package edu.kit.informatik._intern.util.function;

import java.util.Objects;

//TODO JAVADOC
public interface ByteConsumer {
    
    void accept(final byte value);
    
    default ByteConsumer andThen(final ByteConsumer after) {
        Objects.requireNonNull(after);
        
        return (b) -> {
            accept(b);
            after.accept(b);
        };
    }
}
