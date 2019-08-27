package edu.kit.informatik;

import java.util.Collection;
import java.util.PriorityQueue;
import java.util.Queue;

public class SJFWaitingArea extends WaitingArea {
    private Queue<Job> area;

    public SJFWaitingArea() {
        this.area = new PriorityQueue<>();
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
