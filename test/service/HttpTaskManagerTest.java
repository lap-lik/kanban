package service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.KVServer;

import java.io.IOException;

import static constants.Constants.PORT_KV_SERVER;
import static constants.Constants.URL_KV_SERVER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {
    private KVServer kvServer;

    @BeforeEach
    void beforeEach() {
        try {
            kvServer = new KVServer();
            kvServer.start();
            super.taskManager = new HttpTaskManager(URL_KV_SERVER + PORT_KV_SERVER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void afterEach() {
        kvServer.stop();
    }

    @Test
    void loadFromServer() {
        setUp();
        taskManager.getOneEpic(2);
        taskManager.getOneTask(1);
        taskManager.getOneSubtask(3);
        HttpTaskManager httpTaskManager = new HttpTaskManager(URL_KV_SERVER + PORT_KV_SERVER, true);
        assertEquals(taskManager.getAllTasks(), httpTaskManager.getAllTasks(),
                "Задачи с сервера записываются в базу неверно.");
        assertEquals(taskManager.getAllSubtasks(), httpTaskManager.getAllSubtasks(),
                "Подзадачи с сервера записываются в базу неверно.");
        assertEquals(taskManager.getAllEpics(), httpTaskManager.getAllEpics(),
                "Эпики с сервера записываются в базу неверно.");
        assertEquals(taskManager.getPrioritizedTasks(), httpTaskManager.getPrioritizedTasks(),
                "Список приоритетов с сервера записывается в базу неверно.");
        assertEquals(taskManager.getHistory(), httpTaskManager.getHistory(),
                "История с сервера записывается в базу неверно.");
    }
}