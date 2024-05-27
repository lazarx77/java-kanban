import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HistoryManagerTest {
    public static TaskManager taskManager = Managers.getDefault();
    public static HistoryManager historyManager = Managers.getDefaultHistory();
    private Task task1 = new Task(); // id 1
    private Task task2 = new Task(); // id 2
    private Task task3 = new Task(); // id 3
    private Epic epic1 = new Epic(); // id 4

    private Subtask subtask1 = new Subtask(); // id 5
    private Subtask subtask2 = new Subtask(); // id 6
    private Epic epic2 = new Epic(); // id 7
    private Subtask subtask3 = new Subtask(); // id 8
    private Epic epic3 = new Epic(); // id 9
    private Epic epic4 = new Epic(); // id 10
    private Subtask subtask4 = new Subtask(); // id 11
    private Subtask subtask5 = new Subtask(); // id 12


    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(20));
        task1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
        task1 = taskManager.createNewTask(task1);

        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2022, 12, 2, 10, 45, 0));
        task2 = taskManager.createNewTask(task2);

        task3.setTaskName("task3_name");
        task3.setDescription("task3_description");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3.setDuration(Duration.ofMinutes(20));
        task3.setStartTime(LocalDateTime.of(2022, 12, 3, 10, 45, 0));
        task3 = taskManager.createNewTask(task3);

        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setDuration(Duration.ofMinutes(20));
        subtask1.setStartTime(LocalDateTime.of(2022, 12, 4, 10, 25, 0));
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setDuration(Duration.ofMinutes(0));
        subtask2.setStartTime(LocalDateTime.of(2022, 12, 5, 10, 45, 0));
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setDuration(Duration.ofMinutes(10));
        subtask3.setStartTime(LocalDateTime.of(2022, 12, 6, 11, 25, 0));
        subtask3.setEpicId(epic2.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        epic3.setTaskName("epic3_name");
        epic3.setDescription("epic3_description");
        epic3 = taskManager.createNewEpic(epic3);

        epic4.setTaskName("epic4_name");
        epic4.setDescription("epic4_description");
        epic4 = taskManager.createNewEpic(epic4);

        subtask4.setTaskName("subtask4_name");
        subtask4.setDescription("subtask4_description");
        subtask4.setStatus(TaskStatus.NEW);
        subtask4.setDuration(Duration.ofMinutes(10));
        subtask4.setStartTime(LocalDateTime.of(2022, 12, 7, 11, 35, 0));
        subtask4.setEpicId(epic4.getId());
        subtask4 = taskManager.createNewSubtask(subtask4);

        subtask5.setTaskName("subtask5_name");
        subtask5.setDescription("subtask5_description");
        subtask5.setStatus(TaskStatus.NEW);
        subtask5.setDuration(Duration.ofMinutes(10));
        subtask5.setStartTime(LocalDateTime.of(2022, 12, 8, 11, 45, 0));
        subtask5.setEpicId(epic4.getId());
        subtask5 = taskManager.createNewSubtask(subtask5);
    }

    @Test
    public void taskManagerAndHistoryManagerShouldNotBeNull() {
        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }

    // тест добавления в историю
    @Test
    void add() {
        taskManager.getTaskById(1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    @Test
    public void historyShouldSaveSaveTaskAfterUpdate() {
        taskManager.getTaskById(1);
        Task task1Updated = new Task();
        task1Updated.setTaskName("task1_name_updated");
        task1Updated.setDescription("task1_description_updated");
        task1Updated.setStatus(TaskStatus.IN_PROGRESS);
        task1Updated.setId(task1.getId());
        task1Updated.setDuration(task1.getDuration());
        task1Updated.setStartTime(task1.getStartTime());
        taskManager.updateTask(task1Updated);
        taskManager.getTaskById(1);
        List<Task> history = historyManager.getHistory();

        assertEquals("[Task{id='1 , taskName= task1_name_updated , description.length= 25, " +
                        "startTime= 01.12.2022 - 10:25, duration= 20, status=IN_PROGRESS }]",
                history.toString());
    }

    @Test
    public void historyShouldSaveUpdatedEpicsAndSubtasksFormAfterUpdate() {
        String epic2String = "[Epic{id='7 , taskName= epic2_updated_name , description.length=25, startTime= " +
                "06.12.2022 - 11:25, duration= 10, status=DONE subtasksIds= [8]}]";
        taskManager.getEpicById(7);
        Epic epic2Updated = new Epic();
        epic2Updated.setTaskName("epic2_updated_name");
        epic2Updated.setDescription("epic2_updated_description");
        epic2Updated.setId(epic2.getId());
        epic2Updated.setStatus(TaskStatus.NEW);
        epic2Updated.setDuration(epic2.getDuration());
        epic2Updated.setStartTime(epic2.getStartTime());

        taskManager.updateEpic(epic2Updated);
        taskManager.getEpicById(7);
        assertEquals(epic2String, historyManager.getHistory().toString());

        taskManager.getSubtaskById(12);
        Subtask subtask5Updated = new Subtask();
        subtask5Updated.setTaskName("subtask5_updated_name");
        subtask5Updated.setDescription("subtask5_updated_description");
        subtask5Updated.setStatus(TaskStatus.IN_PROGRESS);
        subtask5Updated.setDuration(subtask5.getDuration());
        subtask5Updated.setStartTime(subtask5.getStartTime());
        subtask5Updated.setId(subtask5.getId());
        subtask5Updated.setEpicId(epic4.getId());

        taskManager.updateSubTask(subtask5Updated);
        String subtask5String = "[Epic{id='7 , taskName= epic2_updated_name , description.length=25, " +
                "startTime= 06.12.2022 - 11:25, duration= 10, status=DONE subtasksIds= [8]}, Subtask{id='12 , " +
                "taskName= subtask5_name , description.length=20, startTime= 08.12.2022 - 11:45, duration= 10, " +
                "status=NEW epicId = 10}]";
        assertEquals(subtask5String, historyManager.getHistory().toString());
    }
}






