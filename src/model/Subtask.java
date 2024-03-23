package model;

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
    public String toString() {

        String result = "Subtask{" +
                "id='" + getId() + " " +
                ", taskName='" + getTaskName(getId()) + " ";
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", extraInfo=null";
        }

        result = result + ", status=" + getStatus() + " " + "epicId = " +  epicId;
        return result + '}';
    }


}


