package tests.service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import service.HistoryManager;
import service.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;

public class InMemoryTaskManagerTest {

    private static HistoryManager historyManager;
    private static InMemoryTaskManager taskManager;
    Task task;
    Task task1;
    Task task2;
    Task task3;
    Task task4;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        task = new Task("Убрать сад", "Сегодня");
        task1 = new Task("Убрать огород", "Сегодня");
        task2 = new Task("Убрать машину", "Сегодня");
        task3 = new Task("Убрать лужайку", "Сегодня");
        task4 = new Task("Убрать подвал", "Сегодня");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task("Убрать сад");
        taskManager.createTask(task);
        Assertions.assertNotNull(taskManager.getAllTasks());
    }

    @Test
    void shouldCreateEpic() {
        Epic epic = new Epic("Сделать домашнюю работу", "Сегодня");
        taskManager.createTask(epic);
        SubTask subTask = new SubTask("Математика, 5 номеров", "срочно", epic.getId());
        taskManager.createTask(subTask);

        assert taskManager.getEpicById(epic.getId()) != null : "Задача с id = 1 должна быть не найдена";
    }

    @Test
    void shouldGetTaskList() {
        taskManager.createTask(new Task("Убраться в саду", "сегодня"));
        taskManager.createTask(new Task("Убраться в доме", "сегодня"));

        assert taskManager.getAllTasks().size() == 7;
    }

    @Test
    void shouldDeleteTask() {
        Task task = new Task("Убрать сад");
        taskManager.createTask(task);
        taskManager.removeTaskById(1);
        Assertions.assertNull(taskManager.getTaskById(1));
    }

    @Test
    void shouldGetTaskById() {
        Task task = new Task("Убрать сад");
        taskManager.createTask(task);
        Assertions.assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void shouldGetEpicById() {
        Epic epic2 = new Epic("Убрать сад", "Сегодня");
        taskManager.createTask(epic2);
        Assertions.assertEquals(epic2, taskManager.getEpicById((epic2.getId())));
    }

    @Test
    void shouldGetSubTaskById() {
        Epic epic = new Epic("Убрать сад", "Сегодня");
        SubTask subTask = new SubTask("собрать яблоки", "на заднем дворе", epic.getId());
        taskManager.createTask(epic);
        taskManager.createTask(subTask);
        Assertions.assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void shouldGetHistory() {
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getTaskById(task4.getId());
        taskManager.getHistory();
        Assertions.assertEquals(5, historyManager.getHistory().size());
    }
}
