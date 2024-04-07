import model.Epic;
import model.SubTask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {


        TaskManager taskManager = new TaskManager();


        taskManager.createTask(new Epic("Сделать домашнюю работу", "Быстро её сделать"));
        taskManager.createTask(new SubTask("Сделать русский язык", "5 номеров", 1));
        taskManager.createTask(new SubTask("Сделать математику", "6 номеров", 1));
        System.out.println(taskManager.getSubTaskForEpics(1));

    }
}