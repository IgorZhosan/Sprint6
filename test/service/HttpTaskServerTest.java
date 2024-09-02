package service;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskServerTest {
    private HttpTaskServer server;
    private TaskManager taskManager;
    private final Gson gson = new Gson();

    @BeforeEach
    public void setUp() throws IOException {
        HistoryManager historyManager = new InMemoryHistoryManager();
        taskManager = new InMemoryTaskManager(historyManager);
        server = new HttpTaskServer(taskManager);
        server.start();
    }

    @AfterEach
    public void tearDown() {
        server.stop();
    }

    // Тесты для /tasks

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(120), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> tasks = List.of(task1, task2);
        String expectedJson = gson.toJson(tasks);
        assertEquals(expectedJson, response.body());
    }

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task("New Task", "New Description", Duration.ofMinutes(90), LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals("New Task", tasks.get(0).getTitle());
    }

    @Test
    public void testDeleteTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task to Delete", "To Be Deleted", Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getTaskById(task.getId()));
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        Task task = new Task("Task 1", "Description 1", Duration.ofMinutes(60), LocalDateTime.now());
        taskManager.createTask(task);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/" + task.getId()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = gson.toJson(task);
        assertEquals(expectedJson, response.body());
    }

    // Тесты для /subtasks

    @Test
    public void testGetAllSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createTask(epic);
        SubTask subtask1 = new SubTask("SubTask 1", "SubTask Description 1", epic.getId());
        SubTask subtask2 = new SubTask("SubTask 2", "SubTask Description 2", epic.getId());
        taskManager.createTask(subtask1);
        taskManager.createTask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<SubTask> subtasks = List.of(subtask1, subtask2);
        String expectedJson = gson.toJson(subtasks);
        assertEquals(expectedJson, response.body());
    }

    @Test
    public void testCreateSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createTask(epic);
        SubTask subtask = new SubTask("New SubTask", "New SubTask Description", epic.getId());
        String subtaskJson = gson.toJson(subtask);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<SubTask> subtasks = taskManager.getAllSubTasks();
        assertEquals(1, subtasks.size());
        assertEquals("New SubTask", subtasks.get(0).getTitle());
    }

    @Test
    public void testDeleteSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createTask(epic);
        SubTask subtask = new SubTask("SubTask to Delete", "To Be Deleted", epic.getId());
        taskManager.createTask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getSubTaskById(subtask.getId()));
    }

    @Test
    public void testGetSubtaskById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createTask(epic);
        SubTask subtask = new SubTask("SubTask 1", "SubTask Description 1", epic.getId());
        taskManager.createTask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/" + subtask.getId()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = gson.toJson(subtask);
        assertEquals(expectedJson, response.body());
    }

    // Тесты для /epics

    @Test
    public void testGetAllEpics() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Epic Description 1");
        Epic epic2 = new Epic("Epic 2", "Epic Description 2");
        taskManager.createTask(epic1);
        taskManager.createTask(epic2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Epic> epics = List.of(epic1, epic2);
        String expectedJson = gson.toJson(epics);
        assertEquals(expectedJson, response.body());
    }

    @Test
    public void testCreateEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("New Epic", "New Epic Description");
        String epicJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());
        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(1, epics.size());
        assertEquals("New Epic", epics.get(0).getTitle());
    }

    @Test
    public void testDeleteEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic to Delete", "To Be Deleted");
        taskManager.createTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getEpicById(epic.getId()));
    }

    @Test
    public void testGetEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic 1", "Epic Description 1");
        taskManager.createTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/" + epic.getId()))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        String expectedJson = gson.toJson(epic);
        assertEquals(expectedJson, response.body());
    }

    // Тесты для /history

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(120), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        // Примем задачи в историю
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> history = List.of(task1, task2);
        String expectedJson = gson.toJson(history);
        assertEquals(expectedJson, response.body());
    }

    // Тесты для /prioritized

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(60), LocalDateTime.now());
        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(120), LocalDateTime.now().plusHours(1));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();
        String expectedJson = gson.toJson(prioritizedTasks);
        assertEquals(expectedJson, response.body());
    }
}
