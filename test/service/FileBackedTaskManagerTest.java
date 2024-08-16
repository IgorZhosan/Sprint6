package service;

import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final String FILE_PATH = "Sprint6/resources/tasks.csv";

    @BeforeEach
    void setUp() throws IOException {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Path filePath = Paths.get(FILE_PATH);

        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent());
        }

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        taskManager = new FileBackedTaskManager(historyManager, FILE_PATH);
    }
}
