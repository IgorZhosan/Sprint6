package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SubTaskTest {

    @Test
    void shouldCreateSubTask() {
        Epic epic = new Epic("Сделать уроки", "Понедельник");
        SubTask subTask = new SubTask("понедельник", "5 номеров", epic.getId());

        epic.addingSubTasks(subTask.getId());

        List<Integer> subTaskList = new ArrayList<>(epic.getSubTaskIds());
        List<Integer> expectedSubTaskList = new ArrayList<>();
        expectedSubTaskList.add(subTask.getId());

        Assertions.assertEquals(expectedSubTaskList, subTaskList);
        Assertions.assertEquals("понедельник 5 номеров", subTask.getTitle() + " " + subTask.getDescription());
        Assertions.assertEquals(subTask.getId(), (int) subTaskList.get(0));
    }
}
