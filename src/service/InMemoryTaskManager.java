package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private Map<Integer, Task> tasks;
    private Map<Integer, Subtask> subtasks;
    private Map<Integer, Epic> epics;
    private List<Integer> subtasksIds = new ArrayList<>();
    private HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    // методы для создания задач
    @Override
    public Epic createNewEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        epic.setStatus(TaskStatus.NEW);
        return epics.get(id);
    }


    public void printEpics() {
        System.out.println(epics.values());
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtasksIds(id);
        setEpicStatus(epicId);

        return subtasks.get(id);
    }

    @Override
    public Task createNewTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return tasks.get(id);
    }

    // методы для удаления задач по id
    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        for (int subtaskId : subtasksIds) {
            subtasks.remove(subtaskId);
        }
        epic.clearSubtaskIds();
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtasksIds(id);
        subtasks.remove(id);
        setEpicStatus(epicId);
    }
    // методы для очистки задач
    @Override
    public void clearAllSubtasks() {
        for (int id : subtasks.keySet()) {
            Subtask subtask = subtasks.get(id);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.clearSubtaskIds();
            subtasks.clear();
            subtasksIds.clear();
            epic.setStatus(TaskStatus.NEW);
            epics.put(epicId, epic);
        }
    }

    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void clearAll() {
        clearAllTasks();
        clearAllEpics();
    }

    //остальные методы
    @Override
    public Task getTaskById(int id) {
        historyManager = Managers.getDefaultHistory();
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }


    @Override
    public Epic getEpicById(int id) {
        historyManager = Managers.getDefaultHistory();
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager = Managers.getDefaultHistory();
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void printAllTasks() {
        System.out.println(tasks + "\n");
        System.out.println(epics + "\n");
        System.out.println(subtasks + "\n");
    }

    @Override
    public void updateTask(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            tasks.put(id, task);
        }
    }

    @Override
    public void updateSubTask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtasks.put(id, subtask);
            int epicId = subtask.getEpicId();
            setEpicStatus(epicId);
        }
    }

    @Override
    public void updateEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            List<Integer> subtasksIds = epic.getSubtasksIds();
            setEpicStatus(id);
            epics.put(id, epic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    @Override
    public List<Epic> getAllEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        List<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        List<Subtask> epicSubtasks = new ArrayList<>();
        subtasksIds = epic.getSubtasksIds();
            for (int subtasksIds : subtasksIds) {
            epicSubtasks.add(subtasks.get(subtasksIds));
        }
        return epicSubtasks;
    }

    // вспомогательный метод для автоматической установки статуса эпиков
    private void setEpicStatus(int id) {
        Epic epic = epics.get(id);
        subtasksIds = epic.getSubtasksIds();
        List<TaskStatus> statusList = new ArrayList<>();
        for (int subtaskId : subtasksIds) {
            Subtask subtask = subtasks.get(subtaskId);
            TaskStatus subTaskStatus = subtask.getStatus();
            statusList.add(subTaskStatus);
        }
        if (statusList.contains(TaskStatus.NEW) && !statusList.contains(TaskStatus.DONE) &&
                !statusList.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusList.contains(TaskStatus.DONE) && !statusList.contains(TaskStatus.NEW) &&
                !statusList.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
