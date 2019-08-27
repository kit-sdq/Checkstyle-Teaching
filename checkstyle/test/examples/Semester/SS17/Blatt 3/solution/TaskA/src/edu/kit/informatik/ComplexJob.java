package edu.kit.informatik;

public class ComplexJob extends Job {

    public ComplexJob(String name, int timeOfArrival, int complexity) {
        super(name, timeOfArrival, complexity);
    }

    @Override
    public int process() {
        return getComplexity() * getComplexity();
    }
}
