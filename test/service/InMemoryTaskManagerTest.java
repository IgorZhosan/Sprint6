package service;

import org.junit.jupiter.api.BeforeEach;

public class InMemoryTaskManagerTest extends service.TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
    }
}
