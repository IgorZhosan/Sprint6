package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
                // GET /epics - получение всех эпиков
                String response = gson.toJson(taskManager.getAllEpics());
                sendText(exchange, response, 200);
            } else if (segments.length == 3) {
                // GET /epics/{id} - получение эпика по id
                handleGetEpicById(exchange, segments[2]);
            } else {
                sendNotFound(exchange);
            }
        } else if (method.equals("POST")) {
            // POST /epics - создание нового эпика
            handleCreateOrUpdateEpic(exchange);
        } else if (method.equals("DELETE")) {
            if (segments.length == 2) {
                // DELETE /epics - удаление всех эпиков
                taskManager.removeAllEpics();
                sendText(exchange, "All epics deleted", 200);
            } else if (segments.length == 3) {
                // DELETE /epics/{id} - удаление эпика по id
                handleDeleteEpicById(exchange, segments[2]);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendText(exchange, "{\"error\":\"Method Not Allowed\"}", 405);
        }
    }

    private void handleGetEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                String response = gson.toJson(epic);
                sendText(exchange, response, 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Epic ID\"}", 400);
        }
    }

    private void handleCreateOrUpdateEpic(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Epic epic = gson.fromJson(isr, Epic.class);

        try {
            Optional<Epic> existingEpic = Optional.ofNullable(taskManager.getEpicById(epic.getId()));
            if (existingEpic.isPresent()) {
                taskManager.updateEpic(epic);
                sendText(exchange, "Epic updated", 200);
            } else {
                taskManager.createTask(epic);
                sendText(exchange, "Epic created", 201);
            }
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteEpicById(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            Epic epic = taskManager.getEpicById(id);
            if (epic != null) {
                taskManager.removeEpicById(id);
                sendText(exchange, "Epic deleted", 200);
            } else {
                sendNotFound(exchange);
            }
        } catch (NumberFormatException e) {
            sendText(exchange, "{\"error\":\"Invalid Epic ID\"}", 400);
        }
    }
}
