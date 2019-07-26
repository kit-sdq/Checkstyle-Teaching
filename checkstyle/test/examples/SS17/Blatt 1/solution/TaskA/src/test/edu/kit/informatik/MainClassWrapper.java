package edu.kit.informatik;

public final class MainClassWrapper {
    
    public static void main(final String[] args) {
        TaskA.valueOf(args[0]).process(System.out::println);
    }
    
    private MainClassWrapper() {
    }
}
