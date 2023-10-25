import model.*;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("task1", "taskText1");
        taskManager.createTask(task1);
        Task task2 = new Task("task2", "taskText2");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("epicNew1");
        taskManager.createEpic(epic1);
        Epic epic2 = new Epic("epicNew2");
        taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("subNew1", "subText1", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subNew2", "subText2", epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subNew3", "subText3", epic1.getId());
        taskManager.createSubtask(subtask3);

        System.out.println("\n\u001B[32m" + "TEST: GET 7 TASKS + PRINT_HISTORY " + "\u001B[38m");
        taskManager.getOneTask(task1.getId());
        taskManager.getOneTask(task2.getId());
        taskManager.getOneEpic(epic1.getId());
        taskManager.getOneEpic(epic2.getId());
        taskManager.getOneSubtask(subtask1.getId());
        taskManager.getOneSubtask(subtask2.getId());
        taskManager.getOneSubtask(subtask3.getId());
        historySize(taskManager);
        printHistory(taskManager);

        System.out.println("\n\u001B[32m" + "TEST: GET MORE TIMES TASK1 AND EPIC1 + PRINT_HISTORY " + "\u001B[38m");
        taskManager.getOneTask(task1.getId());
        taskManager.getOneEpic(epic1.getId());
        historySize(taskManager);
        printHistory(taskManager);

        System.out.println("\n\u001B[32m" + "TEST: DELETE TASK1 + PRINT_HISTORY " + "\u001B[38m");
        taskManager.deleteOneTask(task1.getId());
        historySize(taskManager);
        printHistory(taskManager);

        System.out.println("\n\u001B[32m" + "TEST: DELETE EPIC1 + PRINT_HISTORY " + "\u001B[38m");
        taskManager.deleteOneEpic(epic1.getId());
        historySize(taskManager);
        printHistory(taskManager);

        System.out.println("\n\u001B[32m" + "TEST: DELETE ALL_TASKS + PRINT_HISTORY " + "\u001B[38m");
        taskManager.deleteAllTasks();
        historySize(taskManager);
        printHistory(taskManager);
    }
    private static void historySize(TaskManager taskManager){
        System.out.println("History size: " + taskManager.getHistory().size());
    }
    private static void printHistory(TaskManager taskManager){
        taskManager.getHistory().forEach(System.out::println);
    }
}
