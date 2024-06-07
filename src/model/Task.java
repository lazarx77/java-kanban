package model;

import service.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {

    protected Integer id;
    protected String taskName;
    protected String description;
    protected TaskStatus status;
    protected long duration;
    protected LocalDateTime startTime;
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm");

    @Override
    public String toString() {

        String result = "Task{" +
                "id='" + id + " " +
                ", taskName= " + taskName + " ";
        if (description != null) {
            result = result + ", description.length= " + description.length();
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

        result = result + ", status=" + status + " ";
        return result + '}';
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(getDuration());
    }

    public void setDuration(Duration duration) {
        this.duration = duration.toMinutes();

    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return Duration.ofMinutes(duration);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskName(int id) {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        int hash = 31;
        if (id != null) {
            hash = hash + id;
        }
        return hash;
    }
}
