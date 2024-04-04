package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {


    Epic createNewEpic(Epic epic);

    Subtask createNewSubtask(Subtask subtask);

    Task createNewTask(Task task);

    void deleteTaskById(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    void clearAllSubtasks();

    void clearAllTasks();

    void clearAllEpics();

    void clearAll();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void printAllTasks();

    void updateTask(int id);

    void updateSubTask(int id);

    void updateEpic(int id);

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtatks();

    ArrayList<Subtask> getEpicSubtasks(int id);

//    List<Task> getHistory();

}
