package service;

import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class PrioritizedManager {
    private final Set<Task> prioritizedTasks = new TreeSet<>(new StartTimeComparator());

    protected static class StartTimeComparator implements Comparator<Task> {
        @Override
        public int compare(Task t1, Task t2) {
            LocalDateTime startTime1 = t1.getStartTime();
            LocalDateTime startTime2 = t2.getStartTime();
            if (startTime1 == null && startTime2 == null) {
                return 0;
            } else if (startTime1 == null) {
                return 1;
            } else if (startTime2 == null) {
                return -1;
            } else {
                return startTime1.compareTo(startTime2);
            }
        }
    }

    public void add(Task task) {
        prioritizedTasks.add(task);
    }

    public void remove(Task task) {
        prioritizedTasks.remove(task);
    }

    public List<Task> getTasks() {
        return new ArrayList<>(prioritizedTasks);
    }
}
