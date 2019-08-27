package edu.kit.informatik.material.data;

import edu.kit.informatik.material.ui.InteractionStrings;

public class AssemblyAmountPair implements Comparable<AssemblyAmountPair> {
    private final Assembly assembly;
    private long amount;
    
    AssemblyAmountPair(final Assembly assembly, final long amount) {
        this.assembly = assembly;
        this.amount = amount;
    }
    
    public Assembly getAssembly() {
        return this.assembly;
    }
    
    public long getAmount() {
        return this.amount;
    }
    
    void addAmount(long toBeAdded) {
        this.amount += toBeAdded;
    }
    
    void multiplicateAmount(long factor) {
        this.amount *= factor;
    }

    @Override
    public int compareTo(final AssemblyAmountPair o) {
        return (this.amount == o.amount) ? (this.assembly.compareTo(o.assembly)) : toInt(o.amount - this.amount);
    }
    
    private int toInt(long l) {
        if (l >= (long)Integer.MIN_VALUE && l <= (long)Integer.MAX_VALUE) {
           return (int)l; 
        }
        return l > 0 ? Integer.MAX_VALUE : Integer.MIN_VALUE; 
    }

    @Override
    public String toString() {
        return this.assembly.getName() + InteractionStrings.SEPARATOR_INNER + this.amount;
    }
}
