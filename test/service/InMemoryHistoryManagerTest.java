package service;

import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", "Description 1");
        task2 = new Task("Task 2", "Description 2");
        task3 = new Task("Task 3", "Description 3");
    }

    @Test
    void shouldAddAndRemoveTasksFromHistory() {
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
        historyManager.add(task1);
        historyManager.add(task1); // Duplicate add

        Assertions.assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldMaintainOrderAfterRemoval() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2.getId());

        Assertions.assertEquals(task1, historyManager.getHistory().get(0));
        Assertions.assertEquals(task3, historyManager.getHistory().get(1));
    }
}
