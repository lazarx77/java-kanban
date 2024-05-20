package model;

import service.TaskType;

public class Subtask extends Task {
    private int epicId;

    public int setEpicId(int epicId) {
        this.epicId = epicId;
        return epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {

        String result = "Subtask{" +
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

        result = result + ", status=" + getStatus() + " " + "epicId = " + epicId;
        return result + '}';
    }


}


