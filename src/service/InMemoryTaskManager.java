package service;

import model.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, SubTask> subTasks = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime, Comparator.nullsLast(LocalDateTime::compareTo)));

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
        List<SubTask> subTasksForEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                SubTask subTask = subTasks.get(subTaskId);
                if (subTask != null) {
                    subTasksForEpic.add(subTask);
                }
            }
        }
        return subTasksForEpic;
    }

    @Override
    public void removeAllTasks() {
        for (Integer id : tasks.keySet()) {
            prioritizedTasks.remove(tasks.get(id));
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        for (Integer id : epics.keySet()) {
            Epic epic = epics.get(id);
            for (Integer subTaskId : epic.getSubTaskIds()) {
                prioritizedTasks.remove(subTasks.get(subTaskId));
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
            prioritizedTasks.remove(subTasks.get(id));
            historyManager.remove(id);
        }
        subTasks.clear();
    }

    @Override
    public void removeTaskById(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            prioritizedTasks.remove(task);
            historyManager.remove(id);
        }
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (Integer subTaskId : epic.getSubTaskIds()) {
                prioritizedTasks.remove(subTasks.get(subTaskId));
                subTasks.remove(subTaskId);
                historyManager.remove(subTaskId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            Epic epic = epics.get(subTask.getEpicId());
            epic.deleteSubTaskById(id);
            updateEpicStatusIfNeeded(epic);
            prioritizedTasks.remove(subTask);
            historyManager.remove(id);
        }
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask != null) {
            historyManager.add(subTask);
        }
        return subTask;
    }

    @Override
    public void createTask(Task task) {
        if (isTaskTimeOverlapping(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
        tasks.put(task.getId(), task);
        prioritizedTasks.add(task);
    }

    @Override
    public void createTask(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void createTask(SubTask subTask) {
        if (isTaskTimeOverlapping(subTask)) {
            throw new IllegalArgumentException("Подзадача пересекается по времени с другой задачей.");
        }
        subTasks.put(subTask.getId(), subTask);
        prioritizedTasks.add(subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addingSubTasks(subTask.getId());
        updateEpicStatusIfNeeded(epic);
    }

    @Override
    public void updateTask(Task updateTask) {
        Task task = tasks.get(updateTask.getId());
        if (task != null) {
            prioritizedTasks.remove(task);
            if (isTaskTimeOverlapping(updateTask)) {
                throw new IllegalArgumentException("Обновляемая задача пересекается по времени с другой задачей.");
            }
            tasks.put(updateTask.getId(), updateTask);
            prioritizedTasks.add(updateTask);
        }
    }

    @Override
    public void updateEpic(Epic updateEpic) {
        epics.put(updateEpic.getId(), updateEpic);
        updateEpicStatusIfNeeded(updateEpic);
    }

    @Override
    public void updateSubTask(SubTask updateSubTask) {
        SubTask subTask = subTasks.get(updateSubTask.getId());
        if (subTask != null) {
            prioritizedTasks.remove(subTask);
            if (isTaskTimeOverlapping(updateSubTask)) {
                throw new IllegalArgumentException("Обновляемая подзадача пересекается по времени с другой задачей.");
            }
            subTasks.put(updateSubTask.getId(), updateSubTask);
            prioritizedTasks.add(updateSubTask);
            Epic epic = epics.get(updateSubTask.getEpicId());
            updateEpicStatusIfNeeded(epic);
        }
    }

    @Override
    public void updateEpicStatusIfNeeded(Epic epic) {
        List<SubTask> epicSubTasks = getAllSubTasksById(epic.getId());
        boolean allSubTasksDone = !epicSubTasks.isEmpty();
        boolean allSubTasksNew = true;

        for (SubTask subTask : epicSubTasks) {
            if (subTask != null) {
                allSubTasksDone = allSubTasksDone && subTask.getStatus().equals(Status.DONE);
                allSubTasksNew = allSubTasksNew && subTask.getStatus().equals(Status.NEW);
            }
        }

        if (allSubTasksDone) {
            epic.setStatus(Status.DONE);
        } else if (allSubTasksNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    @Override
    public boolean isTaskTimeOverlapping(Task newTask) {
        if (newTask.getStartTime() == null || newTask.getEndTime() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .anyMatch(existingTask -> {
                    if (existingTask.getStartTime() == null || existingTask.getEndTime() == null) {
                        return false;
                    }

                    return newTask.getStartTime().isBefore(existingTask.getEndTime()) &&
                            newTask.getEndTime().isAfter(existingTask.getStartTime());
                });
    }


    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
