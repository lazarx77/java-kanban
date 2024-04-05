package service;

import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public List<Task> history = new ArrayList<>();
    private final int HISTORY_LIST_LIMIT = 10;

    @Override
    public void add(Task task) {
        String taskName = task.getTaskName(task.getId());
        int taskId = task.getId();
        String taskDescription = task.getDescription();
        TaskStatus taskStatus = task.getStatus();


        if (history.size() < HISTORY_LIST_LIMIT) {
            history.add(task);
        } else {
            history.removeFirst();
            history.add(task);
        }
    }


    @Override
    public List<Task> getHistory() {
        return history;
    }


}
