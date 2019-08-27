package edu.kit.informatik._intern.util.concurrent;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * Wrapper implementation for the {@code CloseableLock} interface.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
class CloseableLockWrapper implements CloseableLock {
    
    private final Lock lock;
    
    /**
     * Creates a new auto closeable wrapper for the given {@code source} lock.
     * 
     * @param source the source lock
     */
    CloseableLockWrapper(final Lock source) {
        lock = Objects.requireNonNull(source);
    }
    
    @Override
    public void lock() {
        lock.lock();
    }
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
        lock.lockInterruptibly();
    }
    
    @Override
    public Condition newCondition() {
        return lock.newCondition();
    }
    
    @Override
    public boolean tryLock() {
        return lock.tryLock();
    }
    
    @Override
    public boolean tryLock(final long time, final TimeUnit unit) throws InterruptedException {
        return lock.tryLock(time, unit);
    }
    
    @Override
    public void unlock() {
        lock.unlock();
    }
}
