import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;
import model.Epic;
import model.Subtask;
import service.Managers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerTest {

    public static TaskManager taskManager = Managers.getDefault();
    public static HistoryManager historyManager = Managers.getDefaultHistory();

    Task task1created;
    Task task2;

//    @BeforeAll
//    static void beforeAll() {
//
//        // создаем окружение для тестирования
//        //завоидим значения для задач
//        historyManager = Managers.getDefaultHistory();
//        taskManager = Managers.getDefault();
//
//        Task task1 = new Task();
//        task1.setTaskName("task1_name");
//        task1.setDescription("task1_description");
//        task1.setStatus(TaskStatus.NEW);
//
//        Task task2 = new Task();
//        task2.setTaskName("task2_name");
//        task2.setDescription("task2_description");
//        task2.setStatus(TaskStatus.IN_PROGRESS);
//
//        Task task3 = new Task();
//        task3.setTaskName("task3_name");
//        task3.setDescription("task3_description");
//        task3.setStatus(TaskStatus.IN_PROGRESS);
//
//        Epic epic1 = new Epic();
//        epic1.setTaskName("epic1_name");
//        epic1.setDescription("epic1_description");
//        epic1 = taskManager.createNewEpic(epic1);
//
//
//        Subtask subtask1 = new Subtask();
//        subtask1.setTaskName("subtask1_name");
//        subtask1.setDescription("subtask1_description");
//        subtask1.setStatus(TaskStatus.NEW);
//
//
//        Subtask subtask2 = new Subtask();
//        subtask2.setTaskName("subtask2_name");
//        subtask2.setDescription("subtask2_description");
//        subtask2.setStatus(TaskStatus.DONE);
//
//
//        Epic epic2 = new Epic();
//        epic2.setTaskName("epic2_name");
//        epic2.setDescription("epic2_description");
//        Subtask subtask3 = new Subtask();
//        subtask3.setTaskName("subtask3_name");
//        subtask3.setDescription("subtask3_description");
//        subtask3.setStatus(TaskStatus.DONE);
//
//        Epic epic3 = new Epic();
//        epic3.setTaskName("epic3_name");
//        epic3.setDescription("epic3_description");
//
//        Epic epic4 = new Epic();
//        epic4.setTaskName("epic4_name");
//        epic4.setDescription("epic4_description");
//        Subtask subtask4 = new Subtask();
//        subtask4.setTaskName("subtask4_name");
//        subtask4.setDescription("subtask4_description");
//        subtask4.setStatus(TaskStatus.NEW);
//        Subtask subtask5 = new Subtask();
//        subtask5.setTaskName("subtask5_name");
//        subtask5.setDescription("subtask5_description");
//        subtask5.setStatus(TaskStatus.NEW);
//
//        task1 = taskManager.createNewTask(task1);
//        task2 = taskManager.createNewTask(task2);
//
//        subtask1 = taskManager.createNewSubtask(subtask1);
//        subtask1.setEpicId(epic1.getId());
//        subtask2 = taskManager.createNewSubtask(subtask2);
//        subtask2.setEpicId(epic1.getId());
//        epic2 = taskManager.createNewEpic(epic2);
//        subtask3 = taskManager.createNewSubtask(subtask3);
//        subtask3.setEpicId(epic2.getId());
//        epic3 = taskManager.createNewEpic(epic3);
//        epic4 = taskManager.createNewEpic(epic4);
//        subtask4.setEpicId(epic4.getId());
//        subtask5.setEpicId(epic4.getId());
//
//    }
    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        // historyManager.clearHistory();
    }

    // проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void task1ShouldBeEqualToTask1InTaskManager() {
        Task task1 = new Task();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);
        assertEquals(task1, taskManager.getTaskById(1));
    }

    // проверьте, что наследники класса Task равны друг другу, если равен их id
    @Test
    public void epic1ShouldBeEqualToEpic1InTaskManager(){
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setTaskName("subtask1");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTaskName("subtask2");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);
        assertEquals(epic1, taskManager.getEpicById(1));
    }

    @Test
    public void subtask1ShouldBeEqualToSubtask1InTaskManager(){
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setTaskName("subtask1");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTaskName("subtask2");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);
        assertEquals(subtask1, taskManager.getSubtaskById(2));
    }

    // проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи
