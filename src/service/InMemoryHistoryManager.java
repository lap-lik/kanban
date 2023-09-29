package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final List<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() > 9) {
            history.remove(0);
        }
            history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
