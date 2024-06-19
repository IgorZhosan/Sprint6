package service;

import model.Epic;
import model.Status;
import model.Task;
import model.SubTask;

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
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст");
        }
        for (Task task : tasks.values()) {
            historyManager.add(task);
        }
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        if (epics.isEmpty()) {
            System.out.println("Список задач пуст");
        }
        for (Epic epic : epics.values()) {
            historyManager.add(epic);
        }
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getAllSubTasks() {
        if (subTasks.isEmpty()) {
            System.out.println("Список задач пуст");
        }
        for (SubTask subTask : subTasks.values()) {
            historyManager.add(subTask);
        }
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
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer idEpic : epics.keySet()) {
            Epic epic = epics.get(idEpic);
            epic.deleteAllSubTasks();
        }
        epics.clear();
        subTasks.clear(); //нет эпиков, нед подзадач
    }

    @Override
    public void removeAllSubTasks() {
        for (Integer idSubTask : subTasks.keySet()) {
            SubTask subTask = subTasks.get(idSubTask);
            Epic epic = getEpicById(subTask.getEpicId());
            epic.deleteAllSubTasks();
            updateEpicStatusIfNeeded(epic);
        }
        subTasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.get(id);
        for (Integer indexSubTasks : epic.getSubTaskIds()) {
            subTasks.remove(indexSubTasks);
        }
        epic.deleteAllSubTasks();
        epics.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        Epic epic = epics.get(subTasks.get(id).getEpicId());
        epic.deleteSubTaskById(subTasks.get(id).getEpicId());
        subTasks.remove(id);
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public Task getTaskById(int idTask) {
        historyManager.add(tasks.get(idTask));
        return tasks.get(idTask);
    }

    @Override
    public Epic getEpicById(int idEpic) {
        historyManager.add(epics.get(idEpic));
        return epics.get(idEpic);
    }

    @Override
    public SubTask getSubTaskById(int SubTaskById) {
        historyManager.add(subTasks.get(SubTaskById));
        return subTasks.get(SubTaskById);
    }

    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void createTask(Epic epic) {
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
        Epic epic = epics.get(subTask.getEpicId());
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public void updateEpicStatusIfNeeded(Epic epic) {

        Epic newEpic = epics.get(epic.getId());
        List<SubTask> subTasks = getAllSubTasksById(newEpic.getId());

        boolean allSubTasksDone = !subTasks.isEmpty();
        boolean allSubTasksNew = true;

        for (SubTask subTask : subTasks) {
            if (subTask != null) {
                allSubTasksDone = allSubTasksDone & subTask.getStatus().equals(Status.DONE);
                allSubTasksNew = allSubTasksNew & subTask.getStatus().equals(Status.NEW);
            }
        }

        allSubTasksNew = allSubTasksNew || subTasks.isEmpty();

        if (allSubTasksDone) epic.setStatus(Status.DONE);

        if (allSubTasksNew) epic.setStatus(Status.NEW);

        if (!allSubTasksDone && !allSubTasksNew) epic.setStatus(Status.IN_PROGRESS);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }
}

