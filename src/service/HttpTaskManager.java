package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import exception.KVException;
import model.Epic;
import model.Subtask;
import model.Task;
import server.KVTaskClient;

import java.util.List;

import static constants.Constants.*;

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

    @Override
    public void save() {
        try {
            client.put(KV_KYE_TASK, gson.toJson(getAllTasks()));
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_TASK);
        }
        try {
            client.put(KV_KYE_EPIC, gson.toJson(getAllEpics()));
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_EPIC);
        }
        try {
            client.put(KV_KYE_SUBTASK, gson.toJson(getAllSubtasks()));
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_SUBTASK);
        }
        try {
            client.put(KV_KYE_HISTORY, gson.toJson(getHistory()));
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_HISTORY);
        }
    }

    public void loadFromServer() {
        List<Task> tasksFromServer;
        List<Epic> epicsFromServer;
        List<Subtask> subtasksFromServer;
        List<Task> historyFromServer;
        try {
            tasksFromServer = gson.fromJson(client.load(KV_KYE_TASK), new TypeToken<List<Task>>() {
            }.getType());
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_TASK);
            tasksFromServer = null;
        }
        try {
            epicsFromServer = gson.fromJson(client.load(KV_KYE_EPIC), new TypeToken<List<Epic>>() {
            }.getType());
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_EPIC);
            epicsFromServer = null;
        }
        try {
            subtasksFromServer = gson.fromJson(client.load(KV_KYE_SUBTASK), new TypeToken<List<Subtask>>() {
            }.getType());
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_SUBTASK);
            subtasksFromServer = null;
        }
        try {
            historyFromServer = gson.fromJson(client.load(KV_KYE_HISTORY), new TypeToken<List<Task>>() {
            }.getType());
        } catch (KVException e) {
            System.err.printf("%s По ключу - %s.\n", e.getMessage(), KV_KYE_HISTORY);
            historyFromServer = null;
        }
        loadTasks(tasksFromServer);
        loadEpics(epicsFromServer);
        loadSubtasks(subtasksFromServer);
        loadHistory(historyFromServer);
    }

    private void loadTasks(List<Task> tasksFromServer) {
        if (tasksFromServer != null) {
            for (Task task : tasksFromServer) {
                int taskId = task.getId();
                id = Math.max(id, taskId);
                if (task.getStartTime() != null) {
                    dataPlanner.fillCells(task);
                }
                tasks.put(taskId, task);
                prioritizedManager.add(task);
            }
        }
    }

    private void loadEpics(List<Epic> epicsFromServer) {
        if (epicsFromServer != null) {
            for (Epic epic : epicsFromServer) {
                int epicId = epic.getId();
                id = Math.max(id, epicId);
                epics.put(epicId, epic);
            }
        }
    }

    private void loadSubtasks(List<Subtask> subtasksFromServer) {
        if (subtasksFromServer != null) {
            for (Subtask subtask : subtasksFromServer) {
                int subtaskId = subtask.getId();
                id = Math.max(id, subtaskId);
                if (subtask.getStartTime() != null) {
                    dataPlanner.fillCells(subtask);
                }
                subtasks.put(subtaskId, subtask);
                prioritizedManager.add(subtask);
            }
        }
    }

    private void loadHistory(List<Task> historyFromServer) {
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
}