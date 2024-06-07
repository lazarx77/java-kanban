package servers;

import com.sun.net.httpserver.HttpExchange;
import exceptions.InMemoryTaskNotFoundException;
import exceptions.TimeCrossException;
import model.Task;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;
import service.TaskType;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    protected HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        super.handle(exc);
        HistoryManager historyManager = taskManager.getHistoryManager();

        switch (method) {
            case "GET":
                System.out.println("GET history");
                if (idString.isEmpty()) {
                    response = gson.toJson(historyManager.getHistory());
                } else {
                    try {
                        response = gson.toJson(taskManager.getTaskById(idInt));
                    } catch (InMemoryTaskNotFoundException e) {
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
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                    sendText(exc, response, 200);
                }
                break;
            default:
                response = "Метод не разрешен! Доступные методы: GET, POST, DELETE.";
                sendText(exc, response, 405);
        }
    }
}
