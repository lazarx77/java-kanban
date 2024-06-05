package service;

import exceptions.ManagerSaveException;
import exceptions.TimeCrossException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private static File loadFile;
    private static final File SAVE_FILE = new File("savedTasks.csv");
    private static final String CSV_HEADER = "id,type,name,status,description,epic,date-time,duration";

    public static void main(String[] args) {

        //тестируем пользовательский сценарий

        setLoadFile("testFile.csv");

        FileBackedTaskManager fm = Managers.getDefaultFileManager();
        fm = loadFromFile(loadFile);


        Task task8 = new Task(); // id 8
        task8.setTaskName("task8_name");
        task8.setDescription("task8_description");
        task8.setStatus(TaskStatus.NEW);
        task8.setStartTime(LocalDateTime.of(2023, 12, 15, 10, 11));
        task8.setDuration(Duration.ofMinutes(10));
        Task fileTask1 = fm.createNewTask(task8);

        Task task9 = new Task(); // id 9
        task9.setTaskName("task9_name");
        task9.setDescription("task9_description");
        task9.setStatus(TaskStatus.IN_PROGRESS);
        task9.setStartTime(LocalDateTime.of(2022, 9, 7, 5, 5));
        task9.setDuration(Duration.ofMinutes(180));
        Task fileTask2 = fm.createNewTask(task9);

        Epic epic10 = new Epic(); // id 10
        epic10.setTaskName("epic1_name");
        epic10.setDescription("epic1_description");
        Epic fileEpic1 = fm.createNewEpic(epic10);

        Subtask subtask11 = new Subtask(); // id 11
        subtask11.setTaskName("subtask11_name");
        subtask11.setDescription("subtask11_description");
        subtask11.setStatus(TaskStatus.NEW);
        subtask11.setStartTime(LocalDateTime.of(2023, 12, 2, 10, 11));
        subtask11.setDuration(Duration.ofMinutes(10));
        subtask11.setEpicId(epic10.getId());
        Subtask fileSubtask1 = fm.createNewSubtask(subtask11);

        Subtask subtask12 = new Subtask(); // id 12
        subtask12.setTaskName("subtask2_name");
        subtask12.setDescription("subtask2_description");
        subtask12.setStatus(TaskStatus.DONE);
        subtask12.setStartTime(LocalDateTime.of(2023, 12, 16, 10, 21));
        subtask12.setDuration(Duration.ofMinutes(10));
        subtask12.setEpicId(epic10.getId());
        Subtask fileSubtask2 = fm.createNewSubtask(subtask12);

        Subtask subtask13 = new Subtask(); // id 13
        subtask13.setTaskName("subtask3_name");
        subtask13.setDescription("subtask3_description");
        subtask13.setStatus(TaskStatus.DONE);
        subtask13.setStartTime(LocalDateTime.of(2024, 5, 1, 10, 31));
        subtask13.setDuration(Duration.ofMinutes(10));
        subtask13.setEpicId(epic10.getId());
        Subtask fileSubtask3 = fm.createNewSubtask(subtask13);

        System.out.println(fm.getAllTasks());
        System.out.println(fm.getAllEpics());
        System.out.println(fm.getAllSubtasks());
        System.out.println(fm.getAllSubtasks());
        fm.deleteTaskById(2);
        System.out.println(fm.getAllTasks());

    }

    // метод для создания файла, из которого нужно загружать ранее сохраненные задачи
    public static File setLoadFile(String fileName) {
        FileBackedTaskManager.loadFile = new File(fileName);
        return loadFile;
    }

    //метод для получения файла с сохраненными в процессе работы менеджера задачами
    public static File getSaveFile() {
        return SAVE_FILE;
    }

    //переопределение всех методов, в которых есть необходимость сохранения данных в файл - после лююбого мнесения
    // изменений в задачи

    @Override
    public Epic createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public Task createNewTask(Task task) {
        super.createNewTask(task);
        save();
        return task;
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        save();
    }

    @Override
    public void clearAll() {
        super.clearAll();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        super.updateSubTask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    //метод для сохранения задач в файл

    public void save() {
        try (Writer fileWriter = new FileWriter(SAVE_FILE, StandardCharsets.UTF_8)) {
            fileWriter.write(CSV_HEADER + "\n");

            if (!super.getAllTasks().isEmpty()) {
                for (Task task : super.getAllTasks()) {
                    fileWriter.write(toString(task) + "\n");
                }
            }

            if (!super.getAllEpics().isEmpty()) {
                for (Epic epic : super.getAllEpics()) {
                    fileWriter.write(toString(epic) + "\n");
                }
            }

            if (!super.getAllSubtasks().isEmpty()) {
                for (Subtask subtask : super.getAllSubtasks()) {
                    fileWriter.write(toString(subtask) + "\n");
                }
            }
            fileWriter.write("");

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл в save");
        }
    }

    //метод для получения задачи из строки

    public String toString(Task task) {
        int id = task.getId();
        String result;
        if (task.toString().startsWith("Task")) {
            result = id + "," + TaskType.TASK + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription() + ",";
            if (task.getStartTime() != null) {
                result = result + task.getStartTime().format(Task.formatter) + ",";
            } else {
                result = result + ",";
            }
            if (task.getDuration() != null) {
                result = result + task.getDuration().toMinutes();
            }
        } else if (task.toString().startsWith("Epic")) {
            result = id + "," + TaskType.EPIC + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription() + ",";
            if (task.getStartTime() != null) {
                result = result + task.getStartTime().format(Task.formatter) + ",";
            } else {
                result = result + ",";
            }
            if (task.getDuration() != null) {
                result = result + task.getDuration().toMinutes();
            }
        } else {
            Subtask middleSubtask = (Subtask) task;
            result = id + "," + TaskType.SUBTASK + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription() + "," + middleSubtask.getEpicId() + ",";
            if (task.getStartTime() != null) {
                result = result + task.getStartTime().format(Task.formatter) + ",";
            } else {
                result = result + ",";
            }
            if (task.getDuration() != null) {
                result = result + task.getDuration().toMinutes();
            }
        }
        return result;
    }

    //метод для получение строки из задачи

    public Task fromString(String string) {
        String[] split = string.split(",");

        switch (TaskType.valueOf(split[1])) {
            case EPIC:
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(split[0]));
                epic.setTaskName(split[2]);
                epic.setStatus(TaskStatus.valueOf(split[3]));
                epic.setDescription(split[4]);
                if (!split[5].isEmpty()) {
                    epic.setStartTime(LocalDateTime.parse(split[5], Task.formatter));
                } else {
                    epic.setStartTime(null);
                }
                if (!split[6].isEmpty()) {
                    epic.setDuration(Duration.ofMinutes(Integer.parseInt(split[6])));
                } else {
                    epic.setDuration(null);
                }
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setTaskName(split[2]);
                subtask.setStatus(TaskStatus.valueOf(split[3]));
                subtask.setDescription(split[4]);
                subtask.setEpicId(Integer.parseInt(split[5]));
                if (!split[6].isEmpty()) {
                    subtask.setStartTime(LocalDateTime.parse(split[6], Task.formatter));
                } else {
                    subtask.setStartTime(null);
                }
                if (!split[7].isEmpty()) {
                    subtask.setDuration(Duration.ofMinutes(Integer.parseInt(split[7])));
                } else {
                    subtask.setDuration(null);
                }
                return subtask;
            case TASK:
                Task task = new Task();
                task.setId(Integer.parseInt(split[0]));
                task.setTaskName(split[2]);
                task.setStatus(TaskStatus.valueOf(split[3]));
                task.setDescription(split[4]);
                if (!split[5].isEmpty()) {
                    task.setStartTime(LocalDateTime.parse(split[5], Task.formatter));
                } else {
                    task.setStartTime(null);
                }
                if (!split[6].isEmpty()) {
                    task.setDuration(Duration.ofMinutes(Integer.parseInt(split[6])));
                } else {
                    task.setDuration(null);
                }
                return task;
            default:
                return null;
        }
    }

    //метод для загрузки данных из файла для работы менеджера

    public static FileBackedTaskManager loadFromFile(File file) {

        FileBackedTaskManager middleFm = new FileBackedTaskManager();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (!line.startsWith("id")) {
                    Task task = middleFm.fromString(line);
                    if (task.getId() > middleFm.id) {
                        middleFm.id = task.getId();
                    }
                    switch (task.getType()) {
                        case EPIC:
                            middleFm.epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            if (task.getStartTime() != null) {
                                if (!middleFm.isTimeCross(task)) {
                                    middleFm.prioritizedTasks.add((Subtask) task);
                                } else {
                                    throw new TimeCrossException("Задача " + task.getId() + " не добавлена: " +
                                            "пересечение задач по времени.");
                                }
                            }
                            middleFm.subtasks.put(task.getId(), (Subtask) task);
                            break;
                        default:
                            if (task.getStartTime() != null) {
                                if (!middleFm.isTimeCross(task)) {
                                    middleFm.prioritizedTasks.add(task);
                                } else {
                                    throw new TimeCrossException("Задача " + task.getId() + " не добавлена: " +
                                            "пересечение задач по времени.");
                                }
                            }
                            middleFm.tasks.put(task.getId(), task);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла в loadFromFile");
        }
        return middleFm;
    }
}
