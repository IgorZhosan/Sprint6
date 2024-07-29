package service;

public class Manager {
    private final HistoryManager historyManager = new InMemoryHistoryManager();

    public TaskManager getDefault() {
        return new FileBackedTaskManager(historyManager, "Sprint6/resources/tasks.csv");
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}
