package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic;
    protected SubTask subTask1;
    protected SubTask subTask2;

    @BeforeEach
    void setUp() throws IOException {
        // Создаем задачи и эпики до каждого теста
        task1 = new Task("Task 1", "Description 1", null, null, LocalDateTime.now(), Duration.ofMinutes(60));
        task2 = new Task("Task 2", "Description 2", null, null, LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(60));

        epic = new Epic("Epic 1", "Description 1");

        subTask1 = new SubTask("SubTask 1", "Description 1", epic.getId());
        subTask2 = new SubTask("SubTask 2", "Description 2", epic.getId());
    }

    @Test
    void shouldCreateAndRetrieveTask() {
        taskManager.createTask(task1);

        Assertions.assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        taskManager.createTask(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void shouldCreateAndRetrieveSubTask() {
        taskManager.createTask(epic);
        taskManager.createTask(subTask1);

        Assertions.assertEquals(subTask1, taskManager.getSubTaskById(subTask1.getId()));
    }

    @Test
    void shouldCalculateEpicStatusCorrectly() {
        taskManager.createTask(epic);

        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldCheckTaskTimeOverlapping() {
        taskManager.createTask(task1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}
