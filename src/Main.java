import model.*;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();


        System.out.println("\n\u001B[32m" + "TEST CREATE_13_TASKS_THROUGH_INTERFACE + PRINT_ALL" + "\u001B[38m");
        Task task1 = new Task("taskNew1", "taskText1");
        taskManager.createTask(task1);
        Task task2 = new Task("taskNew2", "taskText2");
        taskManager.createTask(task2);
        Task task3 = new Task("taskNew3", "taskText3");
        taskManager.createTask(task3);
        Task task4 = new Task("taskNew4", "taskText4");
        taskManager.createTask(task4);
        Task task5 = new Task("taskNew5", "taskText5");
        taskManager.createTask(task5);
        Task task6 = new Task("taskNew6", "taskText6");
        taskManager.createTask(task6);
        Task task7 = new Task("taskNew7", "taskText7");
        taskManager.createTask(task7);
        Task task8 = new Task("taskNew8", "taskText8");
        taskManager.createTask(task8);
        Task task9 = new Task("taskNew9", "taskText9");
        taskManager.createTask(task9);
        Task task10 = new Task("taskNew10", "taskText10");
        taskManager.createTask(task10);
        Epic epic1 = new Epic("epicNew1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subNew1", "subText1", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subNew2", "subText2", epic1.getId());
        taskManager.createSubtask(subtask2);

        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printAllSubtasks();


        System.out.println("\n\u001B[32m" + "TEST GET_ONE_BY_ID SIX_TASKS + PRINT_HISTORY" + "\u001B[38m");
        taskManager.getOneTask(task1.getId());
        taskManager.getOneTask(task2.getId());
        taskManager.getOneTask(task3.getId());
        taskManager.getOneTask(task4.getId());
        taskManager.getOneTask(task5.getId());
        taskManager.getOneTask(task6.getId());

        System.out.println("HISTORY_SIZE: " + historyManager.getHistory().size());
        historyManager.getHistory().forEach(System.out::println);

        System.out.println("\n\u001B[32m" + "TEST GET_ONE_BY_ID MORE_FOUR_TASKS + PRINT_HISTORY" + "\u001B[38m");
        taskManager.getOneTask(task7.getId());
        taskManager.getOneTask(task8.getId());
        taskManager.getOneTask(task9.getId());
        taskManager.getOneTask(task10.getId());

        System.out.println("HISTORY_SIZE: " + historyManager.getHistory().size());
        historyManager.getHistory().forEach(System.out::println);


        System.out.println("\n\u001B[32m" + "TEST GET_ONE_BY_ID MORE_THREE_TASKS + PRINT_HISTORY" + "\u001B[38m");
        taskManager.getOneEpic(epic1.getId());
        taskManager.getOneSubtask(subtask1.getId());
        taskManager.getOneSubtask(subtask2.getId());

        System.out.println("HISTORY_SIZE: " + historyManager.getHistory().size());
        historyManager.getHistory().forEach(System.out::println);
    }
}
