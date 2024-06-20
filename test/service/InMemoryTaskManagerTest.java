package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private SubTask subTask1;
    private SubTask subTask2;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);

        task1 = new Task("Task 1", "Description 1");
        task2 = new Task("Task 2", "Description 2");

        epic1 = new Epic("Epic 1", "Description 1");
        subTask1 = new SubTask("SubTask 1", "Description 1", epic1.getId());
        subTask2 = new SubTask("SubTask 2", "Description 2", epic1.getId());
    }

    @Test
    void shouldCreateAndGetTask() {
        taskManager.createTask(task1);
        Assertions.assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldCreateAndGetEpic() {
        taskManager.createTask(epic1);
        Assertions.assertEquals(epic1, taskManager.getEpicById(epic1.getId()));
    }

    @Test
    void shouldCreateAndGetSubTask() {
        taskManager.createTask(epic1);
        taskManager.createTask(subTask1);
        Assertions.assertEquals(subTask1, taskManager.getSubTaskById(subTask1.getId()));
    }

    @Test
    void shouldDeleteTask() {
        taskManager.createTask(task1);
        taskManager.removeTaskById(task1.getId());
        Assertions.assertNull(taskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldDeleteEpicAndItsSubTasks() {
        taskManager.createTask(epic1);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        taskManager.removeEpicById(epic1.getId());

        Assertions.assertNull(taskManager.getEpicById(epic1.getId()));
        Assertions.assertNull(taskManager.getSubTaskById(subTask1.getId()));
        Assertions.assertNull(taskManager.getSubTaskById(subTask2.getId()));
    }

    @Test
    void shouldUpdateTask() {
        taskManager.createTask(task1);
        task1.setTitle("Updated Task 1");
        taskManager.updateTask(task1);
        Assertions.assertEquals("Updated Task 1", taskManager.getTaskById(task1.getId()).getTitle());
    }

    @Test
    void shouldUpdateEpic() {
        taskManager.createTask(epic1);
        epic1.setTitle("Updated Epic 1");
        taskManager.updateEpic(epic1);
        Assertions.assertEquals("Updated Epic 1", taskManager.getEpicById(epic1.getId()).getTitle());
    }

    @Test
    void shouldUpdateSubTask() {
        taskManager.createTask(epic1);
        taskManager.createTask(subTask1);
        subTask1.setTitle("Updated SubTask 1");
        taskManager.updateSubTask(subTask1);
        Assertions.assertEquals("Updated SubTask 1", taskManager.getSubTaskById(subTask1.getId()).getTitle());
    }

    @Test
    void shouldGetHistory() {
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        Assertions.assertEquals(2, taskManager.getHistory().size());
    }
}
