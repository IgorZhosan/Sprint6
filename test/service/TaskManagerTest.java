package service;

import model.*;
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
        // Создаем задачи и эпики до каждого теста с уникальными ID
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW, TaskType.TASK, LocalDateTime.now(), Duration.ofMinutes(60));
        task2 = new Task(2, "Task 2", "Description 2", Status.NEW, TaskType.TASK, LocalDateTime.now().plusMinutes(30), Duration.ofMinutes(60));

        // Эпик и его подзадачи
        epic = new Epic(3, "Epic 1", "Description 1");

        subTask1 = new SubTask(4, "SubTask 1", "Description 1", Status.NEW, epic.getId());
        subTask2 = new SubTask(5, "SubTask 2", "Description 2", Status.NEW, epic.getId());
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

        // Проверяем, что статус эпика изменился на "IN_PROGRESS"
        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());

        // Устанавливаем все подзадачи в состояние DONE
        subTask2.setStatus(Status.DONE);
        taskManager.updateSubTask(subTask2);

        // Теперь статус эпика должен быть "DONE"
        Assertions.assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldCheckTaskTimeOverlapping() {
        taskManager.createTask(task1);
        // Task2 пересекается по времени с Task1
        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(task2));
    }
}
