package edu.kit.informatik.material.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Assembly implements Comparable<Assembly> {
    private final String name;
    private final Map<Assembly, Long> assemblies;
    
    public Assembly(final String name) {
        this.name = name;
        this.assemblies = new HashMap<>(); 
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isComponent() {
        return this.assemblies.isEmpty();
    }
    
    public boolean isAssembly() {
        return !isComponent();
    }
    
    public List<AssemblyAmountPair> getDirectAssemblies() {
        final List<Assembly> ret = new ArrayList<>(
                Arrays.asList(
                        this.assemblies.keySet().stream().filter(x -> x.isAssembly()).toArray(Assembly[]::new)));
        return sortByAmount(ret);
    }
    
    public List<AssemblyAmountPair> getDirectComponents() {
        List<Assembly> ret = new ArrayList<>(
                Arrays.asList(
                        this.assemblies.keySet().stream().filter(x -> x.isComponent()).toArray(Assembly[]::new)));
        return sortByAmount(ret);
    }
    
    public List<AssemblyAmountPair> getAllDirectComponentsAndAssemblies() {
        final List<AssemblyAmountPair> ret = getDirectAssemblies();
        ret.addAll(getDirectComponents());
        sortByName(ret);
        return ret;
    }
    
    private List<AssemblyAmountPair> sortByAmount(final List<Assembly> toBeSorted) {
        final List<AssemblyAmountPair> pairList = new ArrayList<>();
        toBeSorted.forEach(x -> pairList.add(new AssemblyAmountPair(x, this.assemblies.get(x))));
        Collections.sort(pairList);
        return pairList;
    }
    
    private void sortByName(final List<AssemblyAmountPair> toBeSorted) {
        Collections.sort(toBeSorted, (x,y) -> x.getAssembly().compareTo(y.getAssembly()));
    }
    
    public List<AssemblyAmountPair> getAllAssemblies() {
        List<AssemblyAmountPair> ret = getDirectAssemblies();
        
        if(!ret.isEmpty()) {
            for (final AssemblyAmountPair pair : getDirectAssemblies()) {
                final long amountFactor = pair.getAmount();
                final List<AssemblyAmountPair> subAssemblies = pair.getAssembly().getAllAssemblies();
                subAssemblies.forEach(x -> x.multiplicateAmount(amountFactor));
                ret.addAll(subAssemblies);
            }
        }
        
        ret.sort((x,y) -> x.getAssembly().getName().compareTo(y.getAssembly().getName()));
        ret = summary(ret);
        Collections.sort(ret);
        return ret;
    }
    
    public List<AssemblyAmountPair> getAllComponents() {
        final List<AssemblyAmountPair> allAssemblies = getAllAssemblies();
        final List<AssemblyAmountPair> retPre = new ArrayList<>(getDirectComponents());
        for (final AssemblyAmountPair pair : allAssemblies) {
            final long amountFactor = pair.getAmount();
            final List<AssemblyAmountPair> components = pair.getAssembly().getDirectComponents();
            components.forEach(x -> x.multiplicateAmount(amountFactor));
            retPre.addAll(components);
        }
        
        retPre.sort((x,y) -> x.getAssembly().getName().compareTo(y.getAssembly().getName()));
        final List<AssemblyAmountPair> ret = summary(retPre);
        Collections.sort(ret);
        return ret;
    }
    
    /*
     * sums up, i.e. removes duplicates
     */
    private List<AssemblyAmountPair> summary(final List<AssemblyAmountPair> toSumUp) {
        final List<AssemblyAmountPair> ret = new ArrayList<>();
        
        if (!toSumUp.isEmpty()) {
            AssemblyAmountPair before = toSumUp.get(0);
            ret.add(before);
            for (int i = 1; i < toSumUp.size(); i++) {
                final AssemblyAmountPair current = toSumUp.get(i);
                if (current.getAssembly().getName().equals(before.getAssembly().getName())) {
                    before.addAmount(current.getAmount());
                } else {
                    ret.add(current);
                    before = current;
                }
            }
        }
        
        return ret;
    }
    
    public boolean addComponent(final Assembly assembly, final long amount) {
        final boolean contained = this.assemblies.containsKey(assembly);
        final boolean add = contained || cycleCheck(assembly);
        
        if (add) {
            final long newAmount = contained ? (this.assemblies.get(assembly) + amount) : amount;
            if (newAmount <= 1000) {
                this.assemblies.put(assembly, newAmount); 
            } else {
                return false;
            }
        }
        
        return add;
    }
    
    /*
     * returns true if adding is allowed, i.e. no cycle exists
     */
    private boolean cycleCheck(final Assembly assembly) {
        if (this.equals(assembly)) {
            return false;
        }
        
        final List<AssemblyAmountPair> allAssembliesAndComponentsOfOther = assembly.getAllAssemblies();
        allAssembliesAndComponentsOfOther.addAll(assembly.getAllComponents());
        return allAssembliesAndComponentsOfOther.stream().map(x -> x.getAssembly()).noneMatch(x -> x.equals(this));
    }

    public long removePart(final Assembly toBeRemoved, final long amount) {
        final boolean remove = this.assemblies.containsKey(toBeRemoved) && this.assemblies.get(toBeRemoved) >= amount;
        
        if (remove) {
            final long newAmount = this.assemblies.get(toBeRemoved) - amount;
            this.assemblies.put(toBeRemoved, newAmount);
            if (newAmount == 0) {
                this.assemblies.remove(toBeRemoved);
            }
            return newAmount;
        }
        
        return -1;
    }
    
    protected void clear() {
        this.assemblies.clear();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.getClass(), this.name);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other != null && other.getClass().equals(this.getClass())) {
            return this.name.equals(((Assembly)other).name);
        }
        
        return false;
    }

    @Override
    public int compareTo(final Assembly o) {
        return this.name.compareTo(o.name);
    }
}
