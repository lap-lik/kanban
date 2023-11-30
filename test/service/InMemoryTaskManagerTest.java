package service;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest<T extends TaskManager> extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }
}
