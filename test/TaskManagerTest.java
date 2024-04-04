import model.Task;
import model.TaskStatus;
import service.HistoryManager;
import service.TaskManager;
import model.Epic;
import model.Subtask;
import service.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    public static TaskManager taskManager;
    public static HistoryManager historyManager;

    @BeforeAll
    static void beforeAll() {

        // создаем окружение для тестирования
        //завоидим значения для задач
        historyManager = Managers.getDefaultHistory();
        taskManager = Managers.getDefault();

        Task task1 = new Task();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);

        Task task2 = new Task();
        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);

        Task task3 = new Task();
        task3.setTaskName("task3_name");
        task3.setDescription("task3_description");
        task3.setStatus(TaskStatus.IN_PROGRESS);

        Epic epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");


        Subtask subtask1 = new Subtask();
        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);


        Subtask subtask2 = new Subtask();
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);


        Epic epic2 = new Epic();
        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        Subtask subtask3 = new Subtask();
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);

        Epic epic3 = new Epic();
        epic3.setTaskName("epic3_name");
        epic3.setDescription("epic3_description");

        Epic epic4 = new Epic();
        epic4.setTaskName("epic4_name");
        epic4.setDescription("epic4_description");
        Subtask subtask4 = new Subtask();
        subtask4.setTaskName("subtask4_name");
        subtask4.setDescription("subtask4_description");
        subtask4.setStatus(TaskStatus.NEW);
        Subtask subtask5 = new Subtask();
        subtask5.setTaskName("subtask5_name");
        subtask5.setDescription("subtask5_description");
        subtask5.setStatus(TaskStatus.NEW);

        task1 = taskManager.createNewTask(task1);
        task2 = taskManager.createNewTask(task2);
        epic1 = taskManager.createNewEpic(epic1);
        subtask1 = taskManager.createNewSubtask(subtask1);
        subtask1.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);
        subtask2.setEpicId(epic1.getId());
        epic2 = taskManager.createNewEpic(epic2);
        subtask3 = taskManager.createNewSubtask(subtask3);
        subtask3.setEpicId(epic2.getId());
        epic3 = taskManager.createNewEpic(epic3);
        epic4 = taskManager.createNewEpic(epic4);
        subtask4.setEpicId(epic4.getId());
        subtask5.setEpicId(epic4.getId());
    }

    @Test
    public void returnsTrueIfTasksInstancesAreEqualIfTheirIdsEqual() {

    }


}
