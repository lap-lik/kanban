package model;

import service.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private Integer epicId;

    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Integer epicId) {
        super(name, description, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
    }
    public Subtask(Subtask task) {
        super(task);
        epicId = task.getEpicId();
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return String.format("%d,%s,%s,%s,%s,%s,%s,%d\n", id, Type.SUBTASK, name, status, description,
                localDateTimeToString(startTime), durationToString(duration), epicId);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Subtask)) return false;
        if (!super.equals(object)) return false;
        Subtask subtask = (Subtask) object;
        return Objects.equals(epicId, subtask.epicId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }

//    @Override
//    public boolean equals(Object object) {
//        if (this == object) return true;
//        if (!(object instanceof Subtask)) return false;
//        Subtask subtask = (Subtask) object;
//        return duration == subtask.duration && Objects.equals(id, subtask.id) &&
//                Objects.equals(name, subtask.name) && Objects.equals(description, subtask.description) &&
//                status == subtask.status && Objects.equals(startTime, subtask.startTime) &&
//                Objects.equals(epicId, subtask.epicId);
//    }
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, description, status, startTime, duration, epicId);
//    }
}
