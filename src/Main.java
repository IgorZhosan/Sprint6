import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        taskManager.createTask(new Task("Завершить курс по Java","Точно завершить курс по Java",1));

    }
}