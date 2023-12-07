package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    boolean createTask(Task task);

    boolean createEpic(Epic epic);

    boolean createSubtask(Subtask subtask);

    boolean updateTask(Task task);

    boolean updateSubtask(Subtask subtask);

    boolean updateEpic(Epic epic);

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

    boolean deleteOneTask(Integer key);

    boolean deleteOneEpic(Integer key);

    boolean deleteOneSubtask(Integer key);
}