package service;

import exception.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String COLUMNS = "id,type,name,status,description,epic";
    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        File file = new File("./src/resources/Kanban.csv");

        System.out.println("\n\u001B[32m" + "TASK_MANAGER 1: CREATE AND GET 7 TASKS + UPDATE THEN DELETE SUBTASK_3 + PRINT_HISTORY " + "\u001B[38m");
        TaskManager taskManager1 = new FileBackedTasksManager(file);
        Task task1 = new Task("task1", "task1 from TaskManager-1");
        taskManager1.createTask(task1);
        Task task2 = new Task("task2", "task2 from TaskManager-1");
        taskManager1.createTask(task2);
        Epic epic1 = new Epic("epic1", "epic1 from TaskManager-1");
        taskManager1.createEpic(epic1);
        Epic epic2 = new Epic("epic2", "epic2 from TaskManager-1");
        taskManager1.createEpic(epic2);
        Subtask subtask1 = new Subtask("sub1", "sub1 from TaskManager-1", epic1.getId());
        taskManager1.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("sub2", "sub2 from TaskManager-1", epic1.getId());
        taskManager1.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("sub3", "sub3 from TaskManager-1", epic2.getId());
        taskManager1.createSubtask(subtask3);
        taskManager1.getOneTask(task1.getId());
        taskManager1.getOneTask(task2.getId());
        taskManager1.getOneEpic(epic1.getId());
        taskManager1.getOneEpic(epic2.getId());
        taskManager1.getOneSubtask(subtask1.getId());
        taskManager1.getOneSubtask(subtask2.getId());
        taskManager1.getOneSubtask(subtask3.getId());
        taskManager1.getHistory().forEach(System.out::print);
        System.out.println("\n\u001B[32m" + "TASK_MANAGER 1: UPDATE THEN DELETE THEN GET SUBTASK_3 + PRINT_HISTORY " + "\u001B[38m");
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager1.updateSubtask(subtask3);
        taskManager1.deleteOneSubtask(subtask3.getId());
        taskManager1.getHistory().forEach(System.out::print);

        System.out.println("\n\u001B[32m" + "TASK_MANAGER 2: CREATE AND GET TWO TASKS + DELETE EPIC_1 + GET TASK_1 + PRINT_HISTORY " + "\u001B[38m");
        TaskManager taskManager2 = FileBackedTasksManager.loadFromFile(file);
        Task task3 = new Task("task3", "task3 from TaskManager-2");
        taskManager2.createTask(task3);
        Epic epic3 = new Epic("epic3", "epic3 from TaskManager-2");
        taskManager2.createEpic(epic3);
        taskManager2.getOneTask(task3.getId());
        taskManager2.getOneEpic(epic3.getId());
        taskManager2.getOneTask(task1.getId());
        taskManager2.deleteOneEpic(taskManager2.getHistory().get(1).getId());
        taskManager2.getHistory().forEach(System.out::print);
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try {
            if (!Files.exists(Paths.get(file.getParent(), file.getName()))) {
                Files.createFile(Paths.get(file.getParent(), file.getName()));
            }
        } catch (IOException exception) {
            System.out.println("Файл " + file.getName() + "не создался.");
            System.out.println(exception.getMessage());
        }
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty()) {
                    line = bufferedReader.readLine();
                    if (!line.isEmpty()) {
                        for (Integer taskId : historyFromString(line)) {
                            fileBackedTasksManager.addTaskToHistory(taskId);
                        }
                    }
                    break;
                } else if (!line.equals(COLUMNS)) {
                    fileBackedTasksManager.putTaskToMap(line);
                }
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Файл " + file.getName() + " не считался!", exception);
        }
        return fileBackedTasksManager;
    }
    public static String historyToString(HistoryManager historyManager) {

        StringBuilder result = new StringBuilder();
        for (Task task : historyManager.getHistory()) {
            result.append(task.getId().toString()).append(",");
        }
        int index = result.lastIndexOf(",");
        if (index > -1) {
            result.deleteCharAt(index);
        }
        return result.toString();
    }

    public static List<Integer> historyFromString(String value) {
        ArrayList<Integer> result = new ArrayList<>();
        String[] temp = value.split(",");
        for (String str : temp) {
            result.add(Integer.parseInt(str));
        }
        return result;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Task getOneTask(Integer key) {
        Task task = tasks.get(key);
        historyManager.add(task);
        save();
        return task;
    }

    @Override
    public Epic getOneEpic(Integer key) {
        Epic epic = epics.get(key);
        historyManager.add(epic);
        save();
        return epic;
    }

    @Override
    public Subtask getOneSubtask(Integer key) {
        Subtask subtask = subtasks.get(key);
        historyManager.add(subtask);
        save();
        return subtask;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteOneTask(Integer key) {
        super.deleteOneTask(key);
        save();
    }

    @Override
    public void deleteOneEpic(Integer key) {
        super.deleteOneEpic(key);
        save();
    }

    @Override
    public void deleteOneSubtask(Integer key) {
        super.deleteOneSubtask(key);
        save();
    }

    private void addTaskToHistory(Integer taskId) {
        if (tasks.containsKey(taskId)) {
            historyManager.add(tasks.get(taskId));
        } else if (epics.containsKey(taskId)) {
            historyManager.add(epics.get(taskId));
        } else {
            historyManager.add(subtasks.get(taskId));
        }
    }

    private void putTaskToMap(String line) {
        String[] split = line.split(",");
        Type taskType = Type.valueOf(split[1]);
        switch (taskType) {
            case TASK:
                Task task = fromString(line);
                tasks.put(task.getId(), task);
                break;
            case EPIC:
                Epic epic = (Epic) fromString(line);
                epics.put(epic.getId(), epic);
                break;
            default:
                Subtask subtask = (Subtask) fromString(line);
                subtasks.put(subtask.getId(), subtask);
                Epic epic1 = epics.get(subtask.getEpicId());
                epic1.getSubtasksIds().add(subtask.getId());
                epic1.setStatus(checkStatus(epic1));
                break;
        }
    }

    private Task fromString(String value) {
        String[] split = value.split(",");
        Integer taskId = Integer.parseInt(split[0]);
        if (taskId > id) {
            id = taskId;
        }
        Type taskType = Type.valueOf(split[1]);
        String taskName = split[2];
        Status taskStatus = Status.valueOf(split[3]);
        String taskDescription = split[4];
        switch (taskType) {
            case TASK:
                Task task = new Task(taskName, taskDescription);
                task.setId(taskId);
                task.setStatus(taskStatus);
                return task;
            case EPIC:
                Epic epic = new Epic(taskName, taskDescription);
                epic.setId(taskId);
                epic.setStatus(taskStatus);
                return epic;
            default:
                Subtask subtask = new Subtask(taskName, taskDescription, Integer.parseInt(split[5]));
                subtask.setId(taskId);
                subtask.setStatus(taskStatus);
                return subtask;
        }
    }

    private void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(COLUMNS + "\n");
            for (Task task : tasks.values()) {
                bufferedWriter.write(task.toString());
            }
            for (Epic epic : epics.values()) {
                bufferedWriter.write(epic.toString());
            }
            for (Subtask subtask : subtasks.values()) {
                bufferedWriter.write(subtask.toString());
            }
            String history = historyToString(historyManager);
            if (!history.isEmpty()) {
                bufferedWriter.write("\n");
                bufferedWriter.write(history);
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
    }
}
