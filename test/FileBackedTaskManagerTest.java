import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.FileBackedTaskManager;
import service.ManagerSaveException;
import service.Managers;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest extends TaskManagerTest {
    public static FileBackedTaskManager fm;
    File saveFile;
    File testFile;

    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        fm = Managers.getDefaultFileManager();
    }

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
        Task fmTask1 = new Task();
        fmTask1.setTaskName("task1_name");
        fmTask1.setDescription("task1_description");
        fmTask1.setStatus(TaskStatus.NEW);
        fmTask1.setDuration(Duration.ofMinutes(30));
        fmTask1.setStartTime(LocalDateTime.of(2023, 12, 7, 10, 10, 0));
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
        fmSubtask1.setStartTime(LocalDateTime.of(2023, 12, 8, 10, 10, 0));
        fmSubtask1.setEpicId(fmEpic1.getId());
        fmSubtask1 = fm.createNewSubtask(fmSubtask1);

        Subtask fmSubtask2 = new Subtask();
        fmSubtask2.setTaskName("subtask2");
        fmSubtask2.setDescription("subtask2_description");
        fmSubtask2.setStatus(TaskStatus.DONE);
        fmSubtask2.setDuration(Duration.ofMinutes(20));
        fmSubtask2.setStartTime(LocalDateTime.of(2023, 12, 9, 10, 25, 0));
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
                "2,TASK,task1_name,NEW,task1_description,07.12.2023 - 10:10,30, 3,EPIC,epic1,IN_PROGRESS," +
                "epic1_description,08.12.2023 - 10:10,35, 4,SUBTASK,subtask1,NEW,subtask1_description,3," +
                "08.12.2023 - 10:10,15, 5,SUBTASK,subtask2,DONE,subtask2_description,3,09.12.2023 - 10:25,20]");
        saveFile.delete();
    }

    @Test
    public void fileBackedTaskManagerShouldLoadMultipleTasks() {
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
        saveFile.delete();
    }

    @Test
    public void managerSaveExceptionShouldBeThrownIfLoadFileIsNotPresent() {
        File nonExistent = FileBackedTaskManager.setLoadFile("nonExistent.csv");
        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(nonExistent);
        }, "Ошибка чтения файла в loadFromFile");
    }
}
