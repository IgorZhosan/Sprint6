package service;
import model.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private Map<Long, Task> tasks = new HashMap<>();

    public List<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public void createTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            throw new RuntimeException("Cannot create new task because task with id=" + task.getId() + " already exists");
        }

        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            Epic epic = (Epic) tasks.get(subTask.getEpicId());
            epic.getSubTaskIds().add(subTask.getId());
        }
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            throw new RuntimeException("Cannot update task because task with id=" + task.getId() + " don't exists");
        }
        if (task instanceof SubTask) {
            SubTask subTask = (SubTask) task;
            SubTask oldSubTask = (SubTask) tasks.get(subTask.getId());

            if (subTask.getEpicId() != oldSubTask.getEpicId()) {
                Epic oldEpic = (Epic) tasks.get(oldSubTask.getEpicId());
                oldEpic.getSubTaskIds().remove(oldSubTask.getId());
            }

            Epic epic = (Epic) tasks.get(subTask.getEpicId());
            epic.getSubTaskIds().add(subTask.getId());
        } else if (task instanceof Epic) {
            Epic epic = (Epic) task;
            Epic oldEpic = (Epic) tasks.get(epic.getId());

            for (Long subTaskId : oldEpic.getSubTaskIds()) {
                epic.getSubTaskIds().add(subTaskId);
            }
        }
        tasks.put(task.getId(), task);

        if (task instanceof SubTask) {
            updateEpicStatusIfNeeded(((SubTask) task).getEpicId());
        }
    }

    public void removeTaskById(long id) {
        tasks.remove(id);
    }

    public List<SubTask> getAllSubTasks(long epicId) {
        Epic epic = (Epic) tasks.get(epicId);
        ArrayList<SubTask> res = new ArrayList<>();

        for (Long subTaskId : epic.getSubTaskIds()) {
            res.add((SubTask) tasks.get(subTaskId));
        }
        return res;
    }

    private void updateEpicStatusIfNeeded(long epicId) {
        Epic epic = (Epic) tasks.get(epicId);
        List<SubTask> subTasks = getAllSubTasks(epic.getId());

        boolean allSubTasksDone = !subTasks.isEmpty();
        boolean allSubTasksNew = true;

        for (SubTask subTask : subTasks) {
            allSubTasksDone &= subTask.getStatus() == Status.DONE;
            allSubTasksNew &= subTask.getStatus() == Status.NEW;
        }

        allSubTasksNew |= subTasks.isEmpty();

        if (allSubTasksDone)
            epic.setStatus(Status.DONE);

        if (allSubTasksNew)
            epic.setStatus(Status.NEW);

        if (!allSubTasksDone && !allSubTasksNew)
            epic.setStatus(Status.IN_PROGRESS);
    }
}