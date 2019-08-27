package edu.kit.informatik;

public class Product extends Node {
    private int id;
    
    public Product(String name, int id) {
        super(name);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean isAmbigiousWith(Node o) {
        if (this == o || o == null) {
            return false;
        }
        
        if (getClass() != o.getClass()) {
            return getName().equals(o.getName());
        }
        
        Product p = (Product) o;
        
        return getName().equals(p.getName()) ^ id == p.id;
    }
    
    @Override
    public String toString() {
        return getName() + ":" + id; 
    }
}
