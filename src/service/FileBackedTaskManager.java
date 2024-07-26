package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileBackedTaskManager extends InMemoryTaskManager {

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public void createFile() throws Exception {
        try {
            Path direct = Paths.get("C:\\yp\\testingfiles");

            if (!Files.exists(direct)) {
                Files.createDirectories(direct);
            }

            Path file = direct.resolve("test.csv");

            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать директорию или файл", e);
        }
    }

    private void save() throws IOException {

    }


}
