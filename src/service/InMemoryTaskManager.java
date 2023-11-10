package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void createTask(Task task) {
        if (task != null) {
            task.setId(createId());
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(createId());
            subtasks.put(subtask.getId(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksIds().add(subtask.getId());
            updateEpic(epic);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null && tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpic(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epic != null && epics.containsKey(epic.getId())) {
            epic.setStatus(checkStatus(epic));
            epics.put(epic.getId(), epic);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getOneTask(Integer key) {
        Task task = tasks.get(key);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getOneEpic(Integer key) {
        Epic epic = epics.get(key);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getOneSubtask(Integer key) {
        Subtask subtask = subtasks.get(key);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpicId(Integer key) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        if (epics.containsKey(key)) {
            for (Integer integer : epics.get(key).getSubtasksIds()) {
                subtasksByEpic.add(subtasks.get(integer));
            }
        }
        return subtasksByEpic;
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(historyManager::remove);
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.keySet().forEach(historyManager::remove);
        epics.clear();
        deleteAllSubtasks();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.keySet().forEach(historyManager::remove);
        subtasks.clear();
        if (!epics.isEmpty()) {
            for (Epic value : epics.values()) {
                value.setStatus(Status.NEW);
                value.setSubtasksIds(new ArrayList<>());
            }
        }
    }

    @Override
    public void deleteOneTask(Integer key) {
        if (key != null && tasks.containsKey(key)) {
            historyManager.remove(key);
            tasks.remove(key);
        }
    }

    @Override
    public void deleteOneEpic(Integer key) {
        if (key != null && epics.containsKey(key)) {
            for (Integer integer : epics.get(key).getSubtasksIds()) {
                historyManager.remove(integer);
                subtasks.remove(integer);
            }
            historyManager.remove(key);
            epics.remove(key);
        }
    }

    @Override
    public void deleteOneSubtask(Integer key) {
        Subtask subtask = subtasks.get(key);
        if (key != null && subtasks.containsKey(key)) {
            Epic epic = epics.get(subtasks.get(key).getEpicId());
            epic.getSubtasksIds().remove(key);
            updateEpic(epic);
            historyManager.remove(key);
            subtasks.remove(key);
        }
    }

    private Integer createId() {
        return ++id;
    }

    protected Status checkStatus(Epic epic) {
        Status status = epic.getStatus();
        List<Status> statuses = epic.getSubtasksIds().stream().map(e -> subtasks.get(e).getStatus()).collect(Collectors.toList());
        if (statuses.isEmpty()) {
            status = Status.NEW;
        } else if (statuses.size() == 1) {
            status = statuses.get(0);
        } else if (!statuses.contains(Status.IN_PROGRESS) && !statuses.contains(Status.NEW)) {
            status = Status.DONE;
        } else if (!statuses.contains(Status.DONE) && !statuses.contains(Status.IN_PROGRESS)) {
            status = Status.NEW;
        } else {
            status = Status.IN_PROGRESS;
        }
        return status;
    }
}
