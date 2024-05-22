import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;


public class Main {
    public static TaskManager taskManager = Managers.getDefault();
    public static HistoryManager historyManager = Managers.getDefaultHistory();


    public static void main(String[] args) {
        System.out.println("Поехали!");

        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(30));
        task1.setStartTime(LocalDateTime.of(2024, 12, 10, 10, 10));
        task1 = taskManager.createNewTask(task1);


        Task task2 = new Task(); // id 2
        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 15));
        task2.setDuration(Duration.ofMinutes(20));
        task2 = taskManager.createNewTask(task2);

        Epic epic1 = new Epic(); // id 3
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask(); // id 4
        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 15));
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(); // id 5
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 30));
        subtask2.setDuration(Duration.ofMinutes(20));
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        Subtask subtask3 = new Subtask(); // id 6
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setStartTime(LocalDateTime.of(2024, 10, 10, 10, 50));
        subtask3.setDuration(Duration.ofMinutes(20));
        subtask3.setEpicId(epic1.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        Epic epic2 = new Epic(); // id 7
        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        taskManager.getTaskById(1);
        System.out.println(taskManager.getEpicById(3));
        System.out.println(historyManager.getHistory());
        taskManager.getEpicById(3);
        System.out.println(historyManager.getHistory());
        taskManager.getSubtaskById(4);
        System.out.println(historyManager.getHistory());
        taskManager.getSubtaskById(6);
        System.out.println(historyManager.getHistory());
        taskManager.getEpicById(3);
        System.out.println(historyManager.getHistory());
        taskManager.deleteTaskById(2);
        System.out.println(historyManager.getHistory());
        //taskManager.deleteEpic(3);
        taskManager.deleteSubtask(5);
        taskManager.getEpicById(7);
        System.out.println(historyManager.getHistory());
        System.out.println(taskManager.getPrioritizedTasks());
        System.out.println(taskManager.getAllTasks());
    }
}

