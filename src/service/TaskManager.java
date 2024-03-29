package service;

import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;
    private ArrayList<Integer> subtasksIds = new ArrayList<>();

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    // методы для создания задач
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

    public Task createNewTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return tasks.get(id);
    }

    // методы для удаления задач по id
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
        for (int subtaskId : subtasksIds) {
            subtasks.remove(subtaskId);
        }
        epic.clearSubtaskIds();
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtasksIds(id);
        subtasks.remove(id);
        setEpicStatus(epicId);
    }
    // методы для очистки задач
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

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void clearAll() {
        clearAllTasks();
        clearAllEpics();
    }

    //остальные методы
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void printAllTasks() {
        System.out.println(tasks + "\n");
        System.out.println(epics + "\n");
        System.out.println(subtasks + "\n");
    }

    public void updateTask(int id) {
        if (tasks.containsKey(id)) {
            Task task = tasks.get(id);
            tasks.put(id, task);
        }
    }

    public void updateSubTask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id);
            subtasks.put(id, subtask);
            int epicId = subtask.getEpicId();
            setEpicStatus(epicId);
        }
    }

    public void updateEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            ArrayList<Integer> subtasksIds = epic.getSubtasksIds();
            setEpicStatus(id);
            epics.put(id, epic);
        }
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public ArrayList<Epic> getAllEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public ArrayList<Subtask> getAllSubtatks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    public ArrayList<Subtask> getEpicSubtasks(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        subtasksIds = epic.getSubtasksIds();
            for (int subtasksIds : subtasksIds) {
            epicSubtasks.add(subtasks.get(subtasksIds));
        }
        return epicSubtasks;
    }

    private void setEpicStatus(int id) {
        Epic epic = epics.get(id);
        subtasksIds = epic.getSubtasksIds();
        ArrayList<TaskStatus> statusList = new ArrayList<>();
        for (int subtaskId : subtasksIds) {
            Subtask subtask = subtasks.get(subtaskId);
            TaskStatus subTaskStatus = subtask.getStatus();
            statusList.add(subTaskStatus);
        }
        if (statusList.contains(TaskStatus.NEW) && !statusList.contains(TaskStatus.DONE) && !statusList.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.NEW);
        } else if (statusList.contains(TaskStatus.DONE) && !statusList.contains(TaskStatus.NEW) && !statusList.contains(TaskStatus.IN_PROGRESS)) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }
}
