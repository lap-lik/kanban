package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static constants.Constants.COLUMNS;
import static constants.Constants.LOCAL_DATA_TIME_FORMAT;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        if (file.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.isEmpty()) {
                        line = bufferedReader.readLine();
                        if (!line.isEmpty()) {
                            historyFromString(line).forEach(fileBackedTasksManager::addTaskToHistory);
                        }
                        break;
                    }
                    if (!COLUMNS.equals(line)) {
                        fileBackedTasksManager.putTaskToMap(line);
                    }
                }
            } catch (IOException exception) {
                throw new ManagerSaveException("Файл " + file.getName() + " не считался!", exception);
            }
        }
        return fileBackedTasksManager;
    }

    public static String historyToString(HistoryManager historyManager) {
        return historyManager.getHistory().stream()
                .map(task -> task.getId().toString())
                .collect(Collectors.joining(","));
    }

    public static List<Integer> historyFromString(String value) {
        String[] temp = value.split(",");
        return Arrays.stream(temp)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
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
        Task task = super.getOneTask(key);
        save();
        return task;
    }

    @Override
    public Epic getOneEpic(Integer key) {
        Epic epic = super.getOneEpic(key);
        save();
        return epic;
    }

    @Override
    public Subtask getOneSubtask(Integer key) {
        Subtask subtask = super.getOneSubtask(key);
        save();
        return subtask;
    }

    @Override
    public List<Subtask> getAllSubtasksByEpicId(Integer key) {
        List<Subtask> subtasksByEpic = super.getAllSubtasksByEpicId(key);
        save();
        return subtasksByEpic;
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
        Task task = tasks.get(taskId);
        Epic epic = epics.get(taskId);
        Subtask subtask = subtasks.get(taskId);
        if (task != null) {
            historyManager.add(task);
        } else if (epic != null) {
            historyManager.add(epic);
        } else {
            historyManager.add(subtask);
        }
    }

    private void putTaskToMap(String line) {
        String[] split = line.split(",");
        Type taskType = Type.valueOf(split[1]);
        switch (taskType) {
            case TASK:
                Task task = fromString(line);
                boolean isTrueCellTask = dataPlanner.fillCells(task);
                if (isTrueCellTask) {
                    tasks.put(task.getId(), task);
                    prioritizedManager.add(task);
                }
                break;
            case EPIC:
                Epic epic = (Epic) fromString(line);
                epics.put(epic.getId(), epic);
                break;
            default:
                Subtask subtask = (Subtask) fromString(line);
                boolean isTrueCellSubtask = dataPlanner.fillCells(subtask);
                if (isTrueCellSubtask) {
                    subtasks.put(subtask.getId(), subtask);
                    prioritizedManager.add(subtask);
                    Epic epic1 = epics.get(subtask.getEpicId());
                    epic1.getSubtasksIds().add(subtask.getId());
                    epic1.setStatus(checkStatus(epic1));
                    updateEpicDateTime(epic1);
                }
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
                Task task = new Task(taskName, taskDescription, dataTimeFromString(split[5]),
                        Duration.ofMinutes(Integer.parseInt(split[6])));
                task.setId(taskId);
                task.setStatus(taskStatus);
                return task;
            case EPIC:
                Epic epic = new Epic(taskName, taskDescription);
                epic.setId(taskId);
                epic.setStatus(taskStatus);
                return epic;
            default:
                Subtask subtask = new Subtask(taskName, taskDescription, dataTimeFromString(split[5]),
                        Duration.ofMinutes(Integer.parseInt(split[6])), Integer.parseInt(split[7]));
                subtask.setId(taskId);
                subtask.setStatus(taskStatus);
                return subtask;
        }
    }

    private LocalDateTime dataTimeFromString(String line) {
        if (line.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(line, LOCAL_DATA_TIME_FORMAT);
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
                bufferedWriter.flush();
            }
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
    }
}