//    @Test
//    public void epic1ShouldNotBeItsOwnSubtask(){ // метод не будет работать
//        Epic epic1 = new Epic();
//        epic1.setTaskName("epic1");
//        epic1.setDescription("epic1_description");
//        epic1 = taskManager.createNewEpic(epic1);
//        Subtask subtask1 = new Subtask();
//        subtask1 = taskManager.createNewSubtask(epic1); // не скомпилируется, так как createNewSubtask
////принимает только объекты Subtask
//        assertEquals(subtask1, taskManager.getSubtaskById(2));
//    }

    // проверьте, что объект Subtask нельзя сделать своим же эпиком
//    @Test
//    public void subtask1ShouldBeItsOwnEpic(){ // метод не будет работать
//        Epic epic1 = new Epic();
//        epic1.setTaskName("epic1");
//        epic1.setDescription("epic1_description");
//        epic1 = taskManager.createNewEpic(epic1);
//
//        Subtask subtask1 = new Subtask();
//        subtask1.setTaskName("subtask1");
//        subtask1.setDescription("subtask1_description");
//        subtask1.setStatus(TaskStatus.NEW);
//        subtask1.setEpicId(epic1.getId());
//        subtask1 = taskManager.createNewSubtask(subtask1);
//
//        Subtask subtask2 = new Subtask();
//        subtask2.setTaskName("subtask2");
//        subtask2.setDescription("subtask2_description");
//        subtask2.setStatus(TaskStatus.DONE);
//        subtask2.setEpicId(3); // подставляет id subtask2 в качестве номера epic
//        subtask2 = taskManager.createNewSubtask(subtask2); // в методе createNewSubtask требуется экземпляр Epic epic,
//        // который будет равен null
//        assertEquals(subtask1, taskManager.getSubtaskById(2));
//    }

    // убедитесь, что утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void taskManagerAndHistoryManagerShouldNotBeNull() {
        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }

    // проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    @Test
    public void inMemoryTaskManagerAddsDifferentTaskTypesAndCanGetThemByIds() {
        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);

        Task task2 = new Task(); // id 2
        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2 = taskManager.createNewTask(task2);

        Task task3 = new Task(); // id 3
        task3.setTaskName("task3_name");
        task3.setDescription("task3_description");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3 = taskManager.createNewTask(task3);

        Epic epic1 = new Epic(); // id 4
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask(); // id 5
        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(); // id 6
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        Epic epic2 = new Epic(); // id 7
        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        Subtask subtask3 = new Subtask(); // id 8
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setEpicId(epic2.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        Epic epic3 = new Epic(); // id 9
        epic3.setTaskName("epic3_name");
        epic3.setDescription("epic3_description");
        epic3 = taskManager.createNewEpic(epic3);

        Epic epic4 = new Epic(); // id 10
        epic4.setTaskName("epic4_name");
        epic4.setDescription("epic4_description");
        epic4 = taskManager.createNewEpic(epic4);

        Subtask subtask4 = new Subtask(); // id 11
        subtask4.setTaskName("subtask4_name");
        subtask4.setDescription("subtask4_description");
        subtask4.setStatus(TaskStatus.NEW);
        subtask4.setEpicId(epic4.getId());
        subtask4 = taskManager.createNewSubtask(subtask4);

        Subtask subtask5 = new Subtask(); // id 12
        subtask5.setTaskName("subtask5_name");
        subtask5.setDescription("subtask5_description");
        subtask5.setStatus(TaskStatus.NEW);
        subtask5.setEpicId(epic4.getId());
        subtask5 = taskManager.createNewSubtask(subtask5);

        assertEquals("task1_name", task1.getTaskName(1));
        assertEquals("subtask2_name", subtask2.getTaskName(6));
        assertEquals("epic4_name", epic4.getTaskName(10));
        assertEquals("subtask5_name", subtask5.getTaskName(12));
        String epic1String = "Epic{id='4 , taskName= epic1_name , description.length=17, status=IN_PROGRESS " +
                "subtasksIds= [5, 6]}";
        assertEquals(epic1String, taskManager.getEpicById(4).toString());
    }

    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера - в моей реализации
    // нет заданных id - все генерируются

    // создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void tasksArgumentsShouldNotChangeWhenAddedToTheManager() {
        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);
        assertEquals("task1_name", task1.getTaskName(1));
        assertEquals("task1_description", task1.getDescription());
        assertEquals(TaskStatus.NEW, task1.getStatus());
    }

    // тест добавления в историю
    @Test
    void add() {
        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);
        historyManager.add(task1);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    // убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных
    @Test
    public void historyShouldSavePreviousTaskFormAfterUpdate() {
        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);
        taskManager.getTaskById(1);
        List<Task> history = historyManager.getHistory();
        task1.setTaskName("task1_name_updated");
        task1.setDescription("task1_description_updated");
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(1);
        taskManager.getTaskById(1);

        assertEquals("[Task{id='1 , taskName= task1_name , description.length=17, status=NEW }, " +
                "Task{id='1 , taskName= task1_name_updated , description.length=25, status=IN_PROGRESS }]",
                history.toString());
    }

    @Test
    public void historyShouldSavePreviousEpicsAndSubtasksFormAfterUpdate() {
        Task task1 = new Task(); // id 1
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1 = taskManager.createNewTask(task1);

        Task task2 = new Task(); // id 2
        task2.setTaskName("task2_name");
        task2.setDescription("task2_description");
        task2.setStatus(TaskStatus.IN_PROGRESS);
        task2 = taskManager.createNewTask(task2);

        Task task3 = new Task(); // id 3
        task3.setTaskName("task3_name");
        task3.setDescription("task3_description");
        task3.setStatus(TaskStatus.IN_PROGRESS);
        task3 = taskManager.createNewTask(task3);

        Epic epic1 = new Epic(); // id 4
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epic1 = taskManager.createNewEpic(epic1);

        Subtask subtask1 = new Subtask(); // id 5
        subtask1.setTaskName("subtask1_name");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setEpicId(epic1.getId());
        subtask1 = taskManager.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask(); // id 6
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setEpicId(epic1.getId());
        subtask2 = taskManager.createNewSubtask(subtask2);

        Epic epic2 = new Epic(); // id 7
        epic2.setTaskName("epic2_name");
        epic2.setDescription("epic2_description");
        epic2 = taskManager.createNewEpic(epic2);

        Subtask subtask3 = new Subtask(); // id 8
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setEpicId(epic2.getId());
        subtask3 = taskManager.createNewSubtask(subtask3);

        Epic epic3 = new Epic(); // id 9
        epic3.setTaskName("epic3_name");
        epic3.setDescription("epic3_description");
        epic3 = taskManager.createNewEpic(epic3);

        Epic epic4 = new Epic(); // id 10
        epic4.setTaskName("epic4_name");
        epic4.setDescription("epic4_description");
        epic4 = taskManager.createNewEpic(epic4);

        Subtask subtask4 = new Subtask(); // id 11
        subtask4.setTaskName("subtask4_name");
        subtask4.setDescription("subtask4_description");
        subtask4.setStatus(TaskStatus.NEW);
        subtask4.setEpicId(epic4.getId());
        subtask4 = taskManager.createNewSubtask(subtask4);

        Subtask subtask5 = new Subtask(); // id 12
        subtask5.setTaskName("subtask5_name");
        subtask5.setDescription("subtask5_description");
        subtask5.setStatus(TaskStatus.NEW);
        subtask5.setEpicId(epic4.getId());
        subtask5 = taskManager.createNewSubtask(subtask5);

        String epic2String = "[Epic{id='7 , taskName= epic2_name , description.length=17, " +
                "status=DONE subtasksIds= [8]}, Epic{id='7 , taskName= epic2_updated_name , description.length=25, " +
                "status=DONE subtasksIds= [8]}]";
        taskManager.getEpicById(7);
        epic2.setTaskName("epic2_updated_name");
        epic2.setDescription("epic2_updated_description");
        taskManager.updateEpic(7);
        taskManager.getEpicById(7);
        assertEquals(epic2String, historyManager.getHistory().toString());

        taskManager.getSubtaskById(12);
        subtask5.setTaskName("subtask5_updated_name");
        subtask5.setDescription("subtask5_updated_description");
        subtask5.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(12);
        String subtask5String = "[Epic{id='7 , taskName= epic2_name , description.length=17, status=DONE " +
                "subtasksIds= [8]}, Epic{id='7 , taskName= epic2_updated_name , description.length=25, status=DONE " +
                "subtasksIds= [8]}, Subtask{id='12 , taskName= subtask5_name , description.length=20, status=NEW " +
                "epicId = 10}]";
        assertEquals(subtask5String, historyManager.getHistory().toString());



    }
}
