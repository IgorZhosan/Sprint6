package model;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String title, String desc, int epicId) {
        super(title, desc, Status.NEW, TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public SubTask(int id, String title, String desc, Status status, int epicId) {
        super(id, title, desc, status, TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
