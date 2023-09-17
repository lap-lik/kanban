package model;

import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
    private ArrayList<Integer> listSubtasksId;

    public Epic(String title) {
        super(title, null);
        listSubtasksId = new ArrayList<>();
    }

    public ArrayList<Integer> getListSubtasksId() {
        return listSubtasksId;
    }

    public void setListSubtasksId(ArrayList<Integer> listSubtasksId) {
        this.listSubtasksId = listSubtasksId;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", text='" + getText() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", listSubtasksId=" + Arrays.toString(new ArrayList[]{listSubtasksId}) +
                '}';
    }
}
