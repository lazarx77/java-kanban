package servers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Task;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class HttpTaskServer extends InMemoryTaskManager {

    private static final int PORT = 8080;

    private HttpServer server;
    private Gson gson;

    private TaskManager taskManager;

    public HttpTaskServer() {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gson = gsonBuilder.create();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("api/v1/tasks", this::handleTasks);
    }

    private void handleTasks(HttpExchange httpExchange) {
    }

    public static void main(String[] args) throws IOException {

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);


    }

    static class TasksHandler implements HttpHandler {
        TaskManager taskManager = Managers.getDefault();
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath().split("/")[0];

            switch (exchange.getRequestMethod()) {
                case "GET" :
                case "POST" :

            }
        }

        private void handleGetTasks(HttpExchange exchange) throws IOException {
            String response = taskManager.getAllTasks().stream()
                    .map(Task::toString)
                    .collect(Collectors.joining("\n"));
            //writeResponse(exchange, response, 200);
        }
    }

}

class SubtasksHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class EpicsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class HistoryHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

class PrioritizedHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

    }
}

