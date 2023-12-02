package model;

import service.Type;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static constants.Constants.LOCAL_DATA_TIME_FORMAT;

public class Task {
    protected Integer id;
    protected String name;
    protected String description;
    protected Status status;
    protected LocalDateTime startTime;
    protected Duration duration;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String name, String description, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.startTime = startTime;
    }

    public Task(String name, String description, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Task task) {
        id = task.getId();
        name = task.getName();
        description = task.getDescription();
        status = task.getStatus();
        startTime = task.getStartTime();
        duration = task.getDuration();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        if (duration == null) {
            return startTime;
        }
        return startTime.plus(duration);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Task)) return false;
        Task task = (Task) object;
        return Objects.equals(duration, task.duration) && Objects.equals(id, task.id) &&
                Objects.equals(name, task.name) && Objects.equals(description, task.description) &&
                status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, startTime, duration);
    }
}
