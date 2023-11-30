package service;

import constants.Constants;
import exception.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static constants.Constants.FILE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileBackedTaskManagerTest<T extends TaskManager> extends TaskManagerTest<FileBackedTasksManager> {

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
        String myLine1 = "2,EPIC,EPIC-1,NEW,EPIC-1-DESCRIPTION,";
        assertEquals(myLine1, fileLines.get(2), "Эпик-1 записалась в файл не правильно.");
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
        String myLine1 = "2,EPIC,EPIC-1-NEW,DONE,EPIC-1-DESCRIPTION-NEW,";
        assertEquals(myLine1, fileLines.get(2), "Эпик-1 записалась в файл не правильно.");
    }

    @Override
    @Test
    void getOneTask_STANDART() {
        super.getOneTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "1";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле не правильно.");
    }

    @Override
    @Test
    void getOneEpic_STANDART() {
        super.getOneEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "2";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле не правильно.");
    }

    @Override
    @Test
    void getOneSubtask_STANDART() {
        super.getOneSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        String myLine1 = "3";
        assertEquals(myLine1, fileLines.get(5), "Список истории в файле не правильно.");
    }

    @Override
    @Test
    void deleteAllTasks_STANDART() {
        super.deleteAllTasks_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(3, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Override
    @Test
    void deleteAllEpics_STANDART() {
        super.deleteAllEpics_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(2, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Override
    @Test
    void deleteAllSubtasks_STANDART() {
        super.deleteAllSubtasks_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(5, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Override
    @Test
    void deleteOneTask_STANDART() {
        super.deleteOneTask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(4, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Override
    @Test
    void deleteOneEpic_STANDART() {
        super.deleteOneEpic_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(3, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Override
    @Test
    void deleteOneSubtask_STANDART() {
        super.deleteOneSubtask_STANDART();
        assertTrue(file.exists(), "Файл не существует.");
        List<String> fileLines = readeFile();
        assertEquals(6, fileLines.size(), "Количество записей в файле не правильное.");
    }

    @Test
    void loadFromFile() {
        String checkTask1ToString = "1,TASK,TASK-1,NEW,TASK-1-DESCRIPTION,10:15(01-01-2024),30,\n";
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bufferedWriter.write(Constants.COLUMNS + "\n");
            bufferedWriter.write(checkTask1ToString);
            bufferedWriter.write("\n");
            bufferedWriter.write("1,2");
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
        TaskManager taskManagerLoadFromFile = FileBackedTasksManager.loadFromFile(file);

        assertEquals(checkTask1ToString, taskManagerLoadFromFile.getOneTask(1).toString(),
                "При чтении из файла не правильно сохранилась задача в базе");
        assertEquals(checkTask1ToString, taskManagerLoadFromFile.getPrioritizedTasks().get(0).toString(),
                "При чтении из файла не правильно сохранилась задача в списке приоритетного вызова");
        assertEquals(checkTask1ToString, taskManagerLoadFromFile.getHistory().get(0).toString(),
                "При чтении из файла не правильно сохранилась задача в истории");
    }

    private void deleteFile() {
        try {
            Files.delete(file.toPath());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
    }

    private List<String> readeFile() {
        try {
            return Files.readAllLines(file.toPath());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось сохранить в файл " + file.getName(), exception);
        }
    }
}
