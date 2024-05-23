import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest extends TaskManagerTest {
    public static FileBackedTaskManager fm;// = Managers.getDefaultFileManager();
    File saveFile;
    File testFile;

    //    @BeforeEach
//    public void beforeEach() {
//        taskManager = Managers.getDefault();
//        //historyManager = Managers.getDefaultHistory();
//        task1.setTaskName("task1_name");
//        task1.setDescription("task1_description");
//        task1.setStatus(TaskStatus.NEW);
//        task1.setDuration(Duration.ofMinutes(20));
//        task1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
//        task1 = taskManager.createNewTask(task1);
//    }
//
//
//    private Task task1 = new Task(); // id 1
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        fm = Managers.getDefaultFileManager();
    }

//    @AfterAll
//    public static void afterAll() {
//       saveFile.delete();
//    }

    // проверяем сохранение задач в отсутствующий/пустой файл
    @Test
    public void fileBackedTaskManagerShouldSaveTasksToNewFile() {
        Task fmTask1 = new Task();
        fmTask1.setTaskName("task1_name");
        fmTask1.setDescription("task1_description");
        fmTask1.setStatus(TaskStatus.NEW);
        fmTask1.setDuration(Duration.ofMinutes(30));
        fmTask1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        fmTask1 = fm.createNewTask(fmTask1);
        saveFile = FileBackedTaskManager.getSaveFile();
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
        assertEquals(fm.toString(fmTask1), fm.toString(middleTask));
    }

    // проверяем сохранение задач в отсутствующий/пустой файл
    @Test
    public void fileBackedTaskManagerShouldSaveMultipleTasks() {
        //FileBackedTaskManager fm = new FileBackedTaskManager();
        Task fmTask1 = new Task();
        fmTask1.setTaskName("task1_name");
        fmTask1.setDescription("task1_description");
        fmTask1.setStatus(TaskStatus.NEW);
        fmTask1.setDuration(Duration.ofMinutes(30));
        fmTask1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        fmTask1 = fm.createNewTask(fmTask1);

        Epic fmEpic1 = new Epic();
        fmEpic1.setTaskName("epic1");
        fmEpic1.setDescription("epic1_description");
        fmEpic1 = fm.createNewEpic(fmEpic1);

        Subtask fmSubtask1 = new Subtask();
        fmSubtask1.setTaskName("subtask1");
        fmSubtask1.setDescription("subtask1_description");
        fmSubtask1.setStatus(TaskStatus.NEW);
        fmSubtask1.setDuration(Duration.ofMinutes(15));
        fmSubtask1.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 10, 0));
        fmSubtask1.setEpicId(fmEpic1.getId());
        fmSubtask1 = fm.createNewSubtask(fmSubtask1);

        Subtask fmSubtask2 = new Subtask();
        fmSubtask2.setTaskName("subtask2");
        fmSubtask2.setDescription("subtask2_description");
        fmSubtask2.setStatus(TaskStatus.DONE);
        fmSubtask2.setDuration(Duration.ofMinutes(20));
        fmSubtask2.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
        fmSubtask2.setEpicId(fmEpic1.getId());
        fmSubtask2 = fm.createNewSubtask(fmSubtask2);
        saveFile = FileBackedTaskManager.getSaveFile();

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
        //FileBackedTaskManager fm = new FileBackedTaskManager();
        testFile = FileBackedTaskManager.setLoadFile("tempTestFile.csv");

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
        saveFile = FileBackedTaskManager.getSaveFile();

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
