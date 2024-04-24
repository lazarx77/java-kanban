package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private Integer id;

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
        result = result + ", status=" + getStatus() + " " + "subtasksIds= " + subtasksIds;
        return result + '}';
    }
}
