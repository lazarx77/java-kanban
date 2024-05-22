package model;

import service.TaskType;

import java.time.LocalDateTime;
import java.util.*;

public class Epic extends Task {
    private Integer id;
    private LocalDateTime endTime;
    //private long duration;
    //private LocalDateTime starTime;
    private final List<Integer> subtasksIds = new ArrayList<>();

    @Override
    public LocalDateTime getEndTime() {
        endTime = super.getEndTime();
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
                "id='" + getId() + " " +
                ", taskName= " + getTaskName(getId()) + " ";
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
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
        result = result + ", status=" + getStatus() + " " + "subtasksIds= " + subtasksIds;
        return result + '}';
    }
}
