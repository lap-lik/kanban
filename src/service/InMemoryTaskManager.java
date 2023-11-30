package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected Integer id = 0;
    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected final PrioritizedManager prioritizedManager = new PrioritizedManager();
    protected final DataPlanner dataPlanner = new DataPlanner();

    @Override
    public void createTask(Task task) {
        if (task != null) {
            boolean isTrueCell = dataPlanner.fillCells(task);
            if (isTrueCell) {
                task.setId(createId());
                Task newTask = new Task(task);
                tasks.put(newTask.getId(), newTask);
                prioritizedManager.add(newTask);
            }
        }
    }

    @Override
    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            Epic newEpic = new Epic(epic);
            epics.put(newEpic.getId(), newEpic);
        }
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            boolean isTrueCell = dataPlanner.fillCells(subtask);
            if (isTrueCell) {
                subtask.setId(createId());
                Subtask newSubtask = new Subtask(subtask);
                subtasks.put(newSubtask.getId(), newSubtask);
                prioritizedManager.add(newSubtask);
                Epic epic = epics.get(newSubtask.getEpicId());
                epic.getSubtasksIds().add(newSubtask.getId());
                updateEpic(epic);
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        Integer taskId = task.getId();
        if (taskId != null && tasks.containsKey(taskId)) {
            Task savedTask = tasks.get(taskId);
            boolean isStartTimeEqual = Objects.equals(savedTask.getStartTime(), task.getStartTime());
            boolean isEndTimeEqual = Objects.equals(savedTask.getEndTime(), task.getEndTime());
            Task newTask = new Task(task);
            if (isStartTimeEqual && isEndTimeEqual) {
                tasks.put(taskId, newTask);
                prioritizedManager.remove(savedTask);
                prioritizedManager.add(newTask);
            } else {
                dataPlanner.clearCells(savedTask);
                boolean isTrueCell = dataPlanner.fillCells(newTask);
                if (isTrueCell) {
                    tasks.put(taskId, newTask);
                    prioritizedManager.remove(savedTask);
                    prioritizedManager.add(newTask);
                } else {
                    dataPlanner.fillCells(savedTask);
                }
            }
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Integer subtaskId = subtask.getId();
        if (subtaskId != null && subtasks.containsKey(subtaskId)) {
            Subtask savedSubtask = subtasks.get(subtaskId);
            boolean isStartTimeEqual = Objects.equals(savedSubtask.getStartTime(), subtask.getStartTime());
            boolean isEndTimeEqual = Objects.equals(savedSubtask.getEndTime(), subtask.getEndTime());
            Subtask newSubtask = new Subtask(subtask);
            if (isStartTimeEqual || isEndTimeEqual) {
                subtasks.put(subtaskId, newSubtask);
                prioritizedManager.remove(savedSubtask);
                prioritizedManager.add(newSubtask);
                updateEpic(epics.get(newSubtask.getEpicId()));
            } else {
                dataPlanner.clearCells(savedSubtask);
                boolean isTrueCell = dataPlanner.fillCells(subtask);
                if (isTrueCell) {
                    subtasks.put(subtaskId, newSubtask);
                    prioritizedManager.remove(savedSubtask);
                    prioritizedManager.add(newSubtask);
                    updateEpic(epics.get(newSubtask.getEpicId()));
                } else {
                    dataPlanner.fillCells(savedSubtask);
                }
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Integer epicId = epic.getId();
        if (epicId != null && epics.containsKey(epicId)) {
            Epic newEpic = new Epic(epic);
            newEpic.setStatus(checkStatus(newEpic));
            updateEpicDateTime(newEpic);
            epics.put(epicId, newEpic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Task getOneTask(Integer key) {
        Task task = tasks.get(key);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return new Task(task);
    }

    @Override
    public Epic getOneEpic(Integer key) {
        Epic epic = epics.get(key);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        return new Epic(epic);
    }

    @Override
    public Subtask getOneSubtask(Integer key) {
        Subtask subtask = subtasks.get(key);
        if (subtask == null) {
            return null;
        }
        historyManager.add(subtask);
        return new Subtask(subtask);
    }

    @Override
    public List<Subtask> getAllSubtasksByEpicId(Integer key) {
        ArrayList<Subtask> subtasksByEpic = new ArrayList<>();
        if (epics.containsKey(key)) {
            epics.get(key).getSubtasksIds().stream()
                    .map(subtasks::get)
                    .forEach(subtasksByEpic::add);
        }
        return subtasksByEpic;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedManager.getTasks();
    }

    @Override
    public Map<LocalDateTime, Boolean> getIntervalGrid() {
        return new HashMap<>(dataPlanner.getIntervalGrid());
    }

    @Override
    public void deleteAllTasks() {
        tasks.keySet().forEach(taskId -> {
            historyManager.remove(taskId);
            dataPlanner.clearCells(tasks.get(taskId));
            prioritizedManager.remove(tasks.get(taskId));
        });
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
        subtasks.keySet().forEach(subtaskId -> {
            historyManager.remove(subtaskId);
            dataPlanner.clearCells(subtasks.get(subtaskId));
            prioritizedManager.remove(subtasks.get(subtaskId));
        });
        subtasks.clear();
        if (!epics.isEmpty()) {
            epics.values().forEach(value -> {
                value.setStatus(Status.NEW);
                value.setSubtasksIds(new ArrayList<>());
            });
        }
    }

    @Override
    public void deleteOneTask(Integer key) {
        Task task = tasks.get(key);
        if (task != null) {
            dataPlanner.clearCells(task);
            prioritizedManager.remove(task);
            historyManager.remove(key);
            tasks.remove(key);
        }
    }

    @Override
    public void deleteOneEpic(Integer key) {
        Epic epic = epics.get(key);
        if (epic != null) {
            epic.getSubtasksIds().forEach(integer -> {
                historyManager.remove(integer);
                subtasks.remove(integer);
            });
            historyManager.remove(key);
            epics.remove(key);
        }
    }

    @Override
    public void deleteOneSubtask(Integer key) {
        Subtask subtask = subtasks.get(key);
        if (subtask != null) {
            dataPlanner.clearCells(subtask);
            prioritizedManager.remove(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.getSubtasksIds().remove(key);
            updateEpic(epic);
            historyManager.remove(key);
            subtasks.remove(key);
        }
    }

    protected Status checkStatus(Epic epic) {
        List<Status> statuses = epic.getSubtasksIds().stream()
                .map(e -> subtasks.get(e).getStatus())
                .distinct()
                .collect(Collectors.toList());
        if (statuses.isEmpty()) {
            return Status.NEW;
        } else if (statuses.size() == 1) {
            return statuses.get(0);
        } else if (!statuses.contains(Status.IN_PROGRESS) && !statuses.contains(Status.NEW)) {
            return Status.DONE;
        } else if (!statuses.contains(Status.DONE) && !statuses.contains(Status.IN_PROGRESS)) {
            return Status.NEW;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    protected void updateEpicDateTime(Epic epic) {
        List<Subtask> subtasksByEpic = epic.getSubtasksIds().stream()
                .map(subtasks::get)
                .collect(Collectors.toList());

        LocalDateTime epicStartTime = subtasksByEpic.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime epicEndTime = subtasksByEpic.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        Duration epicDuration = subtasksByEpic.stream()
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration::plus)
                .orElse(null);

        epic.setStartTime(epicStartTime);
        epic.setEndTime(epicEndTime);
        epic.setDuration(epicDuration);
    }
    protected Integer createId() {
        return ++id;
    }
}
