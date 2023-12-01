package service;

import exception.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static constants.Constants.FILE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file;

    @BeforeEach
    public void beforeEach() {
        file = new File(FILE_PATH);
        taskManager = new FileBackedTasksManager(file);
    }

    @Override
    @Test
    void createTask_STANDART() {
        super.createTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "1,TASK,TASK-1,NEW,TASK-1-DESCRIPTION,10:15(01-01-2024),30,";
        String myLine2 = "4,TASK,TASK-4,NEW,TASK-4-DESCRIPTION,12:28(01-01-2024),30,";
        assertEquals(myLine1, fileLines.get(1), "Задача-1 записалась в файл не правильно.");
        assertEquals(myLine2, fileLines.get(2), "Задача-4 записалась в файл не правильно.");
    }

    @Override
    @Test
    void createEpic_STANDART() {
        super.createEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "1,EPIC,EPIC-1,NEW,EPIC-1-DESCRIPTION,null,null,";
        assertEquals(myLine1, fileLines.get(1), "Эпик-1 записалась в файл не правильно.");
    }

    @Override
    @Test
    void createSubtask_STANDART() {
        super.createSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "3,SUBTASK,SUBTASK-1,NEW,SUBTASK-1-DESCRIPTION,10:15(01-02-2024),30,2";
        String myLine2 = "4,SUBTASK,SUBTASK-4,NEW,SUBTASK-4-DESCRIPTION,12:28(01-02-2024),30,2";
        assertEquals(myLine1, fileLines.get(3), "Подзадача-1 записалась в файл не правильно.");
        assertEquals(myLine2, fileLines.get(4), "Подзадача-4 записалась в файл не правильно.");
    }

    @Override
    @Test
    void updateTask_STANDART() {
        super.updateTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "1,TASK,TASK-1-NEW,NEW,TASK-1-DESCRIPTION-NEW,11:01(02-03-2024),40,";
        assertEquals(myLine1, fileLines.get(1), "Задача-1 записалась в файл не правильно.");
    }

    @Override
    @Test
    void updateSubtask_STANDART() {
        super.updateSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "3,SUBTASK,SUBTASK-1-NEW,NEW,SUBTASK-1-DESCRIPTION-NEW,11:01(02-03-2024),40,2";
        assertEquals(myLine1, fileLines.get(3), "Подзадача-1 записалась в файл не правильно.");
    }

    @Override
    @Test
    void updateEpic_STANDART() {
        super.updateEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "2,EPIC,EPIC-1-NEW,DONE,EPIC-1-DESCRIPTION-NEW,10:15(01-02-2024),60,";
        assertEquals(myLine1, fileLines.get(2), "Эпик-1 записалась в файл неправильно.");
    }

    @Override
    @Test
    void getOneTask_STANDART() {
        super.getOneTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "1";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле неправильно.");
    }

    @Override
    @Test
    void getOneEpic_STANDART() {
        super.getOneEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "2";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле неправильно.");
    }

    @Override
    @Test
    void getOneSubtask_STANDART() {
        super.getOneSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "3";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле неправильно.");
    }

    @Override
    @Test
    void deleteAllTasks_STANDART() {
        super.deleteAllTasks_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(3, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Override
    @Test
    void deleteAllEpics_STANDART() {
        super.deleteAllEpics_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(2, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Override
    @Test
    void deleteAllSubtasks_STANDART() {
        super.deleteAllSubtasks_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(5, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Override
    @Test
    void deleteOneTask_STANDART() {
        super.deleteOneTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(4, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Override
    @Test
    void deleteOneEpic_STANDART() {
        super.deleteOneEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(3, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Override
    @Test
    void deleteOneSubtask_STANDART() {
        super.deleteOneSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(6, fileLines.size(), "Количество записей в файле неправильное.");
    }

    @Test
    void loadFromFile() {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);

        task1 = new Task("TASK-1", "TASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 1, 1, 10, 9), Duration.ofMinutes(16));
        fileBackedTasksManager.createTask(task1);
        Task task2 = new Task("TASK-2", "TASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 1, 2, 10, 28), Duration.ofMinutes(29));
        fileBackedTasksManager.createTask(task2);
        Task task3 = new Task("TASK-3", "TASK-3-DESCRIPTION",
                LocalDateTime.of(2024, 1, 3, 10, 39), Duration.ofMinutes(47));
        fileBackedTasksManager.createTask(task3);

        epic1 = new Epic("EPIC-1", "EPIC-1-DESCRIPTION");
        fileBackedTasksManager.createEpic(epic1);
        Epic epic2 = new Epic("EPIC-2", "EPIC-2-DESCRIPTION");
        fileBackedTasksManager.createEpic(epic2);

        subtask1 = new Subtask("SUBTASK-1", "SUBTASK-1-DESCRIPTION",
                LocalDateTime.of(2024, 2, 1, 10, 11), Duration.ofMinutes(15), epic1.getId());
        fileBackedTasksManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("SUBTASK-2", "SUBTASK-2-DESCRIPTION",
                LocalDateTime.of(2024, 2, 2, 10, 32), Duration.ofMinutes(30), epic2.getId());
        fileBackedTasksManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("SUBTASK-3", "SUBTASK-3-DESCRIPTION",
                LocalDateTime.of(2024, 2, 3, 10, 44), Duration.ofMinutes(45), epic2.getId());
        fileBackedTasksManager.createSubtask(subtask3);

        fileBackedTasksManager.getOneEpic(epic2.getId());
        fileBackedTasksManager.getOneTask(task3.getId());
        fileBackedTasksManager.getOneSubtask(subtask1.getId());
        fileBackedTasksManager.getOneTask(task1.getId());
        FileBackedTasksManager taskManagerLoadFromFile = FileBackedTasksManager.loadFromFile(file);

        assertEquals(fileBackedTasksManager.getAllTasks().toString(), taskManagerLoadFromFile.getAllTasks().toString(),
                "Задачи из файла записываются в базу неверно.");
        assertEquals(fileBackedTasksManager.getAllSubtasks().toString(), taskManagerLoadFromFile.getAllSubtasks().toString(),
                "Подзадачи из файла записываются в базу неверно.");
        assertEquals(fileBackedTasksManager.getAllEpics().toString(), taskManagerLoadFromFile.getAllEpics().toString(),
                "Эпики из файла записываются в базу неверно.");
        assertEquals(fileBackedTasksManager.getHistory().toString(), taskManagerLoadFromFile.getHistory().toString(),
                "История из файла записывается в базу неверно.");
    }

    private List<String> readeFile() {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
    }
}
