package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    public List<Task> history = new ArrayList<>();
    private final int HISTORY_LIST_LIMIT = 10;

    @Override
    public void add(Task task) {
        Task middleTask = new Task();
        middleTask.setTaskName(task.getTaskName(task.getId()));
        middleTask.setDescription(task.getDescription());
        middleTask.setStatus(task.getStatus());
        middleTask.setId(task.getId());
        if (history.size() < HISTORY_LIST_LIMIT) {
            history.add(middleTask);
        } else {
            history.removeFirst();
            history.add(middleTask);
        }
    }

    @Override
    public void add(Epic epic) {
        ArrayList<Integer> middleSubtasksIds = epic.getSubtasksIds();
        Epic middleEpic = new Epic();
        middleEpic.setTaskName(epic.getTaskName(epic.getId()));
        middleEpic.setDescription(epic.getDescription());
        middleEpic.setStatus(epic.getStatus());
        middleEpic.setId(epic.getId());
        for (int id : middleSubtasksIds){
            middleEpic.addSubtasksIds(id);
        }
        if (history.size() < HISTORY_LIST_LIMIT) {
            history.add(middleEpic);
        } else {
            history.removeFirst();
            history.add(middleEpic);
        }
    }

    @Override
    public void add(Subtask subtask) {
        Subtask middleSubtask = new Subtask();
        middleSubtask.setTaskName(subtask.getTaskName(subtask.getId()));
        middleSubtask.setDescription(subtask.getDescription());
        middleSubtask.setStatus(subtask.getStatus());
        middleSubtask.setId(subtask.getId());
        middleSubtask.setEpicId(subtask.getEpicId());
        if (history.size() < HISTORY_LIST_LIMIT) {
            history.add(middleSubtask);
        } else {
            history.removeFirst();
            history.add(middleSubtask);
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
