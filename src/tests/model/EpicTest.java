package tests.model;

import model.Epic;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class EpicTest {

    @Test
    void shouldCreateEpic() {
        Epic epic = new Epic("Название задачи", "Описание задачи");
        String[] epics = {"Название задачи", "Описание задачи"};
        String[] epicsCorrect = new String[2];
        epicsCorrect[0] = epic.getTitle();
        epicsCorrect[1] = epic.getDescription();
        Assert.assertArrayEquals(epics, epicsCorrect);
    }
}