package service;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer httpServer;
    private final TaskManager taskManager;
    private static final Gson gson = new Gson();

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        setupHandlers();
    }

    private void setupHandlers() {
        httpServer.createContext("/tasks", new TaskHandler(taskManager, gson));
        httpServer.createContext("/subtasks", new SubtaskHandler(taskManager, gson));
        httpServer.createContext("/epics", new EpicHandler(taskManager, gson));
        httpServer.createContext("/history", new HistoryHandler(taskManager, gson));
        httpServer.createContext("/prioritized", new PrioritizedTaskHandler(taskManager, gson));
    }

    public void start() {
        httpServer.start();
        System.out.println("Server started on port " + PORT);
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Server stopped.");
    }

    public static void main(String[] args) throws IOException {
        Manager manager = new Manager();
        TaskManager taskManager = manager.getDefault();
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}
