package service;

import model.Task;

import java.util.List;

/**
 * Интерфейс для менеджера истории задач.
 * Методы реализованы в InMemoryHistoryManager
 */

public interface HistoryManager {

    void add(Task task);

    void clearHistory();

    void remove(int id);

    List<Task> getHistory();
}
