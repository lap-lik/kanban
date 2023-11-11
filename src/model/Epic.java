package model;

import service.Type;

import java.util.ArrayList;
import java.util.Arrays;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;

    public Epic(String name, String text) {
        super(name, text);
        subtasksIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,\n", id, Type.EPIC, name, status, text);
    }
}
