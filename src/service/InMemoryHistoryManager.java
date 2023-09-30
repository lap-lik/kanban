package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    public static final int LIST_SIZE = 9;
    private static final List<Task> history = new ArrayList<>();
    @Override
    public void add(Task task) {
        if (history.size() > LIST_SIZE) {
            history.remove(0);
        }
            history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
