package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<SubTask> getAllSubTasksById(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> res = new ArrayList<>();

        for (Integer subTaskId : epic.getSubTaskIds()) {
            res.add(subTasks.get(subTaskId));
        }

        return res;
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            Epic epic = epics.get(id);
            for (Integer subTaskId : epic.getSubTaskIds()) {
                historyManager.remove(subTaskId);
                subTasks.remove(subTaskId);
            }
            historyManager.remove(id);
        }
        epics.clear();
    }

    @Override
    public void removeAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
        }
        subTasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer subTaskId : epic.getSubTaskIds()) {
            historyManager.remove(subTaskId);
            subTasks.remove(subTaskId);
        }
        historyManager.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getEpicId());
        epic.deleteSubTaskById(id);
        historyManager.remove(id);
        subTasks.remove(id);
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public Task getTaskById(int idTask) {
        Task task = tasks.get(idTask);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int idEpic) {
        Epic epic = epics.get(idEpic);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int idSubTask) {
        SubTask subTask = subTasks.get(idSubTask);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void createTask(Epic epic) throws ManagerSaveException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = getEpicById(subTask.getEpicId());
        epic.addingSubTasks(subTask.getId());
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public void updateTask(Task updateTask) {
        Task task = tasks.get(updateTask.getId());
        task.setTitle(updateTask.getTitle());
        task.setDescription(updateTask.getDescription());
        task.setStatus(updateTask.getStatus());
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        Epic epic = epics.get(updateEpic.getId());
        epic.setTitle(updateEpic.getTitle());
        epic.setDescription(updateEpic.getDescription());
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) {
        SubTask subTask = subTasks.get(updateSubTask.getId());
        subTask.setTitle(updateSubTask.getTitle());
        subTask.setDescription(updateSubTask.getDescription());
        subTask.setStatus(updateSubTask.getStatus());
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public void updateEpicStatusIfNeeded(Epic epic) {
        List<SubTask> subTasks = getAllSubTasksById(epic.getId());
        boolean allSubTasksDone = !subTasks.isEmpty();
        boolean allSubTasksNew = true;

        for (SubTask subTask : subTasks) {
            if (subTask != null) {
                allSubTasksDone = allSubTasksDone && subTask.getStatus().equals(Status.DONE);
                allSubTasksNew = allSubTasksNew && subTask.getStatus().equals(Status.NEW);
            }
        }

        allSubTasksNew = allSubTasksNew || subTasks.isEmpty();

        if (allSubTasksDone) {
            epic.setStatus(Status.DONE);
        } else if (allSubTasksNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }
}
