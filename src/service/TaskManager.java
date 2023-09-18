package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private static Integer id = 1;
    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap<Integer, Epic> epicMap = new HashMap<>();
    private HashMap<Integer, Subtask> subtaskMap = new HashMap<>();

    private Integer createId() {
        return id++;
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(createId());
            taskMap.put(task.getId(), task);
        }
    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(createId());
            epicMap.put(epic.getId(), epic);
        }
    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(createId());
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(subtask.getEpicId());
            ArrayList<Integer> subtasksId = epic.getSubtaskIds();
            subtasksId.add(subtask.getId());
            updateEpic(epic);
        }
    }

    public void updateTask(Task task) {
        if (task != null && taskMap.containsKey(task.getId())) {
            taskMap.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtaskMap.containsKey(subtask.getId())) {
            subtaskMap.put(subtask.getId(), subtask);
            Epic epic = epicMap.get(subtask.getEpicId());
            updateEpic(epic);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epicMap.containsKey(epic.getId())) {
            if (epic.getSubtaskIds().isEmpty()) {
                epic.setStatus(Status.NEW);
                epicMap.put(epic.getId(), epic);
            } else {
                epic.setStatus(checkStatus(epic));
                epicMap.put(epic.getId(), epic);
            }
        }
    }

    private Status checkStatus(Epic epic) {
        Status status = epic.getStatus();
        List<Status> statuses = epic.getSubtaskIds().stream().map(e -> subtaskMap.get(e).getStatus()).collect(Collectors.toList());
        if (statuses.size() == 1){
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

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(taskMap.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicMap.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtaskMap.values());
    }

    public Task getOneTask(Integer key) {
        return taskMap.get(key);
    }

    public Epic getOneEpic(Integer key) {
        return epicMap.get(key);
    }

    public Subtask getOneSubtask(Integer key) {
        return subtaskMap.get(key);
    }

    public ArrayList<Subtask> getAllSubtasksByEpicId(Integer key) {
        ArrayList<Subtask> arrayListSubtask = new ArrayList<>();
        if (epicMap.containsKey(key)) {
            ArrayList<Integer> subtaskId = epicMap.get(key).getSubtaskIds();
            for (Integer integer : subtaskId) {
                arrayListSubtask.add(subtaskMap.get(integer));
            }
        }
        return arrayListSubtask;
    }

    public void deleteAllTasks() {
        taskMap.clear();
    }

    public void deleteAllEpics() {
        epicMap.clear();
        deleteAllSubtasks();
    }

    public void deleteAllSubtasks() {
        subtaskMap.clear();
        if (!epicMap.isEmpty()) {
            for (Epic value : epicMap.values()) {
                value.setStatus(Status.NEW);
                value.setSubtaskIds(new ArrayList<>());
            }
        }
    }

    public void deleteOneTask(Integer key) {
        if (key != null && taskMap.containsKey(key)) {
            taskMap.remove(key);
        }
    }

    public void deleteOneEpic(Integer key) {
        if (key != null && epicMap.containsKey(key)) {
            for (Integer integer : epicMap.get(key).getSubtaskIds()) {
                subtaskMap.remove(integer);
            }
            epicMap.remove(key);
        }
    }

    public void deleteOneSubtask(Integer key) {
        if (key != null && subtaskMap.containsKey(key)) {
            Epic epic = epicMap.get(subtaskMap.get(key).getEpicId());
            epic.getSubtaskIds().remove(key);
            updateEpic(epic);
            subtaskMap.remove(key);
        }
    }

    /*
    METHODS FOR CHECKING
     */
    public void printOneTask(Integer key) {
        if (taskMap.containsKey(key)) {
            System.out.println(taskMap.get(key));
        } else {
            System.out.printf("TASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printOneEpic(Integer key) {
        if (epicMap.containsKey(key)) {
            System.out.println(epicMap.get(key));
        } else {
            System.out.printf("EPIC TASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printOneSubtask(Integer key) {
        if (subtaskMap.containsKey(key)) {
            System.out.println(subtaskMap.get(key));
        } else {
            System.out.printf("SUBTASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printAllTasks() {
        if (taskMap.isEmpty()) {
            System.out.println("TASKS NOT FOUND!");
        }
        for (Task value : taskMap.values()) {
            System.out.println(value);
        }
    }

    public void printAllEpics() {
        if (epicMap.isEmpty()) {
            System.out.println("EPIC TASKS NOT FOUND!");
        }
        for (Epic value : epicMap.values()) {
            System.out.println(value);
        }
    }

    public void printAllSubtasks() {
        if (subtaskMap.isEmpty()) {
            System.out.println("SUBTASKS NOT FOUND!");
        }
        for (Subtask value : subtaskMap.values()) {
            System.out.println(value);
        }
    }
}
