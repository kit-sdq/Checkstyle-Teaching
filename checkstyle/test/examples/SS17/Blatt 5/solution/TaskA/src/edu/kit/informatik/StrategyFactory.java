package edu.kit.informatik;

public class StrategyFactory {
    public static Strategy createStrategy(String name) {
        switch (name) {
            case "S1":
                return new SiblingsStrategy();
            case "S2":
                return new SuccessorsStrategy();
            case "S3":
                return new PredecessorsStrategy();
            default:
                throw new IllegalArgumentException("Strategy not known!");
        }
    }
}
