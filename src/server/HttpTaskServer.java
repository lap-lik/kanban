package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import exception.TaskManagerException;
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import static constants.Constants.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private final TaskManager taskManager;
    private final HttpServer taskServer;
    private final Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        taskServer = HttpServer.create(new InetSocketAddress(HOSTNAME, PORT_CLIENT), 0);
        taskServer.createContext("/tasks", this::TasksHandler);
        gson = GSON;
    }

    public void TasksHandler(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    getMethods(exchange);
                    break;
                case "POST":
                    postMethods(exchange);
                    break;
                case "DELETE":
                    deleteMethods(exchange);
                    break;
                default:
                    sendText(exchange, "Ошибка запроса.", 405);
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    public void start() {
        System.out.println("Запущен сервер на порту " + PORT_CLIENT);
        System.out.println("Доступен в браузере по ссылке " + URL_CLIENT + PORT_CLIENT + "/");
        taskServer.start();
    }

    public void stop() {
        taskServer.stop(0);
        System.out.println("\nСервер на порту " + PORT_CLIENT + " остановлен.");
    }

    private void getMethods(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = exchange.getRequestURI().getQuery();
            if (pathId == null) {
                switch (path) {
                    case "/tasks/":
                        sendJsonResponse(exchange, taskManager.getPrioritizedTasks());
                        break;
                    case "/tasks/task/":
                        sendJsonResponse(exchange, taskManager.getAllTasks());
                        break;
                    case "/tasks/subtask/":
                        sendJsonResponse(exchange, taskManager.getAllSubtasks());
                        break;
                    case "/tasks/epic/":
                        sendJsonResponse(exchange, taskManager.getAllEpics());
                        break;
                    case "/tasks/history/":
                        sendJsonResponse(exchange, taskManager.getHistory());
                        break;
                    default:
                        sendText(exchange, "Ошибка запроса.", 405);
                }
            } else {
                int taskId = parsePathId(pathId);
                if (taskId >= 0) {
                    switch (path) {
                        case "/tasks/subtask/epic/":
                            sendJsonResponse(exchange, taskManager.getAllSubtasksByEpicId(taskId));
                            break;
                        case "/tasks/task/":
                            sendJsonResponse(exchange, taskManager.getOneTask(taskId));
                            break;
                        case "/tasks/subtask/":
                            sendJsonResponse(exchange, taskManager.getOneSubtask(taskId));
                            break;
                        case "/tasks/epic/":
                            sendJsonResponse(exchange, taskManager.getOneEpic(taskId));
                            break;
                        default:
                            sendText(exchange, "Ошибка запроса.", 405);
                    }
                } else {
                    sendText(exchange, "Ошибка запроса.", 405);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void postMethods(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String value = new String(exchange.getRequestBody().readAllBytes(), UTF_8);
            if (!value.isEmpty()) {
                switch (path) {
                    case "/tasks/task/":
                        handleTask(exchange, value);
                        break;
                    case "/tasks/subtask/":
                        handleSubtask(exchange, value);
                        break;
                    case "/tasks/epic/":
                        handleEpic(exchange, value);
                        break;
                    default:
                        sendText(exchange, "Ошибка запроса.", 404);
                }
            } else {
                sendText(exchange, "Ошибка запроса.", 405);
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void handleTask(HttpExchange exchange, String value) {
        try {
            Task task = gson.fromJson(value, Task.class);
            if (taskManager.getOneTask(task.getId()) != null) {
                try {
                    taskManager.updateTask(task);
                    sendText(exchange, String.format("Задача = %s обновлена.", task.getName()), 200);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            } else {
                try {
                    taskManager.createTask(task);
                    sendText(exchange, String.format("Задача = %s добавлена.", task.getName()), 201);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void handleSubtask(HttpExchange exchange, String value) {
        try {
            Subtask subtask = gson.fromJson(value, Subtask.class);
            if (taskManager.getOneSubtask(subtask.getId()) != null) {
                try {
                    taskManager.updateSubtask(subtask);
                    sendText(exchange, String.format("Подзадача = %s обновлена.", subtask.getName()), 200);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            } else {
                try {
                    taskManager.createSubtask(subtask);
                    sendText(exchange, String.format("Подзадача = %s добавлена.", subtask.getName()), 201);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void handleEpic(HttpExchange exchange, String value) {
        try {
            Epic epic = gson.fromJson(value, Epic.class);
            if (taskManager.getOneEpic(epic.getId()) != null) {
                try {
                    taskManager.updateEpic(epic);
                    sendText(exchange, String.format("Большая задача = %s обновлена.", epic.getName()), 200);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            } else {
                try {
                    taskManager.createEpic(epic);
                    sendText(exchange, String.format("Большая задача = %s добавлена.", epic.getName()), 201);
                } catch (TaskManagerException e) {
                    sendText(exchange, e.getMessage(), 405);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void deleteMethods(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String pathId = exchange.getRequestURI().getQuery();
            if (pathId == null) {
                switch (path) {
                    case "/tasks/task/":
                        taskManager.deleteAllTasks();
                        sendText(exchange, "Все задачи удалены.", 200);
                        break;
                    case "/tasks/subtask/":
                        taskManager.deleteAllSubtasks();
                        sendText(exchange, "Все подзадачи удалены.", 200);
                        break;
                    case "/tasks/epic/":
                        taskManager.deleteAllEpics();
                        sendText(exchange, "Все большие задачи удалены.", 200);
                        break;
                    default:
                        sendText(exchange, "Ошибка запроса.", 405);
                }
            } else {
                int taskId = parsePathId(pathId);
                if (taskId >= 0) {
                    switch (path) {
                        case "/tasks/task/":
                            try {
                                taskManager.deleteOneTask(taskId);
                                sendText(exchange, "Удалена задача с ID = " + taskId, 200);
                            } catch (TaskManagerException e) {
                                sendText(exchange, e.getMessage(), 405);
                            }
                            break;
                        case "/tasks/subtask/":
                            try {
                                taskManager.deleteOneSubtask(taskId);
                                sendText(exchange, "Удалена подзадача с ID = " + taskId, 200);
                            } catch (TaskManagerException e) {
                                sendText(exchange, e.getMessage(), 405);
                            }
                            break;
                        case "/tasks/epic/":
                            try {
                                taskManager.deleteOneEpic(taskId);
                                sendText(exchange, "Удалена большая задача с ID = " + taskId, 200);
                            } catch (TaskManagerException e) {
                                sendText(exchange, e.getMessage(), 405);
                            }
                            break;
                        default:
                            sendText(exchange, "Ошибка запроса.", 405);
                    }
                } else {
                    sendText(exchange, "Ошибка запроса.", 405);
                }
            }
        } catch (IOException exception) {
            System.err.println(exception.getMessage());
        } finally {
            exchange.close();
        }
    }

    private void sendText(HttpExchange exchange, String text, int code) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, resp.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(resp);
        } finally {
            exchange.close();
        }
    }

    private int parsePathId(String pathId) {
        try {
            return Integer.parseInt(pathId.replaceFirst("id=", ""));
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    private void sendJsonResponse(HttpExchange exchange, Object response) throws IOException {
        if (response == null) {
            sendText(exchange, "Ошибка запроса.", 405);
        } else {
            String jsonResponse = gson.toJson(response);
            sendText(exchange, jsonResponse, 200);
        }
    }
}