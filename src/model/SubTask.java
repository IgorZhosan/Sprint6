package model;


public class SubTask extends Task {
    private long epicId;

    public SubTask(String title, String desc, long id, long epicId) {
        super(title, desc, id);
        this.epicId = epicId;
    } // epic id

    public SubTask(String title, String desc, long id, Status status, long epicId) {
        super(title, desc, id, status);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }
}