package model;

import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Integer> subtasksIds = new ArrayList<>();

    public ArrayList<Integer> addSubtasksIds(int id) {
        subtasksIds.add(id);
        return subtasksIds;
    }

    public ArrayList<Integer> getSubtasksIds() {
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
                ", taskName='" + getTaskName(getId()) + " ";
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", extraInfo=null";
        }
        result = result + ", status=" + getStatus() + " " + "subtasksIds= " + subtasksIds ;
        return result + '}';
    }


}
