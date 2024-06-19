package tests.model;

import model.Epic;
import model.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class SubTaskTest {
    @Test
    void shouldCreateSubTask() {
        Epic epic = new Epic("Сделать уроки", "Понедельник");
        SubTask subTask = new SubTask("понедельник", "5 номеров", 1);
        epic.addingSubTasks(2);
        List<Integer> subTaskList = new ArrayList<>();
        subTaskList.add(subTask.getId());
        Assertions.assertArrayEquals(epic.getSubTaskIds().toArray(), subTaskList.toArray());
        Assertions.assertEquals("понедельник" + " 5 номеров", subTask.getTitle() + " " + subTask.getDescription());
        Assertions.assertEquals(2, subTask.getId());
    }
}