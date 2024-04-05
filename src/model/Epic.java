package model;

import java.util.HashSet;
import java.util.Set;

public class Epic extends Task {
    private Set<Long> subTaskIds = new HashSet<>();

    public Epic(String title, String description, long id) {
        super(title, description, id);

    } //

    public Set<Long> getSubTaskIds() {
        return subTaskIds;
    }
}