package service;

import model.Epic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileBackedTaskManagerTest {

    HistoryManager historyManager;
    FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void Preparation() throws IOException {
        historyManager = new InMemoryHistoryManager();
        Path filePath = Path.of("Sprint6/resources/tasks.csv");

        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }

        fileBackedTaskManager = new FileBackedTaskManager(historyManager, filePath.toString());
        fileBackedTaskManager.createFileIfNotExist();
        fileBackedTaskManager.createTask(new Epic("TEST", "DESCTEST"));
        fileBackedTaskManager.createTask(new Epic("TEST", "DESCTEST"));
        fileBackedTaskManager.createTask(new Epic("TEST", "DESCTEST"));
    }

    @Test
    public void shouldCreateFileIfNotExist() {
        Path filePath = Path.of("Sprint6/resources/tasks.csv");
        Assertions.assertTrue(Files.exists(filePath), "Файл tasks.csv должен существовать");
    }

    @Test
    public void shouldQuantityRecordsInFile() throws ManagerSaveException {
        int count = 0;
        try (Reader reader = new FileReader("Sprint6/resources/tasks.csv");
             BufferedReader bf = new BufferedReader(reader)) {
             bf.readLine();

            while (bf.readLine() != null) {
                count++;
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось прочитать файл", e);
        }
        Assertions.assertEquals(3, count, "Количество записей в файле должно быть 3");
    }
}