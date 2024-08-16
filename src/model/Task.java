package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private String title;
    private String description;
    private static int id = 0;
    private final int idTask;
    private Status status = Status.NEW;
    private final TaskType type;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Task(String title) {
        this(title, null, Status.NEW, TaskType.TASK);
    }

    public Task(String title, String description) {
        this(title, description, Status.NEW, TaskType.TASK);
    }

    public Task(String title, String description, Status status) {
        this(title, description, status, TaskType.TASK);
    }

    public Task(int id, String title, String description, Status status) {
        this(id, title, description, status, TaskType.TASK);
    }

    public Task(int id, String title, String description, Status status, TaskType type) {
        this.title = title;
        this.description = description;
        this.idTask = id;
        this.status = status;
        this.type = type;
    }

    public Task(String title, String description, Status status, TaskType type) {
        this.title = title;
        this.description = description;
        this.idTask = generateId();
        this.status = status;
        this.type = type;
    }

    public Task(String title, String description, Status status, TaskType type, LocalDateTime startTime, Duration duration) {
        this.title = title;
        this.description = description;
        this.idTask = generateId();
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = calculateEndTime();
    }

    public Task(int id, String title, String description, Status status, TaskType type, LocalDateTime startTime, Duration duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = calculateEndTime();
        this.idTask = generateId();
    }

    private int generateId() {
        return ++id;
    }

    public int getId() {
        return idTask;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime calculateEndTime() {
        if (this.startTime != null && this.duration != null) {
            return this.startTime.plus(this.duration);
        }
        return null;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idTask=" + idTask +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
