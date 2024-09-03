package service;

public class HttpException extends RuntimeException {
    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(String message) {
        super(message);
    }
}
