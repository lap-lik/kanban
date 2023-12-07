package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.util.List;

import static constants.Constants.GSON;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson = GSON;

    public HttpTaskManager(String url) {
        this(url, false);
    }

    public HttpTaskManager(String url, Boolean load) {
        super(null);
        client = new KVTaskClient(url);
        if (load) {
            loadFromServer();
        }
    }

    public void loadFromServer() {
        List<Task> tasksFromServer = gson.fromJson(client.load("/tasks"), new TypeToken<List<Task>>() {
        }.getType());
        if (tasksFromServer != null) {
            for (Task task : tasksFromServer) {
                int taskId = task.getId();
                id = Math.max(id, taskId);
                dataPlanner.fillCells(task);
                tasks.put(taskId, task);
                prioritizedManager.add(task);
            }
        }

        List<Epic> epicsFromServer = gson.fromJson(client.load("/epics"), new TypeToken<List<Epic>>() {
        }.getType());
        if (epicsFromServer != null) {
            for (Epic epic : epicsFromServer) {
                int epicId = epic.getId();
                id = Math.max(id, epicId);
                epics.put(epicId, epic);
            }
        }
        List<Subtask> subtasksFromServer = gson.fromJson(client.load("/subtasks"), new TypeToken<List<Subtask>>() {
        }.getType());
        if (subtasksFromServer != null) {
            for (Subtask subtask : subtasksFromServer) {
                int subtaskId = subtask.getId();
                id = Math.max(id, subtaskId);
                dataPlanner.fillCells(subtask);
                subtasks.put(subtaskId, subtask);
                prioritizedManager.add(subtask);
            }
        }
        List<Task> historyFromServer = gson.fromJson(client.load("/history"), new TypeToken<List<Task>>() {
        }.getType());
        if (historyFromServer != null) {
            for (Task taskFromHistory : historyFromServer) {
                Integer taskId = taskFromHistory.getId();
                Task task = tasks.get(taskId);
                Epic epic = epics.get(taskId);
                Subtask subtask = subtasks.get(taskId);
                if (task != null) {
                    historyManager.add(task);
                } else if (epic != null) {
                    historyManager.add(epic);
                } else if (subtask != null) {
                    historyManager.add(subtask);
                }
            }
        }
    }

    @Override
    public void save() {
        String tasksJson = gson.toJson(getAllTasks());
        client.put("/tasks", tasksJson);
        String epicsJson = gson.toJson(getAllEpics());
        client.put("/epics", epicsJson);
        String subtasksJson = gson.toJson(getAllSubtasks());
        client.put("/subtasks", subtasksJson);

        String historyJson = gson.toJson(getHistory());
        client.put("/history", historyJson);
    }
}
