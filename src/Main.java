import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.*;

public class Main {
    public static void main(String[] args) {

        Manager manager = new Manager();
        TaskManager taskManager = manager.getDefault();
        System.out.println("=========Эпики======");

        Epic epic = new Epic("Убраться в квартире", "Бысто"); // Создали эпик
        SubTask subTask1 = new SubTask("Помыть посуду", "10 тарелок", epic.getId()); // Создали подзадачу1 для эпика 1
        SubTask subTask2 = new SubTask("Постирать вещи", "шерсть", epic.getId()); // Создали подзадачу2 для эпика 1
        taskManager.createTask(epic); // добавили эпик в epics
        taskManager.createTask(subTask1); // добавили подзадачу
        taskManager.createTask(subTask2); // добавили подзадачу
        System.out.println(taskManager.getAllEpics()); // получили эпики
        taskManager.removeAllEpics(); // удалили эпики
        // System.out.println(taskManager.getAllEpics()); // эпики не выводит, так как их больше нет
        Epic epic2 = new Epic("Собрать овощи", "быстро");
        SubTask subTask3 = new SubTask("Собрать картошку", "быстро", epic2.getId());
        SubTask subTask4 = new SubTask("Собрать помидоры", "быстро", epic2.getId());

        taskManager.createTask(epic2);
        taskManager.createTask(subTask3);
        taskManager.createTask(subTask4);
        System.out.println(taskManager.getEpicById(epic2.getId()));// вызвали по id epic
        subTask3.setStatus(Status.NEW);
        subTask4.setStatus(Status.NEW);
        SubTask subTask5 = new SubTask("Собрать кукурузу", "быстро", epic2.getId());
        taskManager.createTask(subTask5);
        System.out.println(taskManager.getEpicById(epic2.getId()));
        System.out.println(subTask5.getId());
        subTask5.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.getAllSubTasksById(epic2.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));
        System.out.println("=========Таски========");


        Task task = new Task("Поесть", "Суп");
        taskManager.createTask(task);
        System.out.println(taskManager.getAllTasks());
        task.setTitle("Перекусить");
        task.setDescription("Печеньки");
        taskManager.updateTask(task);
        System.out.println(taskManager.getAllTasks());
        task.setStatus(Status.IN_PROGRESS);
        System.out.println(taskManager.getAllTasks());
        Task task1 = new Task("Помыться", "святой водой");
        taskManager.createTask(task1);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getTaskById(task1.getId()));
        taskManager.removeTaskById(task1.getId());
        taskManager.removeAllTasks();
        System.out.println("Подзадачи");
        System.out.println(taskManager.getSubTaskById(subTask3.getId()));
        subTask3.setTitle("Картошку помыть");
        taskManager.updateSubTask(subTask3);
        System.out.println(taskManager.getSubTaskById(subTask3.getId()));
        subTask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubTask(subTask3);
        System.out.println(taskManager.getSubTaskById(subTask3.getId()));
        System.out.println(taskManager.getAllSubTasksById(epic2.getId()));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasksById(epic2.getId()));
        System.out.println(taskManager.getEpicById(epic2.getId()));
        taskManager.removeAllSubTasks();
        System.out.println(taskManager.getEpicById(epic2.getId()));
        Task task5 = new Task("Убрать сад", "Сегодня");
        Task task6 = new Task("Убрать огород", "Сегодня");
        Task task2 = new Task("Убрать машину", "Сегодня");
        Task task3 = new Task("Убрать лужайку", "Сегодня");
        Task task4 = new Task("Убрать подвал", "Сегодня");
        Task task7 = new Task("Убрать гараж", "Сегодня");
        Task task8 = new Task("Убрать подсопку", "Сегодня");
        Task task9 = new Task("Убрать бар", "Сегодня");
        Task task10 = new Task("Убрать кладовку", "Сегодня");
        Task task11 = new Task("Убрать машиноместо", "Сегодня");
        Task task12 = new Task("Убрать листья", "Сегодня");
        taskManager.createTask(task5);
        taskManager.createTask(task6);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task7);
        taskManager.createTask(task8);
        taskManager.createTask(task9);
        taskManager.createTask(task10);
        taskManager.createTask(task11);
        taskManager.createTask(task12);
        System.out.println("Проверка history");
        System.out.println(taskManager.getTaskById(task5.getId()));
        System.out.println(taskManager.getTaskById(task6.getId()));
        System.out.println(taskManager.getTaskById(task2.getId()));
        System.out.println(taskManager.getTaskById(task3.getId()));
        System.out.println(taskManager.getTaskById(task4.getId()));
        System.out.println(taskManager.getTaskById(task6.getId()));
        System.out.println(taskManager.getTaskById(task7.getId()));
        System.out.println(taskManager.getTaskById(task8.getId()));
        System.out.println(taskManager.getTaskById(task9.getId()));
        System.out.println(taskManager.getTaskById(task10.getId()));
        System.out.println(taskManager.getTaskById(task11.getId()));
        System.out.println(taskManager.getTaskById(task12.getId()));
        System.out.println(taskManager.getHistory());
        System.out.println(taskManager.getHistory().size());
    }
}
