package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;

public class PrioritizedTaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedTaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        if ("GET".equals(exchange.getRequestMethod())) {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, response, 200);
        } else {
            sendText(exchange, "{\"error\":\"Method Not Allowed\"}", 405);
        }
    }
}
