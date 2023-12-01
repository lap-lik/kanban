package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task1;
    protected Epic epic1;
    protected Subtask subtask1;
    public final static Integer INVALID_TASK_ID = 13;

    void setUp() {
        task1 = new Task("TASK-1", "TASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task1);

        epic1 = new Epic("EPIC-1", "EPIC-1-DESCRIPTION");
        taskManager.createEpic(epic1);

        subtask1 = new Subtask("SUBTASK-1", "SUBTASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 15), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask1);
    }

    @Test
    void createTask_STANDART() {
        setUp();
        final int taskId = task1.getId();
        assertEquals(1, taskId, "Не верный айд");
        final Task savedTask = taskManager.getOneTask(taskId);
        assertNotNull(savedTask, "Задача-1 не найдена.");
        assertEquals(task1, savedTask, "Задача-1 и сохраненная в базе не совпадают.");
        final List<Task> tasks = taskManager.getAllTasks();
        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задача-1 и сохраненная в базе полученная по индексу не совпадают.");
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size(), "Размер списка приоритетного вывода не корректен");
        Task task2 = new Task("TASK-2", "TASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task2);
        Task task3 = new Task("TASK-3", "TASK-3-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 30), Duration.ofMinutes(30));
        taskManager.createTask(task3);
        Task task4 = new Task("TASK-4", "TASK-4-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 12, 28), Duration.ofMinutes(30));
        taskManager.createTask(task4);
        Task task5 = new Task("TASK-5", "TASK-5-DESCRIPTION",
                LocalDateTime.of(2025, 1, 1, 12, 31), Duration.ofMinutes(12));
        taskManager.createTask(task5);
        final List<Task> tasks2 = taskManager.getAllTasks();
        assertNotNull(tasks2, "Задачи на возвращаются.");
        assertEquals(2, tasks2.size(), "Неверное количество задач.");
        assertEquals(task1, tasks2.get(0), "Задача-1 и сохраненная в базе полученная по индексу не совпадают.");
        assertEquals(task4, tasks2.get(1), "Задача-4 и сохраненная в базе полученная по индексу не совпадают.");
    }

    @Test
    void createTask_NULL() {
        taskManager.createTask(null);
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void createEpic_STANDART() {
        Epic epic1 = new Epic("EPIC-1", "EPIC-1-DESCRIPTION");
        taskManager.createEpic(epic1);

        final int epicId = epic1.getId();
        assertEquals(1, epicId);
        final Epic savedEpic = taskManager.getOneEpic(epicId);
        assertNotNull(savedEpic, "Эпик-1 не найдена.");
        assertEquals(epic1, savedEpic, "Эпик-1 и сохраненная в базе не совпадают.");
        final List<Epic> epics = taskManager.getAllEpics();
        assertNotNull(epics, "Эпики на возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество эпиков.");
        assertEquals(epic1, epics.get(0), "Эпик-1 и сохраненная в базе полученная по индексу не совпадают.");
    }

    @Test
    void createEpic_NULL() {
        taskManager.createEpic(null);
        assertTrue(taskManager.getAllEpics().isEmpty());
    }

    @Test
    void createSubtask_STANDART() {
        setUp();
        final int subtaskId = subtask1.getId();
        assertEquals(3, subtaskId, "Не верный айд");
        final Subtask savedSubtask = taskManager.getOneSubtask(subtaskId);
        assertNotNull(savedSubtask, "Подзадача-1 не найдена.");
        assertEquals(subtask1, savedSubtask, "Подзадача-1 и сохраненная в базе не совпадают.");
        final List<Subtask> subtasks1 = taskManager.getAllSubtasks();
        assertNotNull(subtasks1, "Подзадачи на возвращаются.");
        assertEquals(1, subtasks1.size(), "Неверное количество задач.");
        assertEquals(subtask1, subtasks1.get(0), "Подзадача-1 и сохраненная в базе полученная по индексу не совпадают.");
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        assertEquals(2, prioritizedTasks.size(), "Размер списка приоритетного вывода не корректен");
        Subtask subtask2 = new Subtask("SUBTASK-2", "SUBTASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 15), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("SUBTASK-3", "SUBTASK-3-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 30), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask3);
        Subtask subtask4 = new Subtask("SUBTASK-4", "SUBTASK-4-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 12, 28), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask4);
        final List<Subtask> subtasks2 = taskManager.getAllSubtasks();
        assertNotNull(subtask4, "Подзадачи на возвращаются.");
        assertEquals(2, subtasks2.size(), "Неверное количество подзадач.");
        assertEquals(subtask1, subtasks2.get(0), "Подзадача-1 и сохраненная в базе полученная по индексу не совпадают.");
        assertEquals(subtask4, subtasks2.get(1), "Подзадача-4 и сохраненная в базе полученная по индексу не совпадают.");
    }

    @Test
    void createSubtask_NULL() {
        taskManager.createSubtask(null);
        assertTrue(taskManager.getAllSubtasks().isEmpty());
    }

    @Test
    void updateTask_STANDART() {
        setUp();
        final int taskId = task1.getId();
        assertEquals(1, taskId, "Не верный айд");
        final Task savedTask1 = taskManager.getOneTask(taskId);
        assertNotNull(savedTask1, "Задача-1 не найдена.");
        assertEquals(task1, savedTask1, "Задача-1 и сохраненная в базе не совпадают.");

        savedTask1.setName("TASK-1-NEW");
        savedTask1.setDescription("TASK-1-DESCRIPTION-NEW");
        savedTask1.setStatus(Status.IN_PROGRESS);
        savedTask1.setStartTime(LocalDateTime.of(2024, 1, 2, 10, 15));
        savedTask1.setDuration(Duration.ofMinutes(40));
        taskManager.updateTask(savedTask1);
        String checkTask1ToString = "1,TASK,TASK-1-NEW,IN_PROGRESS,TASK-1-DESCRIPTION-NEW,10:15(02-01-2024),40,\n";
        assertEquals(1, taskManager.getAllTasks().size(), "1 - Неверное количество задач.");
        assertEquals(checkTask1ToString, taskManager.getAllTasks().get(0).toString(),
                "Текстовое представление задачи-1 и обновленная в базе не совпадают.");
        assertEquals(savedTask1, taskManager.getPrioritizedTasks().get(0));

        final Task savedTask2 = taskManager.getOneTask(taskId);
        savedTask2.setStatus(Status.DONE);
        savedTask2.setStartTime(LocalDateTime.of(2024, 1, 2, 11, 1));
        taskManager.updateTask(savedTask2);
        assertEquals(1, taskManager.getAllTasks().size(), "2 - Неверное количество задач.");
        assertEquals(Status.DONE, taskManager.getAllTasks().get(0).getStatus(),
                "2 - Статус у обновленной задачи-1 не совпадает.");

        final Task savedTask3 = taskManager.getOneTask(taskId);
        savedTask3.setStatus(Status.NEW);
        savedTask3.setStartTime(LocalDateTime.of(2024, 3, 2, 11, 1));
        taskManager.updateTask(savedTask3);
        assertEquals(1, taskManager.getAllTasks().size(), "3 - Неверное количество задач.");
        assertEquals(Status.NEW, taskManager.getAllTasks().get(0).getStatus(),
                "3 - Статус у обновленной задачи-1 не совпадает.");
    }

    @Test
    void updateTask_EMPTY_MAP() {
        Task task2 = new Task("WW", "QQ");
        task2.setId(1);
        task2.setName("TASK-1-NEW");
        task2.setDescription("TASK-1-DESCRIPTION-NEW");
        task2.setStatus(Status.IN_PROGRESS);
        task2.setStartTime(LocalDateTime.of(2024, 1, 2, 10, 15));
        task2.setDuration(Duration.ofMinutes(40));
        taskManager.updateTask(task2);
        assertTrue(taskManager.getAllTasks().isEmpty(), "Ошибка обновления задачи в пустой базе.");
    }

    @Test
    void updateTask_INVALID_ID() {
        setUp();
        task1.setId(INVALID_TASK_ID);
        task1.setName("TASK-1-NEW");
        task1.setDescription("TASK-1-DESCRIPTION-NEW");
        task1.setStatus(Status.IN_PROGRESS);
        task1.setStartTime(LocalDateTime.of(2024, 1, 2, 10, 15));
        task1.setDuration(Duration.ofMinutes(40));
        taskManager.updateTask(task1);
        String checkTask1ToString = "1,TASK,TASK-1,NEW,TASK-1-DESCRIPTION,10:15(01-01-2024),30,\n";
        assertEquals(checkTask1ToString, taskManager.getAllTasks().get(0).toString(),
                "Текстовое представление задачи-1 и обновленная в базе не совпадают.");
    }

    @Test
    void updateSubtask_STANDART() {
        setUp();
        final int subtaskId = subtask1.getId();
        assertEquals(3, subtaskId, "Не верный айд");
        final Subtask savedSubtask1 = taskManager.getOneSubtask(subtaskId);
        assertNotNull(savedSubtask1, "Подзадача-1 не найдена.");
        assertEquals(subtask1, savedSubtask1, "Подзадача-1 и сохраненная в базе не совпадают.");

        savedSubtask1.setName("SUBTASK-1-NEW");
        savedSubtask1.setDescription("SUBTASK-1-DESCRIPTION-NEW");
        savedSubtask1.setStatus(Status.IN_PROGRESS);
        savedSubtask1.setStartTime(LocalDateTime.of(2024, 1, 2, 10, 15));
        savedSubtask1.setDuration(Duration.ofMinutes(40));
        taskManager.updateSubtask(savedSubtask1);
        String checkSubtask1ToString = "3,SUBTASK,SUBTASK-1-NEW,IN_PROGRESS,SUBTASK-1-DESCRIPTION-NEW,10:15(02-01-2024),40,2\n";
        assertEquals(1, taskManager.getAllSubtasks().size(), "1 - Неверное количество подзадач.");
        assertEquals(checkSubtask1ToString, taskManager.getAllSubtasks().get(0).toString(),
                "Текстовое представление подзадачи-1 и обновленная в базе не совпадают.");
        assertEquals(savedSubtask1, taskManager.getPrioritizedTasks().get(1));

        final Subtask savedSubtask2 = taskManager.getOneSubtask(subtaskId);
        savedSubtask2.setStatus(Status.DONE);
        savedSubtask2.setStartTime(LocalDateTime.of(2024, 1, 2, 11, 1));
        taskManager.updateSubtask(savedSubtask2);
        assertEquals(1, taskManager.getAllSubtasks().size(), "2 - Неверное количество подзадач.");
        assertEquals(Status.DONE, taskManager.getAllSubtasks().get(0).getStatus(),
                "2 - Статус у обновленной подзадачи-1 не совпадает.");

        final Subtask savedSubtask3 = taskManager.getOneSubtask(subtaskId);
        savedSubtask3.setStatus(Status.NEW);
        savedSubtask3.setStartTime(LocalDateTime.of(2024, 3, 2, 11, 1));
        taskManager.updateSubtask(savedSubtask3);
        assertEquals(1, taskManager.getAllSubtasks().size(), "3 - Неверное количество подзадач.");
        assertEquals(Status.NEW, taskManager.getAllSubtasks().get(0).getStatus(),
                "3 - Статус у обновленной подзадачи-1 не совпадает.");
    }

    @Test
    void updateSubtask_INVALID_ID() {
        setUp();
        subtask1.setId(INVALID_TASK_ID);
        subtask1.setEpicId(2);
        subtask1.setName("SUBTASK-1-NEW");
        subtask1.setDescription("SUBTASK-1-DESCRIPTION-NEW");
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask1.setStartTime(LocalDateTime.of(2024, 1, 2, 10, 15));
        subtask1.setDuration(Duration.ofMinutes(40));
        taskManager.updateTask(subtask1);
        String checkSubtask1ToString = "3,SUBTASK,SUBTASK-1,NEW,SUBTASK-1-DESCRIPTION,10:15(01-02-2024),30,2\n";
        assertEquals(checkSubtask1ToString, taskManager.getAllSubtasks().get(0).toString(),
                "Текстовое представление задачи-1 и обновленная в базе не совпадают.");
    }

    @Test
    void updateEpic_STANDART() {
        setUp();
        final int epicId = epic1.getId();
        assertEquals(2, epicId, "Не верный айд");

        final Epic savedEpic1 = taskManager.getOneEpic(epicId);
        assertNotNull(savedEpic1, "Эпик-1 не найден.");

        savedEpic1.setName("EPIC-1-NEW");
        savedEpic1.setDescription("EPIC-1-DESCRIPTION-NEW");
        savedEpic1.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(savedEpic1);
        final String checkUpdateEpic1ToString = "2,EPIC,EPIC-1-NEW,NEW,EPIC-1-DESCRIPTION-NEW,10:15(01-02-2024),30,\n";

        assertEquals(1, taskManager.getAllEpics().size(), "1 - Неверное количество эпиков.");
        assertEquals(checkUpdateEpic1ToString, taskManager.getAllEpics().get(0).toString(),
                "Текстовое представление эпика-1 и обновленная в базе не совпадают.");

        Subtask subtask2 = new Subtask("SUBTASK-2", "SUBTASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 2, 2, 10, 15), Duration.ofMinutes(30), epicId);
        taskManager.createSubtask(subtask2);
        assertEquals(Status.NEW, taskManager.getOneEpic(epicId).getStatus(),
                "После добавления сабстаски со статусом NEW, изменился статус Эпика-1");

        Subtask updateSubtask2 = taskManager.getOneSubtask(subtask2.getId());
        updateSubtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(updateSubtask2);
        assertEquals(Status.IN_PROGRESS, taskManager.getOneEpic(epicId).getStatus(),
                "После изменения статуса сабстаски-2 на IN_PROGRESS, не изменился статус Эпика-1");

        updateSubtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(updateSubtask2);
        assertEquals(Status.IN_PROGRESS, taskManager.getOneEpic(epicId).getStatus(),
                "После изменения статуса сабстаски-2 на DONE, изменился статус Эпика-1");

        Subtask updateSubtask1 = taskManager.getOneSubtask(subtask1.getId());
        updateSubtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(updateSubtask1);
        assertEquals(Status.DONE, taskManager.getOneEpic(epicId).getStatus(),
                "После изменения статуса сабстаски-1 на DONE, не изменился статус Эпика-1");
    }

    @Test
    void updateEpic_EMPTY_MAP() {
        Epic epic2 = new Epic("WW", "QQ");
        epic2.setId(2);
        epic2.setName("EPIC-1-NEW");
        epic2.setDescription("EPIC-1-DESCRIPTION-NEW");
        taskManager.updateEpic(epic2);
        assertTrue(taskManager.getAllEpics().isEmpty(), "Ошибка обновления эпика в пустой базе.");
    }

    @Test
    void updateEpic_INVALID_ID() {
        setUp();
        epic1.setId(INVALID_TASK_ID);
        epic1.setName("EPIC-1-NEW");
        epic1.setDescription("EPIC-1-DESCRIPTION-NEW");
        taskManager.updateEpic(epic1);
        String checkEpic1ToString = "2,EPIC,EPIC-1,NEW,EPIC-1-DESCRIPTION,10:15(01-02-2024),30,\n";
        assertEquals(1, taskManager.getAllEpics().size(),
                "После обновления эпика по ошибочному ID количество эпиков в базе увеличилось.");
        assertEquals(checkEpic1ToString, taskManager.getAllEpics().get(0).toString(),
                "После обновления эпика по ошибочному ID, эпик в базе изменился.");
    }

    @Test
    void getAllTasks_STANDART() {
        setUp();
        List<Task> checkListTasks = taskManager.getAllTasks();
        assertEquals(1, checkListTasks.size(),
                "1 - Из базы возвращается неверное количество задач.");
        Task task2 = new Task("TASK-2", "TASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 3, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task2);
        checkListTasks = taskManager.getAllTasks();
        assertEquals(2, checkListTasks.size(),
                "2 - Из базы возвращается неверное количество задач.");
    }

    @Test
    void getAllTasks_EMPTY_MAP() {
        List<Task> checkListTasks = taskManager.getAllTasks();
        assertEquals(0, checkListTasks.size(),
                "Из базы возвращается неверное количество задач.");
    }

    @Test
    void getAllEpics_STANDART() {
        setUp();
        List<Epic> checkListEpics = taskManager.getAllEpics();
        assertEquals(1, checkListEpics.size(),
                "1 - Из базы возвращается неверное количество эпиков.");
        Epic epic2 = new Epic("EPIC-2", "EPIC-2-DESCRIPTION");
        taskManager.createEpic(epic2);
        checkListEpics = taskManager.getAllEpics();
        assertEquals(2, checkListEpics.size(),
                "2 - Из базы возвращается не верное количество эпиков.");
    }

    @Test
    void getAllEpics_EMPTY_MAP() {
        List<Epic> checkListEpics = taskManager.getAllEpics();
        assertEquals(0, checkListEpics.size(),
                "Из базы возвращается не верное количество эпиков.");
    }

    @Test
    void getAllSubtasks_STANDART() {
        setUp();
        List<Subtask> checkListSubtasks = taskManager.getAllSubtasks();
        assertEquals(1, checkListSubtasks.size(),
                "1 - Из базы возвращается не верное количество сабтасков.");
        Subtask subtask2 = new Subtask("SUBTASK-1", "SUBTASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 4, 1, 10, 15), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask2);
        checkListSubtasks = taskManager.getAllSubtasks();
        assertEquals(2, checkListSubtasks.size(),
                "2 - Из базы возвращается не верное количество сабтасков.");
    }

    @Test
    void getAllSubtasks_EMPTY_MAP() {
        List<Subtask> checkListSubtasks = taskManager.getAllSubtasks();
        assertEquals(0, checkListSubtasks.size(),
                "Из базы возвращается не верное количество сабтасков.");
    }


    @Test
    void getOneTask_STANDART() {
        setUp();
        assertEquals(task1, taskManager.getOneTask(1), "Из базы возвращается не верная задача-1.");
        assertEquals(1, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneTask_EMPTY_MAP() {
        assertNull(taskManager.getOneTask(1), "Из пустой базы возвращается задача-1.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneTask_INVALID_ID() {
        setUp();
        assertNull(taskManager.getOneTask(INVALID_TASK_ID), "Из базы возвращается задача по неправильному ID.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneEpic_STANDART() {
        setUp();
        assertEquals(epic1.getName(), taskManager.getOneEpic(2).getName(), "1 - Из базы возвращается не верный эпик-1.");
        assertEquals(epic1.getDescription(), taskManager.getOneEpic(2).getDescription(),
                "1 - Из базы возвращается не верный эпик-1.");
        assertEquals(1, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneEpic_EMPTY_MAP() {
        assertNull(taskManager.getOneEpic(2), "Из пустой базы возвращается эпик-1.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneEpic_INVALID_ID() {
        setUp();
        assertNull(taskManager.getOneEpic(INVALID_TASK_ID), "Из базы возвращается эпик по неправильному ID.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneSubtask_STANDART() {
        setUp();
        assertEquals(subtask1, taskManager.getOneSubtask(3), "Из базы возвращается не верная подзадача-1.");
        assertEquals(1, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneSubtask_EMPTY_MAP() {
        assertNull(taskManager.getOneSubtask(3), "Из пустой базы возвращается подзадача-1.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getOneSubtask_INVALID_ID() {
        setUp();
        assertNull(taskManager.getOneSubtask(INVALID_TASK_ID), "Из базы возвращается подзадача по неправильному ID.");
        assertEquals(0, taskManager.getHistory().size(), "Количество задач в истории не совпадает.");
    }

    @Test
    void getAllSubtasksByEpicId_STANDART() {
        setUp();
        final Integer epic1Id = epic1.getId();
        final List<Subtask> subtasksIds1 = taskManager.getAllSubtasksByEpicId(epic1Id);
        assertEquals(1, subtasksIds1.size(), "1 - Возвращается не верное количество ID подзадач.");
        Subtask subtask2 = new Subtask("SUBTASK-2", "SUBTASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 2, 2, 10, 15), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask2);
        final List<Subtask> subtasksIds2 = taskManager.getAllSubtasksByEpicId(epic1Id);
        assertEquals(2, subtasksIds2.size(), "2 - Возвращается не верное количество ID подзадач.");
    }

    @Test
    void getAllSubtasksByEpicId_EMPTY_MAP() {
        final Integer epic1Id = 2;
        final List<Subtask> subtasksIds1 = taskManager.getAllSubtasksByEpicId(epic1Id);
        assertEquals(0, subtasksIds1.size(), "1 - Возвращается не верное количество ID подзадач.");
    }

    @Test
    void getAllSubtasksByEpicId_INVALID_ID() {
        final List<Subtask> subtasksIds1 = taskManager.getAllSubtasksByEpicId(INVALID_TASK_ID);
        assertEquals(0, subtasksIds1.size(), "1 - Возвращается не верное количество ID подзадач.");
    }


    @Test
    void getHistory_STANDART() {
        setUp();
        taskManager.getOneTask(task1.getId());
        taskManager.getOneSubtask(subtask1.getId());
        List<Task> history = List.of(task1, subtask1);
        assertArrayEquals(history.toArray(), taskManager.getHistory().toArray(), "История возвращается неправильно.");
    }

    @Test
    void getHistory_EMPTY_MAP() {
        setUp();
        assertTrue(taskManager.getHistory().isEmpty(), "История не пустая.");
    }

    @Test
    void deleteAllTasks_STANDART() {
        setUp();
        assertEquals(1, taskManager.getAllTasks().size(), "Количество задач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "База задач не пустая.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
    }

    @Test
    void deleteAllEpics_STANDART() {
        setUp();
        assertEquals(1, taskManager.getAllEpics().size(), "Количество эпиков в базе не совпадает.");
        assertEquals(1, taskManager.getAllSubtasks().size(),
                "1 - Количество подзадач в базе не совпадает.");
        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "База эптков не пустая.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "База подзадач не пустая.");
    }

    @Test
    void deleteAllSubtasks_STANDART() {
        setUp();
        final List<Integer> subtasksIds = taskManager.getOneEpic(subtask1.getEpicId()).getSubtasksIds();
        assertEquals(1, taskManager.getAllSubtasks().size(), "Количество подзадач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        assertEquals(1, subtasksIds.size(), "Количество Id подзадач у эпика-1 не совпадает.");
        taskManager.deleteAllSubtasks();
        final List<Integer> subtasksIds2 = taskManager.getOneEpic(subtask1.getEpicId()).getSubtasksIds();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "База подзадач не пустая.");
        assertEquals(1, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
        assertEquals(0, subtasksIds2.size(), "Количество Id подзадач у эпика-1 не совпадает.");
    }
    @Test
    void deleteOneTask11_STANDART() {
        setUp();
        Task task2 = new Task("TASK-2", "TASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 3, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task2);
    }


    @Test
    void deleteOneTask_STANDART() {
        setUp();
        Task task2 = new Task("TASK-2", "TASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 3, 1, 10, 15), Duration.ofMinutes(30));
        taskManager.createTask(task2);

        assertEquals(2, taskManager.getAllTasks().size(), "1 - Количество задач в базе не совпадает.");
        assertEquals(3, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        taskManager.deleteOneTask(task2.getId());
        assertEquals(1, taskManager.getAllTasks().size(), "2 - Количество задач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
    }

    @Test
    void deleteOneTask_EMPTY_MAP() {
        assertTrue(taskManager.getAllTasks().isEmpty(), "1- База задач не пустая.");
        taskManager.deleteOneTask(1);
        assertTrue(taskManager.getAllTasks().isEmpty(), "2 - База задач не пустая.");
    }

    @Test
    void deleteOneTask_INVALID_ID() {
        setUp();
        assertEquals(1, taskManager.getAllTasks().size(), "1 - Количество задач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        taskManager.deleteOneTask(INVALID_TASK_ID);
        assertEquals(1, taskManager.getAllTasks().size(), "2 - Количество задач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
    }

    @Test
    void deleteOneEpic_STANDART() {
        setUp();
        Epic epic2 = new Epic("TASK-2", "TASK-2-DESCRIPTION");
        taskManager.createEpic(epic2);

        assertEquals(2, taskManager.getAllEpics().size(), "1 - Количество задач в базе не совпадает.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "Количество подзадач в базе не совпадает.");
        taskManager.deleteOneEpic(epic1.getId());
        assertEquals(1, taskManager.getAllEpics().size(), "2 - Количество задач в базе не совпадает.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "База подзадач не пустая.");
    }

    @Test
    void deleteOneEpic_EMPTY_MAP() {
        assertTrue(taskManager.getAllEpics().isEmpty(), "1- База задач не пустая.");
        taskManager.deleteOneEpic(2);
        assertTrue(taskManager.getAllEpics().isEmpty(), "2 - База задач не пустая.");
    }

    @Test
    void deleteOneEpic_INVALID_ID() {
        setUp();
        assertEquals(1, taskManager.getAllEpics().size(), "1 - Количество эпиков в базе не совпадает.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "1 - Количество подзадач в базе не совпадает.");
        taskManager.deleteOneEpic(INVALID_TASK_ID);
        assertEquals(1, taskManager.getAllEpics().size(), "2 - Количество эпиков в базе не совпадает.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "1 - Количество подзадач в базе не совпадает.");
    }

    @Test
    void deleteOneSubtask_STANDART() {
        setUp();
        Subtask subtask2 = new Subtask("SUBTASK-2", "SUBTASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 8, 1, 10, 15), Duration.ofMinutes(30), epic1.getId());
        taskManager.createSubtask(subtask2);
        final List<Integer> subtasksIds = taskManager.getOneEpic(subtask1.getEpicId()).getSubtasksIds();
        assertEquals(2, subtasksIds.size(), "1 - Количество Id подзадач у эпика-1 не совпадает.");
        assertEquals(2, taskManager.getAllSubtasks().size(), "1 - Количество подзадач в базе не совпадает.");
        assertEquals(3, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        taskManager.deleteOneSubtask(subtask2.getId());
        final List<Integer> subtasksIds2 = taskManager.getOneEpic(subtask1.getEpicId()).getSubtasksIds();
        assertEquals(1, subtasksIds2.size(), "2 - Количество Id подзадач у эпика-1 не совпадает.");
        assertEquals(1, taskManager.getAllSubtasks().size(), "2 - Количество подзадач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
    }

    @Test
    void deleteOneSubtask_EMPTY_MAP() {
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "1- База подзадач не пустая.");
        taskManager.deleteOneSubtask(3);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "2 - База подзадач не пустая.");
    }

    @Test
    void deleteOneSubtask_INVALID_ID() {
        setUp();
        assertEquals(1, taskManager.getAllSubtasks().size(), "1 - Количество подзадач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "1 - Количество задач в множестве приоритетов не совпадает.");
        taskManager.deleteOneSubtask(INVALID_TASK_ID);
        assertEquals(1, taskManager.getAllSubtasks().size(), "2 - Количество подзадач в базе не совпадает.");
        assertEquals(2, taskManager.getPrioritizedTasks().size(),
                "2 - Количество задач в множестве приоритетов не совпадает.");
    }
}