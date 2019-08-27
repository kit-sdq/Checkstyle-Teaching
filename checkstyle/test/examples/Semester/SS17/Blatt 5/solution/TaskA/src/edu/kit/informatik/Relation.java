package edu.kit.informatik;

public enum Relation {
    CONTAINS("CONTAINED_IN"),
    CONTAINED_IN("CONTAINS"),
    SUCCESSOR_OF("PREDECESSOR_OF"),
    PREDECESSOR_OF("SUCCESSOR_OF");
    
    private String reverseRelation;
    
    Relation(String reverseRelation) {
        this.reverseRelation = reverseRelation;
    }
    
    public static Relation fromString(String input) {
        return Relation.valueOf(input.replace("-", "_").toUpperCase());
    }
    
    public Relation getReverseRelation() {
        return valueOf(reverseRelation);
    }
    
    @Override
    public String toString() {
        return this.name().replace("_", "-").toLowerCase();
    }
    
    public String toDOT() {
        return this.name().replace("_", "").toLowerCase();
    }
}
