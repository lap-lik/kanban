package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface TaskManager {
    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    Task getOneTask(Integer key);

    Epic getOneEpic(Integer key);

    Subtask getOneSubtask(Integer key);

    List<Subtask> getAllSubtasksByEpicId(Integer key);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteOneTask(Integer key);

    void deleteOneEpic(Integer key);

    void deleteOneSubtask(Integer key);
}