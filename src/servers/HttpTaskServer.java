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

public class HttpTaskServer  { //extends InMemoryTaskManager {

    private final int PORT = 8080;

    private HttpServer server;
    private Gson gson;

    private TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        taskManager = Managers.getDefault();
//        GsonBuilder gsonBuilder = new GsonBuilder();
        //gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
//        gson = gsonBuilder.create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = Managers.getDefault();
        //gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        //gson = gsonBuilder.create();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TasksHandler());
        server.start();
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer();
    }

    //GsonBuilder gsonBuilder = new GsonBuilder();


//    public static void main(String[] args) throws IOException {
       // TaskManager taskManager = Managers.getDefault();
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
//        gsonBuilder.create();
        //TaskManager taskManager = Managers.getDefault();

        //public HttpTaskServer(TaskManager taskManager) throws IOException {
//        this.taskManager = taskManager;
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
//        gson = gsonBuilder.create();
//        server = HttpServer.create(new InetSocketAddress(PORT), 0);
//        server.createContext("/tasks", new TasksHandler());
//        server.createContext("/epics", new EpicsHandler());
//        server.createContext("/subtasks", new SubtasksHandler);
//        server.createContext("/prioritized", new PrioritizedHandler);
//        server.createContext("/history". new HistoryHandler);
       // server.start();
        //System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        //server.createContext("api/v1/tasks", this::TasksHandler);
        // }
   // }

}


