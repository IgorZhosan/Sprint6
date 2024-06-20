package model;

public class Task {
    private String title;
    private String description;
    private static int id = 0;
    private final int idTask;
    private Status status = Status.NEW;

    public Task(String title) {
        this.title = title;
        this.description = null;
        this.idTask = generateId();
    }

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
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

    @Override
    public String toString() {
        return "Task{" +
                "id=" + idTask +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}