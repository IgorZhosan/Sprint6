import model.Epic;
import model.SubTask;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Epic("Переезд"));
        taskManager.createTask(new Epic("Большая задача1"));
        taskManager.createTask(new SubTask("Подзадача большой задачи1", "тест", 1));


        taskManager.getEpicById(1);
        taskManager.getEpicById(2);
        taskManager.getAllTasks();

    }
}