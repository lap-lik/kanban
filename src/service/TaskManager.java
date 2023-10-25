package service;

import model.*;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    void createTask(Task task);

    void createEpic(Epic epic);

    void createSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic epic);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    Task getOneTask(Integer key);

    Epic getOneEpic(Integer key);

    Subtask getOneSubtask(Integer key);

    ArrayList<Subtask> getAllSubtasksByEpicId(Integer key);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    void deleteOneTask(Integer key);

    void deleteOneEpic(Integer key);

    void deleteOneSubtask(Integer key);
}