package tests.service;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.InMemoryHistoryManager;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    void shouldAddAndRemoveTasksFromHistory() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        Assertions.assertEquals(3, historyManager.getHistory().size());

        historyManager.remove(task2.getId());
        Assertions.assertEquals(2, historyManager.getHistory().size());
        Assertions.assertFalse(historyManager.getHistory().contains(task2));
    }

    @Test
    void shouldNotContainDuplicates() {
        Task task1 = new Task("Task 1", "Description 1");

        historyManager.add(task1);
        historyManager.add(task1); // Duplicate add

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        Task task3 = new Task("Task 3", "Description 3");

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        Assertions.assertEquals(task1, historyManager.getHistory().get(0));
        Assertions.assertEquals(task3, historyManager.getHistory().get(1));
    }
}
