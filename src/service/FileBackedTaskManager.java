package service;

import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path filePath;

    public FileBackedTaskManager(HistoryManager historyManager, Path filePath) {
        super(historyManager);
        this.filePath = filePath;
    }

    private void save() {

    }


}
