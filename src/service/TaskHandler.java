package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        if (method.equals("GET")) {
            if (segments.length == 2) {
                // GET /tasks - получение всех задач
                String response = gson.toJson(taskManager.getAllTasks());
                sendText(exchange, response, 200);
            } else if (segments.length == 3) {
                // GET /tasks/{id} - получение задачи по id
                handleGetTaskById(exchange, segments[2]);
            } else {
                sendNotFound(exchange);
            }
        } else if (method.equals("POST")) {
            // POST /tasks - создание новой задачи
            handleCreateOrUpdateTask(exchange);
        } else if (method.equals("DELETE")) {
            if (segments.length == 2) {
                // DELETE /tasks - удаление всех задач
                taskManager.removeAllTasks();
                sendText(exchange, "All tasks deleted", 200);
            } else if (segments.length == 3) {
                // DELETE /tasks/{id} - удаление задачи по id
                handleDeleteTaskById(exchange, segments[2]);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendText(exchange, "{\"error\":\"Method Not Allowed\"}", 405);
        }
    }

    private void handleGetTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                String response = gson.toJson(task);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Task ID\"}", 400);
        }
    }

    private void handleCreateOrUpdateTask(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Task task = gson.fromJson(isr, Task.class);

        try {
            Optional<Task> existingTask = Optional.ofNullable(taskManager.getTaskById(task.getId()));
            if (existingTask.isPresent()) {
                taskManager.updateTask(task);
                sendText(exchange, "Task updated", 200);
            } else {
                taskManager.createTask(task);
                sendText(exchange, "Task created", 201);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteTaskById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Task task = taskManager.getTaskById(id);
            if (task != null) {
                taskManager.removeTaskById(id);
                sendText(exchange, "Task deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Task ID\"}", 400);
        }
    }
}
