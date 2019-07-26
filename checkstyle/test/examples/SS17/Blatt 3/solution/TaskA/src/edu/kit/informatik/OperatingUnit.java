package edu.kit.informatik;

import java.util.LinkedList;
import java.util.List;

public class OperatingUnit {
    private List<Job> jobs;
    private WaitingArea waitingArea;
    private int timeSlice;
    private int time;
    private Job currentJob;

    public OperatingUnit(List<Job> jobs, WaitingArea waitingArea, int timeSlice) {
        this.jobs = jobs;
        this.waitingArea = waitingArea;
        this.timeSlice = timeSlice;
        this.time = 0;
        this.currentJob = null;
    }

    public String[] simulate() {
        List<String> output = new LinkedList<>();
        int remainingTimeSlice = timeSlice;

        while (true) {
            while (jobs.size() > 0 && jobs.get(0).getTimeOfArrival() == time) {
                waitingArea.add(jobs.remove(0));
            }

            //Preemption after arrival of new Tasks
            if (currentJob != null && timeSlice != 0 && --remainingTimeSlice == 0) {
                waitingArea.add(currentJob);
                currentJob = null;
                remainingTimeSlice = timeSlice;
            }

            if (currentJob == null) {
                currentJob = waitingArea.remove();
            }

            if (currentJob != null) {
                output.add(toString());
                currentJob.reduceRemainingTime();

                if (currentJob.getRemainingTime() == 0) {
                    currentJob = null;
                    remainingTimeSlice = timeSlice;
                }
            } else if (jobs.isEmpty()) {
                break;
            }

            time++;
        }

        return output.toArray(new String[output.size()]);
    }

    @Override
    public String toString() {
        return time + ":" + currentJob.getName() + "(" + currentJob.getRemainingTime() + ")," + waitingArea.toString();
    }
}
