package servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
//import exceptions.HttpTaskNotCreated;
import exceptions.HttpTaskNotFoundException;
import model.Task;
import service.Managers;
import service.TaskManager;
import exceptions.TimeCrossException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

public class TasksHandler implements HttpHandler {

    TaskManager taskManager = Managers.getDefault();
    BaseHttpHandler baseHandler = new BaseHttpHandler();
    Gson gson;

    @Override
    public void handle(HttpExchange exc) throws IOException {
//        System.out.println("Началась обработка /tasks запроса от клиента.");

         //Обрабатываем запрос /tasks"
        String response = "";
        String method = exc.getRequestMethod();
//        System.out.println("Началась обработка " + method + " /hello запроса от клиента.");
        URI requestURI = exc.getRequestURI();
        String path = requestURI.getPath();
        String[] splitStrings = path.split("/");
        String idString = splitStrings[2];
        int idInt = Integer.parseInt(idString);
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.setPrettyPrinting();
//        Gson gson = gsonBuilder.create();
        //gson.fromJson(exc.getRequestBody())

        switch(method) {
            case "POST":
                InputStream inputStream = exc.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                JsonElement jsonElement = JsonParser.parseString(body);
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                if (idString.isEmpty()) {
                    Task task = new Task();
                    task.setTaskName(jsonObject.get("taskName").getAsString());
                    task.setDescription(jsonObject.get("description").getAsString());
                    task.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
                    task.setStartTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString(), Task.formatter));
                    try {
                        task = taskManager.createNewTask(task);
                        response = "Новая задача Task с id =" + task.getId() + " создана";

                        exc.sendResponseHeaders(201, 0);
                        try (OutputStream os = exc.getResponseBody()) {
                            //String response = "Привет " + name + "! Рады видеть на нашем сервере.";
                            os.write(response.getBytes());
                        }




//                        byte[] resp = response.getBytes(StandardCharsets.UTF_8);
//                        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
//                        exc.sendResponseHeaders(201, resp.length);
//                        exc.getResponseBody().write(resp);
//                        exc.close();
                        return;
                    } catch (TimeCrossException e) {
                        baseHandler.sendHasInteractions(exc);
                    }
                } else {
                    Task task = new Task();
                    task.setId(idInt);
                    task.setTaskName(jsonObject.get("taskName").getAsString());
                    task.setDescription(jsonObject.get("description").getAsString());
                    task.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
                    task.setStartTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString(), Task.formatter));
                    try {
                        taskManager.updateTask(task);
                        response = "Задача Task с id=" + idInt + " обновлена";
                        baseHandler.sendText(exc, response);
                        return;
                    } catch (TimeCrossException e) {
                        baseHandler.sendHasInteractions(exc);
                    }
                }
                break;
            case "GET":
                if (idString.isEmpty()) {
//                    response = taskManager.getAllTasks().toString(); //запрос всех задач
                    response = taskManager.getAllTasks().stream()
                            .map(Task::toString)
                            .collect(Collectors.joining("\n"));
                } else {
                    try {
                        response = taskManager.getTaskById(idInt).toString(); //запрос задачи по id
                    } catch (HttpTaskNotFoundException e) {
                        baseHandler.sendNotFound(exc, idInt);
                    }
                }
                break;
            case "DELETE":
                if (idString.isEmpty()) {
                    response = "Ошибка в запросе - укажите id задачи";
                    byte[] resp = response.getBytes(StandardCharsets.UTF_8);
                    exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    exc.sendResponseHeaders(404, resp.length);
                    exc.getResponseBody().write(resp);
                    exc.close();
//
//                    baseHandler.sendNotFound(exc, idInt);
                    return;
                } else {
                    try {
                        taskManager.deleteTaskById(idInt);
                        response = "Задача Task с id= " + idInt + " удалена";
                    } catch (HttpTaskNotFoundException e) {
                        baseHandler.sendNotFound(exc, idInt);
                    }
                }
                break;
            default:
                response = "Вы использовали какой-то другой метод!";
        }
        baseHandler.sendText(exc, response);
    }








}


