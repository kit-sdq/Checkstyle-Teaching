package edu.kit.informatik._intern.util.concurrent;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An extension of {@link ReentrantReadWriteLock} that support the usage of read/write lock in try-with-statements.
 * 
 * @author  Tobias Bachert
 * @version 1.00, 2016/03/27
 */
public class CloseableReentrantReadWriteLock extends ReentrantReadWriteLock {
    
    private static final long serialVersionUID = 3902731180871422076L;
    
    private final transient CloseableLock write = new CloseableLockWrapper(writeLock());
    private final transient CloseableLock read  = new CloseableLockWrapper(readLock());
    
    /**
     * {@linkplain ReentrantReadWriteLock.WriteLock#lock() Acquires} the write lock.
     * 
     * <p>The returned lock should be used with the following syntax:
     * <blockquote><pre>
     * try (CloseableLock writeLock = lock.openWrite()) {
     *     //...
     * }</pre>
     * </blockquote>
     * 
     * @return a reference to the write lock
     */
    public CloseableLock openWrite() {
        return write.open();
    }
    
    /**
     * {@linkplain ReentrantReadWriteLock.ReadLock#lock() Acquires} the read lock.
     * 
     * <p>The returned lock should be used with the following syntax:
     * <blockquote><pre>
     * try (CloseableLock readLock = lock.openRead()) {
     *     //...
     * }</pre>
     * </blockquote>
     * 
     * @return a reference to the read lock
     */
    public CloseableLock openRead() {
        return read.open();
    }
}