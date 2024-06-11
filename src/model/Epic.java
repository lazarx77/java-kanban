package model;

import service.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс эпик-задач
 * введены дополнительные поля endTime для расчитываемого времени окончания эпик-задачи,
 * subtasksIds для хранения id водзадач, которые относятся к конкретной эпик-задаче
 */

public class Epic extends Task {
    private LocalDateTime endTime;
    private final List<Integer> subtasksIds = new ArrayList<>();

    @Override
    public LocalDateTime getEndTime() {
        if (startTime != null) {
            endTime = startTime.plus(getDuration());
        } else {
            endTime = null;
        }
        return endTime;
    }

    public List<Integer> addSubtasksIds(int id) {
        subtasksIds.add(id);
        return subtasksIds;
    }

    public List<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void removeSubtasksIds(Integer id) {
        subtasksIds.remove(id);
    }

    public void clearSubtaskIds() {
        subtasksIds.clear();
    }

    public void setEndTime(LocalDateTime starTime) {
        endTime = starTime.plusMinutes(duration);
    }

    public LocalDateTime getEpicEndTime() {
        return endTime;
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {

        String result = "Epic{" +
                "id='" + id + " " +
                ", taskName= " + taskName + " ";
        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", extraInfo=null";
        }
        if (startTime != null) {
            result = result + ", startTime= " + startTime.format(formatter);
        } else {
            result = result + ", startTime= null";
        }
        if (duration != 0) {
            result = result + ", duration= " + duration;
        } else {
            result = result + ", duration= null";
        }
        result = result + ", status=" + status + " " + "subtasksIds= " + subtasksIds;
        if (endTime != null) {
            result = result + ", endTime= " + endTime.format(formatter);
        } else {
            result = result + ", endTime= null";
        }
        return result + '}';
    }
}
