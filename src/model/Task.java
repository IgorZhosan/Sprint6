package model;
public class Task {
    private String title;
    private String description;
    private long id;
    private Status status = Status.NEW;


    public Task(String title, String description, long id) {
        this.title = title;
        this.description = description;
        this.id = id;
    }

    public Task(String title, String description, long id, Status status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
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
}