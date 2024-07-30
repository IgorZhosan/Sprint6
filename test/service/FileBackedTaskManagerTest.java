package service;

import model.Task;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
    public void shouldQuantityRecordsInFile() throws RuntimeException {
        int count = 0;
        try (Reader reader = new FileReader("Sprint6/resources/tasks.csv");
             BufferedReader bf = new BufferedReader(reader)) {
            while (bf.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл");
        }
        Assertions.assertEquals(count, 3);
    }
}
