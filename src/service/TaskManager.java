package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static Integer id = 1;
    private HashMap<Integer, Task> taskHashMap;
    private HashMap<Integer, Epic> epicHashMap;
    private HashMap<Integer, Subtask> subtaskHashMap;

    public TaskManager() {
        taskHashMap = new HashMap<>();
        epicHashMap = new HashMap<>();
        subtaskHashMap = new HashMap<>();
    }

    public void createTask(Task task) {
        if (task != null) {
            task.setId(id++);
            taskHashMap.put(task.getId(), task);
        }
    }

    public void createEpic(Epic epic) {
        if (epic != null) {
            epic.setId(id++);
            epicHashMap.put(epic.getId(), epic);
        }
    }

    public void createSubtask(Subtask subtask) {
        if (subtask != null) {
            subtask.setId(id++);
            subtaskHashMap.put(subtask.getId(), subtask);
            Epic epic = epicHashMap.get(subtask.getEpicId());
            ArrayList<Integer> subtasksId = epic.getListSubtasksId();
            subtasksId.add(subtask.getId());
            updateEpic(epic);
        }
    }

    public void updateTask(Task task) {
        if (task != null && taskHashMap.containsKey(task.getId())) {
            taskHashMap.put(task.getId(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtask != null && subtaskHashMap.containsKey(subtask.getId())) {
            subtaskHashMap.put(subtask.getId(), subtask);
            Epic epic = epicHashMap.get(subtask.getEpicId());
            updateEpic(epic);
        }
    }

    public void updateEpic(Epic epic) {
        if (epic != null && epicHashMap.containsKey(epic.getId())) {
            if (epic.getListSubtasksId().isEmpty()) {
                epic.setStatus(Status.NEW);
                epicHashMap.put(epic.getId(), epic);
            } else {
                Status status = Status.DONE;
                ArrayList<Integer> subtasksId = epic.getListSubtasksId();
                for (Integer subtaskId : subtasksId) {
                    Status tepmStatus = subtaskHashMap.get(subtaskId).getStatus();
                    if (tepmStatus.equals(Status.NEW) && !status.equals(Status.IN_PROGRESS)) {
                        status = tepmStatus;
                    }
                    if (tepmStatus.equals(Status.IN_PROGRESS)) {
                        status = tepmStatus;
                    }
                }
                epic.setStatus(status);
                epicHashMap.put(epic.getId(), epic);
            }
        }
    }

    public ArrayList<Task> getAllTask() {
        return new ArrayList<Task>(taskHashMap.values());
    }

    public ArrayList<Epic> getAllEpic() {
        return new ArrayList<Epic>(epicHashMap.values());
    }

    public ArrayList<Subtask> getAllSubtask() {
        return new ArrayList<Subtask>(subtaskHashMap.values());
    }

    public Task getOneTask(Integer key) {
        return taskHashMap.get(key);
    }

    public Epic getOneEpic(Integer key) {
        return epicHashMap.get(key);
    }

    public Subtask getOneSubtask(Integer key) {
        return subtaskHashMap.get(key);
    }

    public ArrayList<Subtask> getAllSubtaskByEpicId(Integer key) {
        ArrayList<Subtask> arrayListSubtask = new ArrayList<>();
        if (epicHashMap.containsKey(key)) {
            ArrayList<Integer> subtaskId = epicHashMap.get(key).getListSubtasksId();
            for (Integer integer : subtaskId) {
                arrayListSubtask.add(subtaskHashMap.get(integer));
            }
        }
        return arrayListSubtask;
    }

    public void deleteAllTask() {
        taskHashMap.clear();
    }

    public void deleteAllEpic() {
        epicHashMap.clear();
        deleteAllSubtask();
    }

    public void deleteAllSubtask() {
        subtaskHashMap.clear();
        if (!epicHashMap.isEmpty()) {
            for (Epic value : epicHashMap.values()) {
                value.setStatus(Status.NEW);
                value.setListSubtasksId(new ArrayList<>());
            }
        }
    }

    public void deleteOneTask(Integer key) {
        if (key != null && taskHashMap.containsKey(key)) {
            taskHashMap.remove(key);
        }
    }

    public void deleteOneEpic(Integer key) {
        if (key != null && epicHashMap.containsKey(key)) {
            for (Integer integer : epicHashMap.get(key).getListSubtasksId()) {
                subtaskHashMap.remove(integer);
            }
            epicHashMap.remove(key);
        }
    }

    public void deleteOneSubtask(Integer key) {
        if (key != null && subtaskHashMap.containsKey(key)) {
            Epic epic = epicHashMap.get(subtaskHashMap.get(key).getEpicId());
            epic.getListSubtasksId().remove(key);
            subtaskHashMap.remove(key);
        }
    }

    /*
    METHODS FOR CHECKING
     */
    public void printOneTask(Integer key) {
        if (taskHashMap.containsKey(key)) {
            System.out.println(taskHashMap.get(key));
        } else {
            System.out.printf("TASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printOneEpic(Integer key) {
        if (epicHashMap.containsKey(key)) {
            System.out.println(epicHashMap.get(key));
        } else {
            System.out.printf("EPIC TASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printOneSubtask(Integer key) {
        if (subtaskHashMap.containsKey(key)) {
            System.out.println(subtaskHashMap.get(key));
        } else {
            System.out.printf("SUBTASK WITH ID %d NOT FOUND\n", key);
        }
    }

    public void printAllTask() {
        if (taskHashMap.isEmpty()) {
            System.out.println("TASKS NOT FOUND!");
        }
        for (Task value : taskHashMap.values()) {
            System.out.println(value);
        }
    }

    public void printAllEpic() {
        if (epicHashMap.isEmpty()) {
            System.out.println("EPIC TASKS NOT FOUND!");
        }
        for (Epic value : epicHashMap.values()) {
            System.out.println(value);
        }
    }

    public void printAllSubtask() {
        if (subtaskHashMap.isEmpty()) {
            System.out.println("SUBTASKS NOT FOUND!");
        }
        for (Subtask value : subtaskHashMap.values()) {
            System.out.println(value);
        }
    }
}
