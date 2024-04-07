package service;

import model.Epic;
import model.Task;
import model.SubTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class TaskManager {
    private Map<Integer, Task> tasks = new HashMap<>();
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();

    public Map<Integer, Task> getAllTasks() {
        return tasks;
    }

    public Map<Integer, Epic> getAllEpics() {
        return epics;
    }

    public SubTask getAllSubTasks() {
        return (SubTask) subTasks;
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

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeEpicById(int id) {
        epics.remove(id);
    }

    public void removeSubTaskById(int id) {
        subTasks.remove(id);
    }

    public Task getTaskById(int idTask) {
        return tasks.get(idTask);
    }

    public Epic getEpicById(int idEpic) {
        return epics.get(idEpic);
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
        Epic epic = getEpicById(subTask.getEpicId());
        epic.addingSubTasks(subTask.getId());
    }

    public List<SubTask> getSubTaskForEpics(int idEpic) {

        Epic epic = epics.get(idEpic);

        List<SubTask> subTasks1 = new ArrayList<>();

        for (Integer i : epic.getSubTaskIds()) {
            subTasks1.add(subTasks.get(i));
        }
        return subTasks1;
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
}

