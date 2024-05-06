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

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;


public class FileBackedTaskManager extends InMemoryTaskManager {

    //private TaskType type;
    File file = new File("C:\\Users\\Port\\dev\\java-kanban", "savedTasks.txt");
    String CSV_HEADER = "id,type,name,status,description,epic";

    @Override
    public Epic createNewEpic(Epic epic) {
        super.createNewEpic(epic);
        try {
            save(epic);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл с createNewEpic" + e.getMessage());
        }
        return epic;
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        super.createNewSubtask(subtask);
        try {
            save(subtask);
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл с createNewSubtask" + e.getMessage());
        }
        return subtask;
    }

    @Override
    public Task createNewTask(Task task) {
        super.createNewTask(task);
        try {
            save(task);
        } catch (IOException e) {
            System.out.println("Ошибка записи файла с createNewTask" + e.getMessage());
        }
        return task;
    }

    public void save(Task task) throws IOException {
        Writer fileWriter = new FileWriter(file, true);
        //fileWriter.write(CSV_HEADER);
        fileWriter.write(toString(task) + "\n");
        fileWriter.close();
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
                return epic;
            case SUBTASK:
                Subtask subtask = new Subtask();
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setTaskName(split[2]);
                subtask.setStatus(TaskStatus.valueOf(split[3]));
                subtask.setEpicId(Integer.parseInt(split[4]));
                return subtask;
            default:
                Task task = new Task();
                task.setId(Integer.parseInt(split[0]));
                task.setTaskName(split[2]);
                task.setStatus(TaskStatus.valueOf(split[3]));
                return task;
        }
    }


    static void loadFromFile(File file) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
//            String taskStringFromFile = fileReader.readLine();
            BufferedReader br = new BufferedReader(fileReader);

            while (br.ready()) {
                String line = br.readLine();
                //fromString
                System.out.println(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
