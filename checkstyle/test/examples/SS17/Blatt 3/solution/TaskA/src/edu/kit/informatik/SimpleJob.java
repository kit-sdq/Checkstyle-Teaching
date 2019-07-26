package edu.kit.informatik;

public class SimpleJob extends Job {

    public SimpleJob(String name, int timeOfArrival, int complexity) {
        super(name, timeOfArrival, complexity);
    }

    @Override
    public int process() {
        return getComplexity();
    }
}
