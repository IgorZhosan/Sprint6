package service;

public class Manager {
    public TaskManager getDefault() {
        return new InMemoryTaskManager(getHistoryManager());
    }

    public HistoryManager getHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
