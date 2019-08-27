package edu.kit.informatik._intern.util.concurrent;

import java.util.concurrent.locks.Lock;

/**
 * Interface for auto closable locks.
 * 
 * <p>It is highly deprecated to use this interface for locks that may raise any exception when locking due to resulting
 * in an monitor-exception or an incorrect decrementing of the hold-count.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
public interface CloseableLock extends Lock, AutoCloseable {
    
    /**
     * {@linkplain #lock() Acquires} the lock and returns a reference to the lock.
     * 
     * @return a reference to the lock
     */
    default CloseableLock open() {
        lock();
        return this;
    }
    
    /**
     * {@link #unlock() Releases} the lock.
     */
    @Override
    default void close() {
        unlock();
    }
}
