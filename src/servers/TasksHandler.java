package servers;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import exceptions.InMemoryTaskNotFoundException;
import model.Epic;
import model.Task;
import model.TaskStatus;
import service.TaskManager;
import exceptions.TimeCrossException;
import service.TaskType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;

public class TasksHandler extends BaseHttpHandler {

    protected TasksHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        super.handle(exc);

        switch (method) {
            case "POST":
                System.out.println("POST tasks");
                Task task = gson.fromJson(body, Task.class);
//                Task task = new Task();
//                task.setTaskName(jsonObject.get("name").getAsString());
//                task.setDescription(jsonObject.get("description").getAsString());
//                task.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
//                task.setStartTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString(), Task.formatter));
//                task.setStatus(TaskStatus.valueOf(jsonObject.get("status").getAsString()));
                if (idString.isEmpty()) {
                    try {
                        task = taskManager.createNewTask(task);
                        response = "Новая задача " + task.getType() + " с id = " + task.getId() + " создана";
                        exc.sendResponseHeaders(201, 0);
                        try (OutputStream os = exc.getResponseBody()) {
                            os.write(response.getBytes(DEFAULT_CHARSET));
                        }
//                        return;
                    } catch (TimeCrossException e) {
                        sendHasInteractions(exc);
                    }
                } else {
                    task.setId(idInt);
                    try {
                        taskManager.updateTask(task);
                        response = "Задача " + task.getType() + " с id = " + idInt + " обновлена";
                        sendText(exc, response);
                        return;
                    } catch (TimeCrossException e) {
                        sendHasInteractions(exc);
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                break;
            case "GET":
                System.out.println("GET tasks");
                if (idString.isEmpty()) {
                    response = gson.toJson(taskManager.getAllTasks());
                } else {
                    try {
                        response = gson.toJson(taskManager.getTaskById(idInt));
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                break;
            case "DELETE":
                System.out.println("DELETE tasks");
                if (idString.isEmpty() || idInt == 0) {
                    response = "Ошибка в запросе - укажите id задачи в числовом виде";
                    byte[] resp = response.getBytes(DEFAULT_CHARSET); //пробую разные способы отправки ответов
                    exc.sendResponseHeaders(400, resp.length);
                    exc.getResponseBody().write(resp);
                    exc.close();
                    return;
                } else {
                    try {
                        taskManager.deleteTaskById(idInt);
                        response = "Задача " + TaskType.TASK + " с id = " + idInt + " удалена";
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                break;
            default:
                response = "Метод не разрешен! Доступные методы: GET, POST, DELETE.";
                exc.sendResponseHeaders(405, 0);
                try (OutputStream os = exc.getResponseBody()) {
                    os.write(response.getBytes(DEFAULT_CHARSET));
                    return;
                }
        }
        sendText(exc, response);
    }
}


