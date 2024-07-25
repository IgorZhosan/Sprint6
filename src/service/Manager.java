package service;

public class Manager {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return new InMemoryTaskManager(historyManager);
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
