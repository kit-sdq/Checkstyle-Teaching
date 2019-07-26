package edu.kit.informatik;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class FIFOWaitingArea extends WaitingArea {
    private Queue<Job> area;

    public FIFOWaitingArea() {
        this.area = new LinkedList<>();
    }

    @Override
    public void add(Job job) {
        area.offer(job);
    }

    @Override
    public Job remove() {
        return area.poll();
    }

    @Override
    public Collection<Job> getQueue() {
        return area;
    }
}
