package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class EpicTest {

    @Test
    void shouldCreateEpic() {
        Epic epic = new Epic("Название задачи", "Описание задачи");
        String[] epics = {"Название задачи", "Описание задачи"};
        String[] epicsCorrect = new String[2];
        epicsCorrect[0] = epic.getTitle();
        epicsCorrect[1] = epic.getDescription();
        assertArrayEquals(epics, epicsCorrect);
    }
}