package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }


    @Test
    @Override
    void updateTask_STANDART() {
        super.updateTask_STANDART();
        assertEquals(5, taskManager.getIntervalGrid().size(),
                "3 - Неверное количество занятых ячеек в DataPlanner.");
    }

    @Test
    void updateSubtask_STANDART() {
        super.updateSubtask_STANDART();
        assertEquals(5, taskManager.getIntervalGrid().size(),
                "3 - Неверное количество занятых ячеек в DataPlanner.");
    }
}
