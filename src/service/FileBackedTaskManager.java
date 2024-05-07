package service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.io.FileReader;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;


public class FileBackedTaskManager extends InMemoryTaskManager {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        File testFile = new File ("testFile.txt");

        fileBackedTaskManager.loadFromFile(testFile);



//        Task task1 = new Task(); // id 1
//        task1.setTaskName("task1_name");
//        task1.setDescription("task1_description");
//        task1.setStatus(TaskStatus.NEW);
//        Task fileTask1 = fileBackedTaskManager.createNewTask(task1);
        //task1 = taskManager.createNewTask(task1);


//        Task task2 = new Task(); // id 2
//        task2.setTaskName("task2_name");
//        task2.setDescription("task2_description");
//        task2.setStatus(TaskStatus.IN_PROGRESS);
//        Task fileTask2 = fileBackedTaskManager.createNewTask(task2);
        //task2 = taskManager.createNewTask(task2);

//        Epic epic1 = new Epic(); // id 3
//        epic1.setTaskName("epic1_name");
//        epic1.setDescription("epic1_description");
//        Epic fileEpic1 = fileBackedTaskManager.createNewEpic(epic1);
//        epic1 = taskManager.createNewEpic(epic1);

//        Subtask subtask1 = new Subtask(); // id 4
//        subtask1.setTaskName("subtask1_name");
//        subtask1.setDescription("subtask1_description");
//        subtask1.setStatus(TaskStatus.NEW);
//        subtask1.setEpicId(epic1.getId());
//        Subtask fileSubtask1 = fileBackedTaskManager.createNewSubtask(subtask1);
        //subtask1 = taskManager.createNewSubtask(subtask1);

//        Subtask subtask2 = new Subtask(); // id 5
//        subtask2.setTaskName("subtask2_name");
//        subtask2.setDescription("subtask2_description");
//        subtask2.setStatus(TaskStatus.DONE);
//        subtask2.setEpicId(epic1.getId());
//        Subtask fileSubtask2 = fileBackedTaskManager.createNewSubtask(subtask2);
        //subtask2 = taskManager.createNewSubtask(subtask2);

//        Subtask subtask3 = new Subtask(); // id 6
//        subtask3.setTaskName("subtask3_name");
//        subtask3.setDescription("subtask3_description");
//        subtask3.setStatus(TaskStatus.DONE);
//        subtask3.setEpicId(epic1.getId());
//        Subtask fileSubtask3 = fileBackedTaskManager.createNewSubtask(subtask3);
        //subtask3 = taskManager.createNewSubtask(subtask3);

//        Epic epic2 = new Epic(); // id 7
//        epic2.setTaskName("epic2_name");
//        epic2.setDescription("epic2_description");
//        Epic fileEpic2 = fileBackedTaskManager.createNewEpic(epic2);
//        epic2 = taskManager.createNewEpic(epic2);

        //taskManager.getTaskById(2);
        System.out.println(fileBackedTaskManager.getAllTasks());
        System.out.println(fileBackedTaskManager.getAllEpics());
        System.out.println(fileBackedTaskManager.getAllSubtasks());
        //System.out.println(historyManager.getHistory());
        //taskManager.getEpicById(3);
        //System.out.println(historyManager.getHistory());
        //taskManager.getSubtaskById(4);
        //System.out.println(historyManager.getHistory());
        //taskManager.getSubtaskById(6);
        //System.out.println(historyManager.getHistory());
        //taskManager.getEpicById(3);
        //System.out.println(historyManager.getHistory());
        //taskManager.deleteTaskById(2);
        //System.out.println(historyManager.getHistory());
        //taskManager.deleteEpic(3);
        //taskManager.getEpicById(7);
        //System.out.println(historyManager.getHistory());
    }

    //private TaskType type;
    private File file = new File("C:\\Users\\Port\\dev\\java-kanban", "savedTasks.txt");
    private static final String CSV_HEADER = "id,type,name,status,description,epic";
//    private Map<Integer, Task> tasks;
//    private Map<Integer, Subtask> subtasks;
//    private Map<Integer, Epic> epics;

    //loadFromFile(file);

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

    public void save() {
        try (Writer fileWriter = new FileWriter(file, StandardCharsets.UTF_8)) {
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

            //fileWriter.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл в save");
        }
    }


    private String toString(Task task) {
        int id = task.getId();
        String result;
        if (task.toString().startsWith("Task")) {
            result = id + "," + TaskType.TASK + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription();
        } else if (task.toString().startsWith("Epic")) {
            result = id + "," + TaskType.EPIC + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription();
        } else {
            Subtask middleSubtask = (Subtask) task;
            result = id + "," + TaskType.SUBTASK + "," + task.getTaskName(id) + "," + task.getStatus() + "," +
                    task.getDescription() + "," + middleSubtask.getEpicId();
        }
        return result;
    }

    private Task fromString(String string) {
        String[] split = string.split(",");

        switch (TaskType.valueOf(split[1])) {
            case EPIC:
                Epic epic = new Epic();
                epic.setId(Integer.parseInt(split[0]));
                epic.setTaskName(split[2]);
                epic.setStatus(TaskStatus.valueOf(split[3]));
                epic.setDescription(split[4]);
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setTaskName(split[2]);
                subtask.setStatus(TaskStatus.valueOf(split[3]));
                subtask.setDescription(split[4]);
                subtask.setEpicId(Integer.parseInt(split[5]));
                return subtask;
            case TASK:
                Task task = new Task();
                task.setId(Integer.parseInt(split[0]));
                task.setTaskName(split[2]);
                task.setStatus(TaskStatus.valueOf(split[3]));
                task.setDescription(split[4]);
                return task;
            default:
                return null;
        }
    }


    public void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                if (!line.startsWith("id")) {
                    Task task = fromString(line);
                    if (task.getId() > super.id) {
                        super.id = task.getId();
                    }
                    switch (task.getType()) {
                        case EPIC:
                            epics.put(task.getId(), (Epic) task);
                            break;
                        case SUBTASK:
                            Epic epic = new Epic();

                            subtasks.put(task.getId(), (Subtask) task);
                        default:
                            tasks.put(task.getId(), task);
                    }
                }
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла в loadFromFile");
        }
    }
}
