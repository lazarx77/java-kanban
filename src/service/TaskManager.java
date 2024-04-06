package service;

import model.Epic;
import model.Subtask;
import model.Task;

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

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getEpicSubtasks(int id);

}
