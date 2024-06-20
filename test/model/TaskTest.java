package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void shouldCreateTask() {
        Task task = new Task("Уборка дома", "сегодня");
        Assertions.assertEquals("Уборка дома сегодня", task.getTitle() + " " + task.getDescription());
    }
}