package service;

import model.Task;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest {
    HistoryManager historyManager;
    TaskManager taskManager;

    @BeforeEach
    void Preparation() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        taskManager.createTask(new Task("ТЕСТ", "ТЕСТ Описания"));
        taskManager.createTask(new Task("ТЕСТ", "ТЕСТ Описания"));
        taskManager.createTask(new Task("ТЕСТ", "ТЕСТ Описания"));
    }

    @Test
    public void shouldCreateFileIfNotExist() {
        Assertions.assertEquals(Files.exists(Path.of("Sprint6/resources/tasks.csv")), Files.exists(Path.of("tasks.csv")));
    }

    @Test
    public void shouldQuantityRecordsInFile() {

    }
}
