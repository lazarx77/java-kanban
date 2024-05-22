import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.InMemoryTaskManager;
import service.ManagerSaveException;
import service.Managers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest <service.FileBackedTaskManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getDefault();
        historyManager = Managers.getDefaultHistory();
        FileBackedTaskManager fm = new FileBackedTaskManager();
    }

    // проверяем сохранение задач в отсутствующий/пустой файл
    @Test
    public void fileBackedTaskManagerShouldSaveTasksToNewFile() {
        FileBackedTaskManager fm = new FileBackedTaskManager();
        Task task1 = new Task();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(30));
        task1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        task1 = fm.createNewTask(task1);
        File saveFile = FileBackedTaskManager.getSaveFile();
        Task middleTask = null;
        try (BufferedReader fileReader = new BufferedReader(new FileReader(saveFile, StandardCharsets.UTF_8))) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (!line.startsWith("id")) {
                    middleTask = fm.fromString(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        assertEquals(fm.toString(task1), fm.toString(middleTask));
        saveFile.delete();
    }

    // проверяем сохранение задач в отсутствующий/пустой файл
    @Test
    public void fileBackedTaskManagerShouldSaveMultipleTasks() {
        FileBackedTaskManager fm = new FileBackedTaskManager();
        Task task1 = new Task();
        task1.setTaskName("task1_name");
        task1.setDescription("task1_description");
        task1.setStatus(TaskStatus.NEW);
        task1.setDuration(Duration.ofMinutes(30));
        task1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        task1 = fm.createNewTask(task1);

        Epic epic1 = new Epic();
        epic1.setTaskName("epic1");
        epic1.setDescription("epic1_description");
        epic1 = fm.createNewEpic(epic1);

        Subtask subtask1 = new Subtask();
        subtask1.setTaskName("subtask1");
        subtask1.setDescription("subtask1_description");
        subtask1.setStatus(TaskStatus.NEW);
        subtask1.setDuration(Duration.ofMinutes(15));
        subtask1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        subtask1.setEpicId(epic1.getId());
        subtask1 = fm.createNewSubtask(subtask1);

        Subtask subtask2 = new Subtask();
        subtask2.setTaskName("subtask2");
        subtask2.setDescription("subtask2_description");
        subtask2.setStatus(TaskStatus.DONE);
        subtask2.setDuration(Duration.ofMinutes(20));
        subtask2.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
        subtask2.setEpicId(epic1.getId());
        subtask2 = fm.createNewSubtask(subtask2);
        File saveFile = FileBackedTaskManager.getSaveFile();

        List<String> taskStings = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(saveFile, StandardCharsets.UTF_8))) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (!line.startsWith("id")) {
                    taskStings.add(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        assertEquals(taskStings.toString(), "[1,TASK,task1_name,NEW,task1_description,01.12.2022 - 10:10,30, " +
                "2,EPIC,epic1,IN_PROGRESS,epic1_description,01.12.2022 - 10:10,35, 3,SUBTASK,subtask1," +
                "NEW,subtask1_description,2,01.12.2022 - 10:10,15, 4,SUBTASK,subtask2,DONE,subtask2_description," +
                "2,01.12.2022 - 10:25,20]");
    }

    @Test
    public void fileBackedTaskManagerShouldLoadMultipleTasks() {
        FileBackedTaskManager fm = new FileBackedTaskManager();
        File testFile = FileBackedTaskManager.setLoadFile("tempTestFile.csv");

        try (Writer fw = new FileWriter(testFile)) {
            fw.write("id,type,name,status,description,epic,date-time,duration\n" +
                    "1,TASK,task1_name,NEW,task1_description,01.12.2022 - 10:10,30\n" +
                    "2,EPIC,epic1,IN_PROGRESS,epic1_description,01.12.2022 - 10:10,35\n" +
                    "3,SUBTASK,subtask1,NEW,subtask1_description,2,01.12.2022 - 10:10,15\n" +
                    "4,SUBTASK,subtask2,DONE,subtask2_description,2,01.12.2022 - 10:25,20\n");
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл");
        }
        fm = FileBackedTaskManager.loadFromFile(testFile);
        fm.save();
        File saveFile = FileBackedTaskManager.getSaveFile();

        List<String> taskStings = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(saveFile, StandardCharsets.UTF_8))) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (!line.startsWith("id")) {
                    taskStings.add(line);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
        assertEquals(taskStings.toString(), "[1,TASK,task1_name,NEW,task1_description,01.12.2022 - 10:10,30, " +
                "2,EPIC,epic1,IN_PROGRESS,epic1_description,01.12.2022 - 10:10,35, 3,SUBTASK,subtask1," +
                "NEW,subtask1_description,2,01.12.2022 - 10:10,15, 4,SUBTASK,subtask2,DONE,subtask2_description," +
                "2,01.12.2022 - 10:25,20]");
        testFile.delete();
    }


}
