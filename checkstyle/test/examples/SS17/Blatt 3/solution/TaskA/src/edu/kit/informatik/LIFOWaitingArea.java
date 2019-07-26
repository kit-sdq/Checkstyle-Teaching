package edu.kit.informatik;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

public class LIFOWaitingArea extends WaitingArea {
    private Deque<Job> area;

    public LIFOWaitingArea() {
        this.area = new ArrayDeque<>();
    }

    @Override
    public void add(Job job) {
        area.push(job);
    }

    @Override
    public Job remove() {
        if (area.isEmpty()) {
            return null;
        }

        return area.pop();
    }

    @Override
    public Collection<Job> getQueue() {
        return area;
    }
}
