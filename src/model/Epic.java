package model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    private final Set<Integer> subTaskIds = new HashSet<>();

    public Epic(String title) {
        super(title);
    }

    public Set<Integer> getSubTaskIds() {
        return subTaskIds;
    }

    public void addingSubTasks(int id) {
        subTaskIds.add(id);
    }

}