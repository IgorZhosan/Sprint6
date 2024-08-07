package model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    private final Set<Integer> subTaskIds = new HashSet<>();

    public Epic(String title, String desc) {
        super(title, desc, Status.NEW, TaskType.EPIC);
    }

    public Epic(int id, String title, String desc) {
        super(id, title, desc, Status.NEW, TaskType.EPIC);
    }

    public Set<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addingSubTasks(int id) {
        subTaskIds.add(id);
    }

    public void deleteAllSubTasks() {
        subTaskIds.clear();
    }

    public void deleteSubTaskById(int id) {
        subTaskIds.remove(id);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTaskIds=" + getSubTaskIds() +
                '}';
    }
}
