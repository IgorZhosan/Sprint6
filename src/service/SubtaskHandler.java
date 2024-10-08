package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.SubTask;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] segments = path.split("/");

        try {
            if (method.equals("GET")) {
                if (segments.length == 2) {
                    // GET /subtasks - получение всех подзадач
                    String response = gson.toJson(taskManager.getAllSubTasks());
                    sendText(exchange, response, 200);
                } else if (segments.length == 3) {
                    handleGetSubtaskById(exchange, segments[2]);
                } else {
                    sendNotFound(exchange);
                }
            } else if (method.equals("POST")) {
                handleCreateOrUpdateSubtask(exchange);
            } else if (method.equals("DELETE")) {
                if (segments.length == 2) {
                    taskManager.removeAllSubTasks();
                    sendText(exchange, "All subtasks deleted", 200);
                } else if (segments.length == 3) {
                    handleDeleteSubtaskById(exchange, segments[2]);
                } else {
                    sendNotFound(exchange);
                }
            } else {
                sendText(exchange, "{\"error\":\"Method Not Allowed\"}", 405);
            }
        } catch (Exception e) {
            sendText(exchange, "{\"error\":\"Internal Server Error\"}", 500);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange, String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            SubTask subTask = taskManager.getSubTaskById(id);
            if (subTask != null) {
                String response = gson.toJson(subTask);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Subtask ID\"}", 400);
        }
    }

    private void handleCreateOrUpdateSubtask(HttpExchange exchange) {
        try (InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)) {
            if (exchange.getRequestBody().available() == 0) {
                sendText(exchange, "{\"error\":\"Empty request body\"}", 400);
                return;
            }

            SubTask subTask = gson.fromJson(isr, SubTask.class);

            Optional<SubTask> existingSubTask = Optional.ofNullable(taskManager.getSubTaskById(subTask.getId()));
            if (existingSubTask.isPresent()) {
                taskManager.updateSubTask(subTask);
                sendText(exchange, "Subtask updated", 200);
            } else {
                taskManager.createTask(subTask);
                sendText(exchange, "Subtask created", 201);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        } catch (Exception e) {
            sendText(exchange, "{\"error\":\"Bad Request\"}", 400);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String idStr) {
        try {
            int id = Integer.parseInt(idStr);
            SubTask subTask = taskManager.getSubTaskById(id);
            if (subTask != null) {
                taskManager.removeSubTaskById(id);
                sendText(exchange, "Subtask deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Subtask ID\"}", 400);
        }
    }
}
