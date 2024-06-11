package managersTests;

import exceptions.TimeCrossException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class TaskManagerTest {

    public static TaskManager taskManager;
    protected Task task1; // id 1
    protected Task task2; // = new Task(); // id 2
    protected Task task3; // id 3
    protected Epic epic1; // = new Epic(); // id 4
    protected Subtask subtask1; // = new Subtask(); // id 5
    protected Subtask subtask2; // = new Subtask(); // id 6
    protected Epic epic2; // = new Epic(); // id 7
    protected Subtask subtask3; // = new Subtask(); // id 8
    protected Epic epic3; // = new Epic(); // id 9
    protected Epic epic4; // = new Epic(); // id 10
    protected Subtask subtask4; // = new Subtask(); // id 11
    protected Subtask subtask5; // = new Subtask(); // id 12

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        //historyManager = Managers.getDefaultHistory();
        task1 = new Task();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(20));
        task1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
        task1 = taskManager.createNewTask(task1);

        task2 = new Task();
        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 50, 0));
        task2 = taskManager.createNewTask(task2);

        task3 = new Task();
        task3.setTaskName("task3_name");
        task3.setDescription("task3_description");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3.setDuration(Duration.ofMinutes(20));
        task3.setStartTime(LocalDateTime.of(2022, 12, 1, 11, 50, 0));
        task3 = taskManager.createNewTask(task3);

        epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        subtask1 = new Subtask();
        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setDuration(Duration.ofMinutes(20));
        subtask1.setStartTime(LocalDateTime.of(2022, 12, 1, 12, 25, 0));
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        subtask2 = new Subtask();
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setDuration(Duration.ofMinutes(0));
        subtask2.setStartTime(LocalDateTime.of(2022, 12, 1, 13, 45, 0));
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        epic2 = new Epic();
        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        subtask3 = new Subtask();
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setDuration(Duration.ofMinutes(10));
        subtask3.setStartTime(LocalDateTime.of(2022, 12, 1, 14, 25, 0));
        subtask3.setEpicId(epic2.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        epic3 = new Epic();
        epic3.setTaskName("epic3_name");
        epic3.setDescription("epic3_description");
        epic3 = taskManager.createNewEpic(epic3);

        epic4 = new Epic();
        epic4.setTaskName("epic4_name");
        epic4.setDescription("epic4_description");
        epic4 = taskManager.createNewEpic(epic4);

        subtask4 = new Subtask();
        subtask4.setTaskName("subtask4_name");
        subtask4.setDescription("subtask4_description");
        subtask4.setStatus(TaskStatus.DONE);
        subtask4.setDuration(Duration.ofMinutes(10));
        subtask4.setStartTime(LocalDateTime.of(2022, 12, 1, 15, 35, 0));
        subtask4.setEpicId(epic4.getId());
        subtask4 = taskManager.createNewSubtask(subtask4);

        subtask5 = new Subtask();
        subtask5.setTaskName("subtask5_name");
        subtask5.setDescription("subtask5_description");
        subtask5.setStatus(TaskStatus.DONE);
        subtask5.setDuration(Duration.ofMinutes(10));
        subtask5.setStartTime(LocalDateTime.of(2022, 12, 1, 15, 45, 0));
        subtask5.setEpicId(epic4.getId());
        subtask5 = taskManager.createNewSubtask(subtask5);
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void task1ShouldBeEqualToTask1InTaskManager() {
        assertEquals(task1, taskManager.getTaskById(1));
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id

    @Test
    public void epic1ShouldBeEqualToEpic1InTaskManager() {
        assertEquals(epic1, taskManager.getEpicById(4));
    }

    @Test
    public void subtask1ShouldBeEqualToSubtask1InTaskManager() {
        assertEquals(subtask1, taskManager.getSubtaskById(5));
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id

    @Test
    public void inMemoryTaskManagerAddsDifferentTaskTypesAndCanGetThemByIds() {
        assertEquals("task1_name", task1.getTaskName(1));
        assertEquals("subtask2_name", subtask2.getTaskName(6));
        assertEquals("epic4_name", epic4.getTaskName(10));
        assertEquals("subtask5_name", subtask5.getTaskName(12));
        String epic1String = "Epic{id='4 , taskName= epic1_name , description.length=17, startTime=" +
                " 01.12.2022 - 12:25, duration= 20, status=IN_PROGRESS subtasksIds= [5, 6], " +
                "endTime= 01.12.2022 - 13:05}";
        assertEquals(epic1String, taskManager.getEpicById(4).toString());
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера - в моей реализации
    // нет заданных id - все генерируются

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void tasksArgumentsShouldNotChangeWhenAddedToTheManager() {
        assertEquals("task1_name", task1.getTaskName(1));
        assertEquals("task1_description", task1.getDescription());
        assertEquals(TaskStatus.NEW, task1.getStatus());
    }

    @Test
    public void epicsShouldHaveSameStatusAsAllItsSubtasks() {
        assertEquals(epic4.getStatus(), subtask4.getStatus());
        assertEquals(epic4.getStatus(), subtask5.getStatus());
    }

    @Test
    public void timeCrossExceptionShouldBeThrownIfTasksTimeCross() {
        task2 = new Task();
        task2.setTaskName("task13_name");
        task2.setDescription("task13_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2.setDuration(Duration.ofMinutes(20));
        task2.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));

        assertThrows(TimeCrossException.class, () -> {
            task2 = taskManager.createNewTask(task2);
        }, "Задача 13 не добавлена: пересечение задач по времени.");
    }
}


