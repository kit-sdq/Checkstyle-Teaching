package edu.kit.informatik;

import java.util.Collection;

public abstract class WaitingArea {
    public abstract void add(Job job);

    public abstract Job remove();

    public abstract Collection<Job> getQueue();

    @Override
    public String toString() {
        if (getQueue().isEmpty()) {
            return "Waiting:empty";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Waiting:");

        for (Job job : getQueue()) {
            sb.append(job.getName());
            sb.append("(");
            sb.append(job.getRemainingTime());
            sb.append(")");
            sb.append(",");
        }

        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
