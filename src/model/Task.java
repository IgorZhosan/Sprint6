package model;

public class Task {
    private String title;
    private String description;
    private static int id = 0;
    private final int idTask;
    private Status status = Status.NEW;
    private final TaskType type;

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

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", idTask=" + idTask +
                ", status=" + status +
                '}';
    }
}
