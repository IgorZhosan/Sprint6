package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    @Test
    void shouldCreateAndRetrieveTask() {
        Task task = new Task("Task 1", "Description 1");
        taskManager.createTask(task);

        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void shouldCreateAndRetrieveEpic() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);

        Assertions.assertEquals(epic, taskManager.getEpicById(epic.getId()));
    }

    @Test
    void shouldCreateAndRetrieveSubTask() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);

        SubTask subTask = new SubTask("SubTask 1", "Description 1", epic.getId());
        taskManager.createTask(subTask);

        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void shouldCalculateEpicStatusCorrectly() {
        Epic epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);

        SubTask subTask1 = new SubTask("SubTask 1", "Description 1", epic.getId());
        SubTask subTask2 = new SubTask("SubTask 2", "Description 2", epic.getId());

        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        subTask1.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask1);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldCheckTaskTimeOverlapping() {
        Task task1 = new Task("Task 1", "Description 1", null, null, LocalDateTime.now(), Duration.ofMinutes(60));
        Task task2 = new Task("Task 2", "Description 2", null, null, LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(60));

        taskManager.createTask(task1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}
