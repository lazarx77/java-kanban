package servers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.TaskNotFoundException;

import model.Task;
import service.TaskManager;
import exceptions.TimeCrossException;
import service.TaskType;

import java.io.IOException;

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
                if (idString.isEmpty()) {
                    try {
                        task = taskManager.createNewTask(task);
                        response = "Новая задача " + task.getType() + " с id = " + task.getId() + " создана";
                    } catch (TimeCrossException e) {
                        sendHasInteractions(exc);
                    }
                } else {
                    task.setId(idInt);
                    try {
                        taskManager.updateTask(task);
                        response = "Задача " + task.getType() + " с id = " + idInt + " обновлена";
                    } catch (TimeCrossException e) {
                        sendHasInteractions(exc);
                    } catch (TaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                sendText(exc, response, 201);
                break;
            case "GET":
                System.out.println("GET tasks");
                if (idString.isEmpty()) {
                    response = gson.toJson(taskManager.getAllTasks());
                } else {
                    try {
                        response = gson.toJson(taskManager.getTaskById(idInt));
                    } catch (TaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                sendText(exc, response, 200);
                break;
            case "DELETE":
                System.out.println("DELETE tasks");
                if (idString.isEmpty() || idInt == 0) {
                    response = "Ошибка в запросе - укажите id задачи в числовом виде";
                    sendText(exc, response, 400);
                } else {
                    try {
                        taskManager.deleteTaskById(idInt);
                        response = "Задача " + TaskType.TASK + " с id = " + idInt + " удалена";
                    } catch (TaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                    sendText(exc, response, 200);
                }
                break;
            default:
                response = "Метод не разрешен! Доступные методы для tasks: GET, POST, DELETE.";
                sendText(exc, response, 405);
        }
    }
}


