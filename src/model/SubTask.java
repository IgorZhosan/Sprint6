package model;

public class SubTask extends Task {
    private final int epicId;

    public SubTask(String title, String desc, int epicId) {
        super(title, desc);
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
                ", epicd=" + getEpicId() +
                '}';
    }
}