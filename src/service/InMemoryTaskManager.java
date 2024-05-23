package service;

import com.sun.source.tree.Tree;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    protected Map<Integer, Epic> epics;
    protected List<Integer> subtasksIds = new ArrayList<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    private final Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        epic.setStatus(TaskStatus.NEW);
        epics.put(id, epic);
        return epics.get(id);
    }

    @Override
    public Subtask createNewSubtask(Subtask subtask) {
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubtasksIds(id);
        setEpicStart(epicId);
        setEpicStatus(epicId);
        if (subtask.getStartTime() == null || !isTimeCross(epic)) {
            subtasks.put(id, subtask);
        }
        if (subtask.getStartTime() != null) {
            prioritizedTasks.add(subtask);
        }
        return subtasks.get(id);
    }

    @Override
    public Task createNewTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        if (task.getStartTime() == null || !isTimeCross(task)) {
            tasks.put(id, task);
        }
        if (task.getStartTime() != null) {
            prioritizedTasks.add(task);
        }
        return tasks.get(id);
    }

    // методы для удаления задач по id
    @Override
    public void deleteTaskById(int id) {
        if (tasks.get(id).getStartTime() != null) {
            prioritizedTasks.remove(tasks.get(id));
        }
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        List<Integer> subtasksIds = epic.getSubtasksIds();
        for (int subtaskId : subtasksIds) {
            historyManager.remove(subtaskId);
            subtasks.remove(subtaskId);
        }
        epic.clearSubtaskIds();
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubtasksIds(id);
        historyManager.remove(id);
        subtasks.remove(id);
        setEpicStatus(epicId);
        setEpicStart(epicId);
        if (subtask.getStartTime() != null) {
            prioritizedTasks.remove(subtask);
        }
    }

    // методы для очистки задач

    @Override
    public void clearAllSubtasks() {
        for (int id : subtasks.keySet()) {
            Subtask subtask = subtasks.get(id);
            int epicId = subtask.getEpicId();
            Epic epic = epics.get(epicId);
            epic.clearSubtaskIds();
            historyManager.remove(id);
            subtasks.clear();
            subtasksIds.clear();
            epic.setStatus(TaskStatus.NEW);
            epics.put(epicId, epic);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.remove(subtask);
            }
        }
    }

    @Override
    public void clearAllTasks() {
        for (int id : tasks.keySet()) {
            historyManager.remove(id);
            if (tasks.get(id).getStartTime() != null) {
                prioritizedTasks.remove(tasks.get(id));
            }
        }
        tasks.clear();
    }

    @Override
    public void clearAllEpics() {
        subtasks.clear();
        for (int id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void clearAll() {
        clearAllTasks();
        clearAllEpics();
        historyManager.clearHistory();
        prioritizedTasks.clear();
    }

    //остальные методы
    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }


    @Override
    public Epic getEpicById(int id) {
        setEpicStatus(id); // защита от внешнего setStatus для epic - пересчет статуса перед записью в историю
        setEpicStart(id);
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) {
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
    public void updateTask(Task task) {
        int id = task.getId();
        if (tasks.containsKey(id)) {
            if (tasks.get(id).getStartTime() != null && !isTimeCross(task)) {
                prioritizedTasks.remove(tasks.get(id));
                if (task.getStartTime() != null && !isTimeCross(task)) {
                    prioritizedTasks.add(task);
                    tasks.put(id, task);
                }
            }

        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        int id = subtask.getId();
        if (subtasks.containsKey(id)) {
            if (subtasks.get(id).getStartTime() != null && !isTimeCross(subtask)) {
                prioritizedTasks.remove(subtasks.get(id));
                if (subtask.getStartTime() != null && !isTimeCross(subtask)) {
                    prioritizedTasks.add(subtask);
                    subtasks.put(id, subtask);
                }
            }

            int epicId = subtask.getEpicId();
            setEpicStatus(epicId);
            setEpicStart(epicId);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        int id = epic.getId();
        if (epics.containsKey(id)) {
            Epic middleEpic = epics.get(id);
            List<Integer> subtasksIds = middleEpic.getSubtasksIds();
            for (int subtaskId : subtasksIds) {
                epic.addSubtasksIds(subtaskId);
            }
            setEpicStatus(id);
            setEpicStart(id);
            epics.put(id, epic);
        }
    }

    @Override
    public List<Task> getAllTasks() {
//        List<Task> tasksList = new ArrayList<>();
        //        for (Task task : tasks.values()) {
//            tasksList.add(task);
//        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
//        ArrayList<Epic> epicsList = new ArrayList<>();
//        for (Epic epic : epics.values()) {
//            epicsList.add(epic);
//        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
//        List<Subtask> subtasksList = new ArrayList<>();
//        for (Subtask subtask : subtasks.values()) {
//            subtasksList.add(subtask);
//        }
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
//        new ArrayList<>(epics.get(id).getSubtasksIds()).stream().map(subtasksId -> subtasks.get(subtasksId)).collect(Collectors.toList());
//
//        Epic epic = epics.get(id);
//        List<Subtask> epicSubtasks = new ArrayList<>();
//        subtasksIds = epic.getSubtasksIds();
//        for (int subtasksIds : subtasksIds) {
//            epicSubtasks.add(subtasks.get(subtasksIds));
//        }
        return epics.get(id).getSubtasksIds().stream().map(subtasks::get).toList();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }


    // вспомогательный метод для автоматической установки статуса эпиков
    private void setEpicStatus(int id) {
        Epic epic = epics.get(id);
        List<TaskStatus> statusList = epics.get(id).getSubtasksIds().stream().map(subtasks::get).map(Task::getStatus).
                toList();
//        subtasksIds = epic.getSubtasksIds();
//        List<TaskStatus> statusList = new ArrayList<>();
//        for (int subtaskId : subtasksIds) {
//            Subtask subtask = subtasks.get(subtaskId);
//            TaskStatus subTaskStatus = subtask.getStatus();
//            statusList.add(subTaskStatus);
//        }
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

    private void setEpicStart(int id) {
        Epic epic = epics.get(id);
        subtasksIds = epic.getSubtasksIds();
        if (epic.getSubtasksIds() != null) {
            LocalDateTime startTime = subtasksIds.stream().
                    map(subtaskId -> subtasks.get(subtaskId).getStartTime()).
                    filter(Objects::nonNull).sorted().
                    findFirst().orElse(null);
            //в логике кода null - тоже результат
            epic.setStartTime(startTime);
            if (startTime != null) {
                Duration epicDuration = Duration.ofMinutes(subtasksIds.stream().
                        map(subtaskId -> subtasks.get(subtaskId).
                        getDuration()).filter(Objects::nonNull).
                        map(Duration::toMinutes).
                        reduce(0L, Long::sum));
                epic.setDuration(epicDuration);
                LocalDateTime epicEndTime = epic.getStartTime().plusMinutes(epic.getDuration().toMinutes());
                epic.setEndTime(epicEndTime);
            }
        }
    }

    private boolean isTimeCross(Task task) {
        return prioritizedTasks.stream().anyMatch(pt -> (task.getStartTime() != null) && (task.getDuration() != null)
                && ((task.getEndTime().isAfter(pt.getStartTime()))
                && (task.getStartTime().isBefore(pt.getEndTime()))));
    }
}
