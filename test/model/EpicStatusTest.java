package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;

public class EpicStatusTest {

    private InMemoryTaskManager taskManager;
    private Epic epic;
    private SubTask subTask1;
    private SubTask subTask2;
    private SubTask subTask3;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        epic = new Epic("Epic 1", "Description 1");
        taskManager.createTask(epic);

        subTask1 = new SubTask("SubTask 1", "Description 1", epic.getId());
        subTask2 = new SubTask("SubTask 2", "Description 2", epic.getId());
        subTask3 = new SubTask("SubTask 3", "Description 3", epic.getId());
    }

    @Test
    void shouldReturnNewStatusIfAllSubTasksNew() {
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        Assertions.assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnDoneStatusIfAllSubTasksDone() {
        subTask1.setStatus(Status.DONE);
        subTask2.setStatus(Status.DONE);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        Assertions.assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnInProgressStatusIfSubTasksNewAndDone() {
        subTask1.setStatus(Status.NEW);
        subTask2.setStatus(Status.DONE);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }

    @Test
    void shouldReturnInProgressStatusIfAllSubTasksInProgress() {
        subTask1.setStatus(Status.IN_PROGRESS);
        subTask2.setStatus(Status.IN_PROGRESS);
        taskManager.createTask(subTask1);
        taskManager.createTask(subTask2);

        Assertions.assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus());
    }
}
