package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public void getAllTasks() {
        System.out.println(tasks);
    }

    public void getAllEpics() {
        System.out.println(epics);
    }

    public void getAllSubTasks() {
        System.out.println(subTasks);
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllEpics() {
        epics.clear();
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    public void getTaskById(int idTask) {
        System.out.println(tasks.get(idTask));
    }

    public void getEpicById(int idEpic) {
        System.out.println(epics.get(idEpic));
    }


    public void getSubTaskById(int SubTaskById) {
        System.out.println(subTasks.get(SubTaskById));
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createTask(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void createTask(SubTask subTask) {
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addingSubTasks(subTask.getId());
    }

    public void updateTask(int id, Task updateTask) {
        if (tasks.containsKey(id)) {
            tasks.put(id, updateTask);
        }
    }

    public void updateEpic(int idEpic, Epic updateEpic) {
        if (epics.containsKey(idEpic)) {
            epics.put(idEpic, updateEpic);
        }
    }

    public void updateSubTask(int idSubTask, SubTask subTask) {
        if (subTasks.containsKey(idSubTask)) {
            subTasks.put(idSubTask, subTask);
        }
    }

    private void updateEpicStatus(Epic epic) {
        boolean allCompleted = true;
        boolean allNew = true;
        for (int subTaskId : epic.getSubTaskIds()) {
            SubTask subTask = subTasks.get(subTaskId);
            if (subTask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subTask.getStatus() != Status.DONE) {
                allCompleted = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allCompleted) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


}
