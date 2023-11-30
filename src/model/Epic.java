package model;

import service.Type;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> subtasksIds;
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        subtasksIds = new ArrayList<>();
    }

    public Epic(Epic task) {
        super(task);
        subtasksIds = task.getSubtasksIds();
        endTime = task.getEndTime();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return subtasksIds;
    }

    public void setSubtasksIds(ArrayList<Integer> subtasksIds) {
        this.subtasksIds = subtasksIds;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,\n", id, Type.EPIC, name, status, description);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Epic)) return false;
        Epic epic = (Epic) object;
        return Objects.equals(id, epic.id) && Objects.equals(name, epic.name) &&
                Objects.equals(description, epic.description) && status == epic.status &&
                Objects.equals(subtasksIds, epic.subtasksIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, subtasksIds);
    }
}
