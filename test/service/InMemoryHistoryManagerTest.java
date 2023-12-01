package service;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task1;
    Epic epic1;
    Subtask subtask1;

    @BeforeEach
    public void start() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task("TASK-1", "TASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 15), Duration.ofMinutes(30));
        task1.setId(1);
        historyManager.add(task1);
        epic1 = new Epic("EPIC-1", "EPIC-1-DESCRIPTION");
        epic1.setId(2);
        historyManager.add(epic1);
        subtask1 = new Subtask("SUBTASK-1", "SUBTASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 15), Duration.ofMinutes(30), 2);
        subtask1.setId(3);
        historyManager.add(subtask1);
    }

    @Test
    void getHistory() {
        List<Task> listTask = historyManager.getHistory();
        assertEquals(3, listTask.size(), "История возвращается неправильно.");
    }

    @Test
    void addTask() {
        historyManager.add(task1);
        List<Task> listTask = historyManager.getHistory();
        assertEquals(3, listTask.size());
        assertEquals(task1, listTask.get(2), "В историю добавляется неправильно.");
    }

    @Test
    void remove() {
        List<Task> listTasks = historyManager.getHistory();
        assertEquals(3, listTasks.size(), "История возвращается неправильно.");
        historyManager.remove(1);
        List<Task> listTask = historyManager.getHistory();
        assertEquals(2, listTask.size(), "Из истории удаляется неправильно.");
    }
}