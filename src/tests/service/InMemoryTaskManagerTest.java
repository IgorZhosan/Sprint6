package tests.service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
    }

    @Test
    void shouldCreateAndGetTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);

        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void shouldCreateAndGetEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void shouldCreateAndGetSubTask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description 1", epic.getId());
        taskManager.createTask(subTask);

        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        taskManager.removeTaskById(task.getId());

        Assertions.assertNull(taskManager.getTaskById(task.getId()));
    }

    @Test
    void shouldDeleteEpicAndItsSubTasks() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);
        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", epic.getId());
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        taskManager.removeEpicById(epic.getId());

        Assertions.assertNull(taskManager.getEpicById(epic.getId()));
        Assertions.assertNull(taskManager.getSubTaskById(subTask1.getId()));
        Assertions.assertNull(taskManager.getSubTaskById(subTask2.getId()));
    }

    @Test
    void shouldUpdateTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);
        task.setTitle("Updated Task 1");
        taskManager.updateTask(task);

        Assertions.assertEquals("Updated Task 1", taskManager.getTaskById(task.getId()).getTitle());
    }

    @Test
    void shouldUpdateEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);
        epic.setTitle("Updated Epic 1");
        taskManager.updateEpic(epic);

        Assertions.assertEquals("Updated Epic 1", taskManager.getEpicById(epic.getId()).getTitle());
    }

    @Test
    void shouldUpdateSubTask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("SubTask 1", "Description 1", epic.getId());
        taskManager.createTask(subTask);
        subTask.setTitle("Updated SubTask 1");
        taskManager.updateSubTask(subTask);

        Assertions.assertEquals("Updated SubTask 1", taskManager.getSubTaskById(subTask.getId()).getTitle());
    }

    @Test
    void shouldGetHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        Assertions.assertEquals(2, taskManager.getHistory().size());
    }
}
