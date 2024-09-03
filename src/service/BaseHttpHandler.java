package service;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange exchange, String text, int statusCode) {
        try {
            byte[] responseBytes = text.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            exchange.getResponseBody().write(responseBytes);
        } catch (IOException e) {
            throw new HttpException("Failed to send response", e);
        } finally {
            exchange.close();
        }
    }

    protected void sendNotFound(HttpExchange exchange) {
        sendText(exchange, "{\"error\":\"Not Found\"}", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) {
        sendText(exchange, "{\"error\":\"Task conflict\"}", 406);
    }

    @Override
    public void handle(HttpExchange exchange) {

    }
}
