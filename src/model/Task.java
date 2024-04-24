package model;

import java.util.Objects;

public class Task {

    private Integer id;
    private String taskName;
    private String description;
    private TaskStatus status;


    @Override
    public String toString() {

        String result = "Task{" +
                "id='" + id + " " +
                ", taskName= " + taskName + " ";
        if (description != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", extraInfo=null";
        }

        result = result + ", status=" + status + " ";
        return result + '}';
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
