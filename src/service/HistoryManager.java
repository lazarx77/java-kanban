package service;

import model.Subtask;
import model.Task;
import model.Epic;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {

    void add(Task task);

    void add(Epic epic);

    void add(Subtask subtask);

    void clearHistory();

    List<Task> getHistory();
}
