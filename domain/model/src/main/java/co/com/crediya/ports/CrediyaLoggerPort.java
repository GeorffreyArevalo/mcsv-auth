package co.com.crediya.ports;


public interface CrediyaLoggerPort {

    void info(String message);
    void info(String message,  Object... args);
}
