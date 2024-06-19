package tests.service;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;

public class InMemoryHistoryManagerTest {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;

    @BeforeEach
    void setUp() {
        historyManager = new service.InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        Task task = new Task("Убрать сад", "Сегодня");
        Task task1 = new Task("Убрать огород", "Сегодня");
        Task task2 = new Task("Убрать машину", "Сегодня");
        Task task3 = new Task("Убрать лужайку", "Сегодня");
        Task task4 = new Task("Убрать подвал", "Сегодня");
        Task task5 = new Task("Убрать сад", "Сегодня");
        Task task6 = new Task("Убрать огород", "Сегодня");
        Task task7 = new Task("Убрать машину", "Сегодня");
        Task task8 = new Task("Убрать лужайку", "Сегодня");
        Task task9 = new Task("Убрать подвал", "Сегодня");
        Task task10 = new Task("Убрать сад", "Сегодня");
        Task task11 = new Task("Убрать огород", "Сегодня");
        taskManager.createTask(task);
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);
        taskManager.createTask(task7);
        taskManager.createTask(task8);
        taskManager.createTask(task9);
        taskManager.createTask(task10);
        taskManager.createTask(task11);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        taskManager.getTaskById(task4.getId());
        taskManager.getTaskById(task5.getId());
        taskManager.getTaskById(task6.getId());
        taskManager.getTaskById(task7.getId());
        taskManager.getTaskById(task8.getId());
        taskManager.getTaskById(task9.getId());
        taskManager.getTaskById(task10.getId());
        taskManager.getTaskById(task11.getId());
    }

    @Test
    void shouldRewriteTheFirstElementInHistoryManager() {
        Assertions.assertEquals(10, taskManager.getHistory().size());
    }

}
