package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> history = new ArrayList<>();
    static final int HISTORY_LIST_LIMIT = 10;

    @Override
    public void add(Task task) {
        if (history.size() >= HISTORY_LIST_LIMIT) {
            history.removeFirst();
        }
        try {
            history.add(task.clone());
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearHistory() {
        history.clear();
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
