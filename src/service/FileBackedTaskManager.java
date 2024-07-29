package service;

import model.*;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final Path file;

    public FileBackedTaskManager(HistoryManager historyManager, String filePath) throws ManagerSaveException {
        super(historyManager);
        this.file = Paths.get(filePath);
        createFileIfNotExist();
        loadFromFile();
    }

    public void createFileIfNotExist() throws ManagerSaveException {
        try {
            if (!Files.exists(file)) {
                if (!Files.exists(file.getParent())) {
                    Files.createDirectories(file.getParent());
                }
                Files.createFile(file);
                try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
                    writer.write("id,type,name,status,description,epic\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось создать файл: " + file.toString(), e);
        }
    }

    private void save() throws ManagerSaveException {
        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : getAllTasks()) {
                writer.write(taskToString(task) + "\n");
            }
            for (Epic epic : getAllEpics()) {
                writer.write(taskToString(epic) + "\n");
            }
            for (SubTask subTask : getAllSubTasks()) {
                writer.write(taskToString(subTask) + "\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось сохранить данные в файл", e);
        }
    }

    private String taskToString(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getTitle()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription()).append(",");
        if (task instanceof SubTask) {
            sb.append(((SubTask) task).getEpicId());
        }
        return sb.toString();
    }

    private void loadFromFile() throws ManagerSaveException {
        if (!Files.exists(file)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            for (String line : lines.stream().skip(1).collect(Collectors.toList())) {
                Task task = taskFromLoad(line);
                switch (task.getType()) {
                    case TASK:
                        super.createTask(task);
                        break;
                    case EPIC:
                        super.createTask((Epic) task);
                        break;
                    case SUBTASK:
                        super.createTask((SubTask) task);
                        break;
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Не удалось загрузить данные из файла", e);
        }
    }

    private Task taskFromLoad(String value) {
        String[] fields = value.split(",");
        int id = Integer.parseInt(fields[0]);
        TaskType taskType = TaskType.valueOf(fields[1]);
        String title = fields[2];
        Status status = Status.valueOf(fields[3]);
        String descriptions = fields[4];
        switch (taskType) {
            case TASK:
                return new Task(id, title, descriptions, status, taskType);
            case EPIC:
                return new Epic(id, title, descriptions);
            case SUBTASK:
                int epicId = Integer.parseInt(fields[5]);
                return new SubTask(id, title, descriptions, status, epicId);
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи");
        }
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void createTask(Epic epic) throws ManagerSaveException {
        super.createTask(epic);
        save();
    }

    @Override
    public void createTask(SubTask subTask) throws ManagerSaveException {
        super.createTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task updateTask) throws ManagerSaveException {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void updateEpic(Epic updateEpic) throws ManagerSaveException {
        super.updateEpic(updateEpic);
        save();
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) throws ManagerSaveException {
        super.updateSubTask(updateSubTask);
        save();
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) throws ManagerSaveException {
        super.removeSubTaskById(id);
        save();
    }
}
