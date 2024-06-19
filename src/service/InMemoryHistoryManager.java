package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>(10);

    private int currentIndex = 0;

    private static final int SIZE_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() < SIZE_HISTORY) {
            history.add(task);
        } else {
            history.set(currentIndex, task);
            currentIndex = (currentIndex + 1) % 10;
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }
}
