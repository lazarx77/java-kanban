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

//    public void setStartTime(Map<Integer, Subtask> subtasks, List<Integer> subtasksIds) {
//        starTime = (subtasksIds.stream().map(subtasks::get).map(subtask -> subtask.startTime).min(LocalDateTime::compareTo)).orElse(LocalDateTime.now());
//    }
//
//    public void setDuration(Map<Integer, Subtask> subtasks, List<Integer> subtasksIds){
//        long middleDuration = 0;
//        for(Integer subtaskId : subtasksIds) {
//            middleDuration = middleDuration + subtasks.get(subtaskId).duration;
//        }
//        duration = middleDuration;
//
//
//        //return duration = subtasksIds.stream().
//    }


    public void setEndTime(LocalDateTime starTime) {
        endTime = starTime.plusMinutes(duration);
    }

    public LocalDateTime getEpicEndTime(){
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
            result = result + ", startTime= 00:00";
        }
        if (duration != 0) {
            result = result + ", duration= " + duration;
        } else {
            result = result + ", duration= 0";
        }
        result = result + ", status=" + getStatus() + " " + "subtasksIds= " + subtasksIds;
        return result + '}';
    }
}
