package edu.kit.informatik.material.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

public class AssemblyManager {
    private final Map<String, Assembly> assemblies;

    public AssemblyManager() {
        this.assemblies = new HashMap<>();
    }

    public Assembly getAssembly(final String name) {
        return this.assemblies.get(name);
    }

    public boolean addAssembly(final String name, final Map<String, Integer> assemblies) {
        boolean contained = this.assemblies.containsKey(name);
        final Assembly assembly = contained ? this.assemblies.get(name) : new Assembly(name);
        if (assembly.isAssembly()) { // assembly is already not a component but an assembly
            return false;
        }
        for (final Entry<String, Integer> entry : assemblies.entrySet()) {
            final String compName = entry.getKey();
            final int compAmount = entry.getValue();
            final Assembly compToBeAdded = this.assemblies.containsKey(compName) ? this.assemblies.get(compName)
                    : new Assembly(compName);
            final boolean added = assembly.addComponent(compToBeAdded, compAmount);
            if (!added) {
                return false;
            }
        }
        this.assemblies.put(name, assembly);
        assembly.getAllDirectComponentsAndAssemblies()
                .forEach(x -> this.assemblies.put(x.getAssembly().getName(), x.getAssembly()));
        return true;
    }

    public boolean removeAssembly(final String name) {
        if (this.assemblies.containsKey(name) && this.assemblies.get(name).isAssembly()) {
            final List<AssemblyAmountPair> containedComponents = this.assemblies.get(name).getDirectComponents();
            // removing the assembly with the given name
            final Assembly assembly = this.assemblies.get(name);
            assembly.clear();
            containedComponents.add(new AssemblyAmountPair(assembly, 0));
            removeSideEffect(containedComponents);
            return true;
        }
        return false;
    }
    
    private void removeSideEffect(final List<AssemblyAmountPair> containedComponents) {
        // deleting directly contained single-components from assemblies map which are
        // not contained in other assemblies
        final Set<Assembly> allComponents = new TreeSet<>();
        this.assemblies.values().forEach(x -> allComponents.addAll(Arrays
                .asList(x.getDirectComponents().stream().map(y -> y.getAssembly()).toArray(Assembly[]::new))));
        containedComponents.forEach(x -> {
            if (!allComponents.contains(x.getAssembly())) {
                this.assemblies.remove(x.getAssembly().getName());
            }
        });
    }

    public boolean removePart(final Assembly assembly, final Assembly part, final long amount) {
        final long newAmount = assembly.removePart(part, amount);
        if (newAmount == 0) {
            if (part.isComponent()) {
                removeSideEffect(Arrays.asList(new AssemblyAmountPair(part,0)));
            }
            if (assembly.isComponent()) {
                removeSideEffect(Arrays.asList(new AssemblyAmountPair(assembly,0)));
            }
        }
        return newAmount != -1;
    }
}
