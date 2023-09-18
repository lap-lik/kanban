import model.*;
import service.TaskManager;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        ArrayList<Task> arrayListTask;
        ArrayList<Epic> arrayListEpic;
        ArrayList<Subtask> arrayListSubtask;

        System.out.println("\n\u001B[32m" + "TEST CREATE + READ_ALL" + "\u001B[38m");
        Task task1 = new Task("taskNew1", "taskText1");
        taskManager.createTask(task1);
        Task task2 = new Task("taskNew2", "taskText2");
        taskManager.createTask(task2);
        Epic epic1 = new Epic("epicNew1");
        taskManager.createEpic(epic1);
        Epic epic2 = new Epic("epicNew2");
        taskManager.createEpic(epic2);
        Epic epic3 = new Epic("epicNew3");
        taskManager.createEpic(epic3);
        Subtask subtask1 = new Subtask("subNew1", "subText1", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subNew2", "subText2", epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subNew3", "subText3", epic2.getId());
        taskManager.createSubtask(subtask3);
        arrayListTask = taskManager.getAllTasks();
        arrayListEpic = taskManager.getAllEpics();
        arrayListSubtask = taskManager.getAllSubtasks();
        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printAllSubtasks();

        System.out.println("\n\u001B[32m" + "TEST UPDATE + READ_ALL" + "\u001B[38m");
        task1.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(task1); //Task{id=1, title='taskNew1', text='taskText1', status=IN_PROGRESS}
        epic3.setStatus(Status.DONE);
        taskManager.updateEpic(epic3); //Epic{id=5, title='epicNew3', text='null', status='NEW', listSubtasksId=[[]]}
        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1); //Subtask{id=6, title='subNew1', text='subText1', status='IN_PROGRESS', epicId=2}
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2); //Subtask{id=7, title='subNew2', text='subText2', status='DONE', epicId=2}
        subtask3.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask3); //Subtask{id=8, title='subNew3', text='subText3', status='DONE', epicId=3}
        arrayListTask = taskManager.getAllTasks();
        arrayListEpic = taskManager.getAllEpics();
        arrayListSubtask = taskManager.getAllSubtasks();
        taskManager.printAllTasks();
        taskManager.printAllEpics();
        taskManager.printAllSubtasks();

        System.out.println("\n\u001B[32m" + "TEST READ_ONE_BY_ID" + "\u001B[38m");
        Task readTask1 = taskManager.getOneTask(1);
        taskManager.printOneTask(1); //Task{id=1, title='taskNew1', text='taskText1', status=IN_PROGRESS}
        Task readTask2 = taskManager.getOneTask(6);
        taskManager.printOneTask(6);//TASK WITH ID 6 NOT FOUND
        Epic readEpic1 = taskManager.getOneEpic(3);
        taskManager.printOneEpic(3);//Epic{id=3, title='epicNew1', text='null', status='IN_PROGRESS', listSubtasksId=[[6, 7]]}
        Subtask readSubtask1 = taskManager.getOneSubtask(4);
        taskManager.printOneSubtask(4);//SUBTASK WITH ID 4 NOT FOUND

        System.out.println("\n\u001B[32m" + "TEST READ_ALL_SUBTASK_BY_ID_EPIC" + "\u001B[38m");
        arrayListSubtask = taskManager.getAllSubtasksByEpicId(3);
        taskManager.printOneSubtask(6); //Subtask{id=6, title='subNew1', text='subText1', status='IN_PROGRESS', epicId=2}
        taskManager.printOneSubtask(7); //Subtask{id=7, title='subNew2', text='subText2', status='DONE', epicId=2}

        System.out.println("\n\u001B[32m" + "TEST DELETE_ONE + READ_ALL" + "\u001B[38m");
        taskManager.deleteOneTask(5);
        taskManager.printOneTask(5); //TASK WITH ID 5 NOT FOUND
        taskManager.deleteOneSubtask(6);
        taskManager.printOneEpic(3); //Epic{id=3, title='epicNew1', text='null', status='IN_PROGRESS', listSubtasksId=[[7]]}
        taskManager.printOneSubtask(8); //Subtask{id=8, title='subNew3', text='subText3', status='DONE', epicId=4}
        taskManager.deleteOneEpic(4);
        taskManager.printOneSubtask(8); //SUBTASK WITH ID 8 NOT FOUND

        System.out.println("\n\u001B[32m" + "TEST DELETE_ALL + READ_ALL" + "\u001B[38m");
        taskManager.deleteAllTasks();
        taskManager.printAllTasks(); //TASKS NOT FOUND!
        taskManager.deleteAllSubtasks();
        taskManager.printAllSubtasks(); //SUBTASKS NOT FOUND!
        taskManager.printOneEpic(3); //Epic{id=3, title='epicNew1', text='null', status='NEW', listSubtasksId=[[]]}
        taskManager.deleteAllEpics();
        taskManager.printAllEpics(); //EPIC TASKS NOT FOUND!
    }
}
