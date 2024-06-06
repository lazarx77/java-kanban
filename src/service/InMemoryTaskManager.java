package service;

import exceptions.HashMapIsEmptyException;
import exceptions.HttpTaskNotFoundException;
import exceptions.InMemoryTaskNotFoundException;
import exceptions.TimeCrossException;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected int id = 0;
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Subtask> subtasks;
    protected Map<Integer, Epic> epics;
    protected List<Integer> subtasksIds = new ArrayList<>();
    protected HistoryManager historyManager = Managers.getDefaultHistory();
    protected Set<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

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
        subtask.setId(id + 1);
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (subtask.getStartTime() != null) {
            if (!isTimeCross(subtask)) {
                prioritizedTasks.add(subtask);
            } else {
                throw new TimeCrossException("Подзадача " + subtask.getId() + " не добавлена: " +
                        "пересечение задач по времени.");
            }
        }
        id++;
        subtasks.put(id, subtask);
        epic.addSubtasksIds(id);
        setEpicStart(epicId);
        setEpicStatus(epicId);
        return subtasks.get(id);
    }

    @Override
    public Task createNewTask(Task task) {
        task.setId(id + 1);
        if (task.getStartTime() != null) {
            if (!isTimeCross(task)) {
                prioritizedTasks.add(task);
            } else {
                throw new TimeCrossException("Задача " + task.getId() + " не добавлена: " +
                        "пересечение задач по времени.");
            }
        }
        id++;
        tasks.put(id, task);
        return tasks.get(id);
    }

    // методы для удаления задач по id
    @Override
    public void deleteTaskById(int id) {
        if (tasks.get(id) != null) {
            if (tasks.get(id).getStartTime() != null) {
                prioritizedTasks.remove(tasks.get(id));
            }
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Task c id=" + id + " отсутствеет в tasks");
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.get(id) != null) {
            Epic epic = epics.get(id);
            List<Integer> subtasksIds = epic.getSubtasksIds();
            for (int subtaskId : subtasksIds) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
            epic.clearSubtaskIds();
            historyManager.remove(id);
            epics.remove(id);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Epic с id=" + id + " отсутствует в epics");
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.get(id) != null) {
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
        } else {
            throw new InMemoryTaskNotFoundException("Задача Subtask с id=" + id + " отсутствует в subtasks");
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
        if (tasks.get(id) != null) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Task с указанным id =" + id + " в tasks не найдена");
        }
    }


    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) != null) {
            setEpicStatus(id); // защита от внешнего setStatus для epic - пересчет статуса перед записью в историю
            setEpicStart(id);
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Epic с указанным id = " + id + "в epics не найдена");
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) != null) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Subtask с указанным id = " + id + "в subtasks не найдена");
        }
    }

    @Override
    public void printAllTasks() {
        System.out.println(tasks + "\n");
        System.out.println(epics + "\n");
        System.out.println(subtasks + "\n");
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            if (task.getStartTime() != null) {
                if (prioritizedTasks.contains(task)) {
                    prioritizedTasks.remove(tasks.get(task.getId()));
                }
                if (!isTimeCross(task)) {
                    prioritizedTasks.add(task);
                } else {
                    throw new TimeCrossException("Задача " + task.getId() + " не обновлена: " +
                            "пересечение задач по времени.");
                }
            }
            tasks.put(task.getId(), task);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Task с указанным id = " + task.getId() + "не найдена");
        }
    }

    @Override
    public void updateSubTask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            if (subtask.getStartTime() != null) {
                if (prioritizedTasks.contains(subtask)) {
                    prioritizedTasks.remove(subtasks.get(subtask.getId()));
                }
                if (!isTimeCross(subtask)) {
                    prioritizedTasks.add(subtask);
                } else {
                    throw new TimeCrossException("Подзадача" + subtask.getId() + " не обновлена: " +
                            "пересечение задач по времени.");
                }
            }
            id++;
            tasks.put(id, subtask);
            int epicId = subtask.getEpicId();
            setEpicStatus(epicId);
            setEpicStart(epicId);
        } else {
            throw new InMemoryTaskNotFoundException("Задача Subtask с указанным id = " + subtask.getId() + "не найдена");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
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
        } else {
            throw new InMemoryTaskNotFoundException("Задача Epic с указанным id = " + epic.getId() + "не найдена");
        }
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Subtask> getEpicSubtasks(int id) {
        return epics.get(id).getSubtasksIds().stream().map(subtasks::get).toList();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    //метод для нахождения пересечения задач по времени
    protected boolean isTimeCross(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(pt -> (task.getStartTime() != null) && (task.getDuration() != null)
                        && ((task.getEndTime().isAfter(pt.getStartTime()))
                        && (task.getStartTime().isBefore(pt.getEndTime()))));
    }

    // вспомогательный метод для автоматической установки статуса эпиков
    private void setEpicStatus(int id) {
        Epic epic = epics.get(id);
        List<TaskStatus> statusList = epics.get(id).getSubtasksIds().stream().map(subtasks::get).map(Task::getStatus)
                .toList();
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

    //метод для установки начала времени эпиков
    private void setEpicStart(int id) {
        Epic epic = epics.get(id);
        subtasksIds = epic.getSubtasksIds();
        if (epic.getSubtasksIds() != null) {
            LocalDateTime startTime = subtasksIds.stream()
                    .map(subtaskId -> subtasks.get(subtaskId).getStartTime())
                    .filter(Objects::nonNull).sorted()
                    .findFirst().orElse(null);
            //в логике кода null - тоже результат
            epic.setStartTime(startTime);
            if (startTime != null) {
                Duration epicDuration = Duration.ofMinutes(subtasksIds.stream()
                        .map(subtaskId -> subtasks.get(subtaskId).getDuration()).filter(Objects::nonNull)
                        .map(Duration::toMinutes)
                        .reduce(0L, Long::sum));
                epic.setDuration(epicDuration);
                LocalDateTime epicEndTime = epic.getStartTime().plusMinutes(epic.getDuration().toMinutes());
                epic.setEndTime(epicEndTime);
            }
        }
    }
}
