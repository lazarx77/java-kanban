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

    void updateTask(Task task);

    void updateSubTask(Subtask subtask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<Subtask> getAllSubtasks();

    List<Subtask> getEpicSubtasks(int id);

    List<Task> getPrioritizedTasks();
    HistoryManager getHistoryManager();

}
