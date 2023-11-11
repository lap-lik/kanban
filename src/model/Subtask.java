package model;

import service.Type;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String text, Integer epicId) {
        super(name, text);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%d\n", id, Type.SUBTASK, name, status, text, epicId);
    }
}
