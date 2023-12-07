package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static constants.Constants.GSON;
import static org.junit.jupiter.api.Assertions.*;


class HttpTaskServerTest {
    private HttpTaskServer httpTaskServer;
    private TaskManager taskManager;
    private Task task1;
    private Epic epic1;
    private Subtask subtask1;
    private KVServer kvServer;
    public final static Integer INVALID_TASK_ID = 13;

    private final Gson gson = GSON;

    @BeforeEach
    void beforeEach() throws IOException {
        kvServer = new KVServer();
        kvServer.start();

        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    void setUp() {
        task1 = new Task("TASK-1", "TASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task1);

        epic1 = new Epic("EPIC-1", "EPIC-1-DESCRIPTION");
        taskManager.createEpic(epic1);

        subtask1 = new Subtask("SUBTASK-1", "SUBTASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 15), Duration.ofMinutes(30),
                epic1.getId());
        taskManager.createSubtask(subtask1);
    }

    @AfterEach
    void afterEach() {
        httpTaskServer.stop();
        kvServer.stop();
    }


    @Test
    void createTask_STANDART() throws IOException, InterruptedException {
        Task task = new Task("TASK", "TASK-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 15), Duration.ofMinutes(30));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа должен быть - 201.");
        assertEquals("Задача = TASK добавлена.", response.body(), "Неверное тело ответа.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getAllTasks().get(0).getId(), "ID созданной задачи неверный.");
    }

    @Test
    void createEpic_STANDART() throws IOException, InterruptedException {
        Epic epic = new Epic("EPIC", "EPIC-DESCRIPTION");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа должен быть - 201.");
        assertEquals("Большая задача = EPIC добавлена.", response.body(), "Неверное тело ответа.");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество больших задач.");
        assertEquals(1, taskManager.getAllEpics().get(0).getId(), "ID созданной большой задачи неверный.");
    }

    @Test
    void createSubtask_STANDART() throws IOException, InterruptedException {
        setUp();
        Subtask subtask = new Subtask("SUBTASK", "SUBTASK-DESCRIPTION",
                LocalDateTime.of(2024, 2, 2, 10, 15), Duration.ofMinutes(30),
                epic1.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код ответа должен быть - 201.");
        assertEquals("Подзадача = SUBTASK добавлена.", response.body(), "Неверное тело ответа.");
        assertEquals(2, taskManager.getAllSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(4, taskManager.getAllSubtasks().get(1).getId(), "ID созданной подзадачи неверный.");
    }

    @Test
    void updateTask_STANDART() throws IOException, InterruptedException {
        setUp();
        Task task = taskManager.getOneTask(task1.getId());
        task.setName("TASK-UPDATE");
        task.setDescription("TASK-UPDATE-DESCRIPTION");
        task.setStatus(Status.IN_PROGRESS);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Задача = TASK-UPDATE обновлена.", response.body(), "Неверное тело ответа.");
        assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
        assertEquals(1, taskManager.getAllTasks().get(0).getId(), "ID обновленной задачи неверный.");
    }

    @Test
    void updateEpic_STANDART() throws IOException, InterruptedException {
        setUp();
        Epic epic = taskManager.getOneEpic(epic1.getId());
        epic.setName("EPIC-UPDATE");
        epic.setDescription("EPIC-UPDATE-DESCRIPTION");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Большая задача = EPIC-UPDATE обновлена.", response.body(),
                "Неверное тело ответа.");
        assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество больших задач.");
        assertEquals(2, taskManager.getAllEpics().get(0).getId(),
                "ID обновленной большой задачи неверный.");
    }

    @Test
    void updateSubtask_STANDART() throws IOException, InterruptedException {
        setUp();
        Subtask subtask = taskManager.getOneSubtask(subtask1.getId());
        subtask.setName("SUBTASK-UPDATE");
        subtask.setDescription("SUBTASK-UPDATE-DESCRIPTION");
        subtask.setStatus(Status.IN_PROGRESS);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Подзадача = SUBTASK-UPDATE обновлена.", response.body(), "Неверное тело ответа.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество подзадач.");
        assertEquals(3, taskManager.getAllSubtasks().get(0).getId(),
                "ID обновленной подзадачи неверный.");
    }

    @Test
    void getAllTasks_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksList = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(tasksList, "Запрос не вернул список задач.");
        assertEquals(taskManager.getAllTasks(), tasksList, "Запрос вернул неправильный список задач.");
    }

    @Test
    void getAllEpics_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type epicType = new TypeToken<List<Epic>>() {
        }.getType();
        List<Epic> epicList = gson.fromJson(response.body(), epicType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(epicList, "Запрос не вернул список больших задач.");
        assertEquals(taskManager.getAllEpics(), epicList, "Запрос вернул неправильный список больших задач.");
    }

    @Test
    void getAllSubtasks_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasksList = gson.fromJson(response.body(), subtaskType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(subtasksList, "Запрос не вернул список подзадач.");
        assertEquals(taskManager.getAllSubtasks(), subtasksList, "Запрос вернул неправильный список подзадач.");
    }

    @Test
    void getOneTask_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task = gson.fromJson(response.body(), Task.class);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(task, "Запрос не вернул задачу.");
        assertEquals(taskManager.getOneTask(1), task, "Запрос вернул неправильную задачу.");
    }

    @Test
    void getOneTask_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=" + INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
    }

    @Test
    void getOneEpic_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(epic, "Запрос не вернул большую задачу.");
        assertEquals(taskManager.getOneEpic(2), epic, "Запрос вернул неправильную большую задачу.");
    }

