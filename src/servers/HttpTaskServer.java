package servers;
import com.sun.net.httpserver.HttpServer;

import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final int PORT = 8080;
    private HttpServer server;
    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/subtasks", new SubtasksHandler(taskManager));
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту");
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer();
    }
}


