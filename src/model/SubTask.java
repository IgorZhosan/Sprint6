package model;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String title, String desc, int epicId) {
        super(title, desc);
        this.epicId = epicId;
    }

    public SubTask(String title, String desc, Status status, int epicId) {
        super(title, desc, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

}