    @Test
    void getOneEpic_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=" + INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
    }

    @Test
    void getOneSubtask_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtask = gson.fromJson(response.body(), Subtask.class);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(subtask, "Запрос не вернул подзадачу.");
        assertEquals(taskManager.getOneSubtask(3), subtask, "Запрос вернул неправильную подзадачу.");
    }

    @Test
    void getOneSubtask_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=" + INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
    }

    @Test
    void getAllSubtasksByEpicId_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type subtaskType = new TypeToken<List<Subtask>>() {
        }.getType();
        List<Subtask> subtasksList = gson.fromJson(response.body(), subtaskType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(subtasksList, "Запрос не вернул список подзадач.");
        assertEquals(taskManager.getAllSubtasksByEpicId(2), subtasksList, "Запрос вернул неправильный список подзадач.");
    }

    @Test
    void getAllSubtasksByEpicId_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/epic/?id=" + INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
    }

    @Test
    void getHistory_STANDART() throws IOException, InterruptedException {
        setUp();
        taskManager.getOneEpic(epic1.getId());
        taskManager.getOneTask(task1.getId());
        taskManager.getOneSubtask(subtask1.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksList = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(tasksList, "Запрос не вернул список истории.");
        assertEquals(taskManager.getHistory().get(0).getId(), tasksList.get(0).getId(),
                "Запрос вернул неправильный список истории.");
        assertEquals(taskManager.getHistory().get(1).getId(), tasksList.get(1).getId(),
                "Запрос вернул неправильный список истории.");
        assertEquals(taskManager.getHistory().get(2).getId(), tasksList.get(2).getId(),
                "Запрос вернул неправильный список истории.");
    }

    @Test
    void getPrioritizedTasks_STANDART() throws IOException, InterruptedException {
        setUp();
        taskManager.getOneEpic(epic1.getId());
        taskManager.getOneTask(task1.getId());
        taskManager.getOneSubtask(subtask1.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Type taskType = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> tasksList = gson.fromJson(response.body(), taskType);

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertNotNull(tasksList, "Запрос не вернул список приоритетов.");
        assertEquals(taskManager.getPrioritizedTasks().get(0).getId(), tasksList.get(0).getId(),
                "Запрос вернул неправильный список приоритетов.");
        assertEquals(taskManager.getPrioritizedTasks().get(1).getId(), tasksList.get(1).getId(),
                "Запрос вернул неправильный список приоритетов.");
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Все задачи удалены.", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Не удалились все задачи.");
    }

    @Test
    void deleteAllEpics() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Все большие задачи удалены.", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Не удалились все большие задачи.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Не удалились все подзадачи.");
    }

    @Test
    void deleteAllSubtasks() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Все подзадачи удалены.", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Не удалились все подзадачи.");
        assertTrue(taskManager.getOneEpic(epic1.getId()).getSubtasksIds().isEmpty(),
                "Не удалилась связь с подзадачей у большой задачи.");
    }

    @Test
    void deleteOneTask_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Удалена задача с ID = 1", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Задача не удалилась.");
    }
    @Test
    void deleteOneTask_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id="+INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
        assertFalse(taskManager.getAllTasks().isEmpty(), "Задача не удалилась.");
    }

    @Test
    void deleteOneEpic_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id=2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Удалена большая задача с ID = 2", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Большая задача не удалилась.");
    }
    @Test
    void deleteOneEpic_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/epic/?id="+INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
        assertFalse(taskManager.getAllEpics().isEmpty(), "Большая задача не удалилась.");
    }

    @Test
    void deleteOneSubtask_STANDART() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id=3");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код ответа должен быть - 200.");
        assertEquals("Удалена подзадача с ID = 3", response.body(), "Неверное тело ответа.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Подзадача не удалилась.");
    }
    @Test
    void deleteOneSubtask_INVALID_ID() throws IOException, InterruptedException {
        setUp();

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/subtask/?id="+INVALID_TASK_ID);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код ответа должен быть - 405.");
        assertEquals("Ошибка запроса.", response.body(), "Неверное тело ответа.");
        assertFalse(taskManager.getAllSubtasks().isEmpty(), "Подзадача не удалилась.");
    }
}