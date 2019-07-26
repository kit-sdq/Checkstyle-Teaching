package edu.kit.informatik;

public abstract class Job implements Comparable<Job> {
    private String name;
    private int timeOfArrival;
    private int complexity;
    private int remainingTime;

    public Job(String name, int timeOfArrival, int complexity) {
        this.name = name;
        this.timeOfArrival = timeOfArrival;
        this.complexity = complexity;
        this.remainingTime = process();
    }

    public String getName() {
        return name;
    }

    public int getTimeOfArrival() {
        return timeOfArrival;
    }

    public int getComplexity() {
        return complexity;
    }

    public int getRemainingTime() {
        return this.remainingTime;
    }

    public void reduceRemainingTime() {
        reduceRemainingTime(1);
    }

    public void reduceRemainingTime(int amount) {
        remainingTime -= amount;
    }

    @Override
    public int compareTo(Job other) {
        return this.process() - other.process();
    }

    public abstract int process();

}
