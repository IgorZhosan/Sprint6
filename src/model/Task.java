package model;

public class Task {
    private final String title;
    private final String description;
    private static int id = 0;
    private final int idTask;
    private Status status = Status.NEW;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.idTask = generateId();
    }

    public Task(String title) {
        this.title = title;
        this.description = null;
        this.idTask = generateId();
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.idTask = generateId();
        this.status = status;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + idTask +
                ", status=" + status +
                '}';
    }
}