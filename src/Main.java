import model.Task;
import model.TaskStatus;
import service.HistoryManager;
import service.TaskManager;
import model.Epic;
import model.Subtask;
import service.Managers;

public class Main {
    public static TaskManager taskManager;
    public static HistoryManager historyManager;


    public static void main(String[] args) {
        System.out.println("Поехали!");

        historyManager = Managers.getDefaultHistory();

        // проверяем действия с обычными task
        Task task1 = new Task();
        task1.setTaskName("task1");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);

        Task task2 = new Task();
        task2.setTaskName("task2");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);


        taskManager = Managers.getDefault();
        task1 = taskManager.createNewTask(task1);
        task2 = taskManager.createNewTask(task2);

        // тестируем epic1
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        // тестируем subtask
        Subtask subtask1 = new Subtask();
        subtask1.setTaskName("subtask1");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW); // поменял значение статуса для тестирования
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTaskName("subtask2");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        // тестируем epic2
        Epic epic2 = new Epic();
        epic2.setTaskName("epic2");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        // тестируем epic3
        Epic epic3 = new Epic();
        epic3.setTaskName("epic3");
        epic3.setDescription("epic3_description");
        epic3 = taskManager.createNewEpic(epic3);


        Subtask subtask3 = new Subtask();
        subtask3.setTaskName("subtask3");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setEpicId(epic2.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtatks());

        //обновляем task1
        task1.setTaskName("task1_new_name");
        task1.setDescription("task1__new_description");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        System.out.println(taskManager.getAllTasks());

        //обновляем epic2
        epic2.setTaskName("epic2_new_name");
        epic2.setDescription("epic2_description_new_description");
        taskManager.updateEpic(6);
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getEpicSubtasks(6));

        //


        System.out.println("HISTORY MANAGER");
        taskManager.getTaskById(1);
        taskManager.getEpicById(3);
        System.out.println(historyManager.getHistory());
        System.out.println("!!!!!!!!!");

        //удаляем задачи
        taskManager.deleteSubtask(4);
        taskManager.deleteEpic(6);
        taskManager.deleteTaskById(1);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtatks());

        taskManager.clearAllSubtasks();
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtatks());

        taskManager.clearAll();
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllSubtatks());
        System.out.println(taskManager.getAllEpics());
    }
}
