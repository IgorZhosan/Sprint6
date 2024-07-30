package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTasks();

    List<Epic> getAllEpics();

    List<SubTask> getAllSubTasks();

    List<SubTask> getAllSubTasksById(int epicId);

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    Task getTaskById(int idTask);

    Epic getEpicById(int idEpic);

    SubTask getSubTaskById(int subTaskById);

    void createTask(Task task) throws ManagerSaveException;

    void createTask(Epic epic) throws ManagerSaveException;

    void createTask(SubTask subTask);

    void updateTask(Task updateTask);

    void updateEpic(Epic updateEpic);

    void updateSubTask(SubTask updateSubTask);

    void updateEpicStatusIfNeeded(Epic epic);

    List<Task> getHistory();

}
