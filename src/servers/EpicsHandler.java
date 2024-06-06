package servers;

import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import exceptions.InMemoryTaskNotFoundException;
import model.Epic;
import model.Task;
import model.TaskStatus;
import service.TaskManager;
import service.TaskType;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicsHandler extends BaseHttpHandler {

    protected EpicsHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        super.handle(exc);
//        response = ""; //переменная для формирования ответа
//        method = exc.getRequestMethod(); //вызываем метод запроса
//        URI requestURI = exc.getRequestURI();
//        String path = requestURI.getPath(); //получаем путь запроса
//        String[] splitStrings = path.split("/"); //делим путь на составляющие
//        idString = "";
//        idInt = 0;
//        if (splitStrings.length == 3) { // получаем переданный в пути id задачи
//            idString = splitStrings[2];
//            try {
//                idInt = Integer.parseInt(idString);
//            } catch (NumberFormatException e) {
//                System.out.println(e.getMessage());
//            }
//        }
//        inputStream = exc.getRequestBody();
//        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
//        jsonElement = JsonParser.parseString(body);
//        jsonObject = jsonElement.getAsJsonObject();

        switch (method) {
            case "POST":
                System.out.println("POST epics");
                //System.out.println(body);
//                try {
                    Epic epic = gson.fromJson(body, Epic.class);
                String str = epic.toString();
//                } catch (Exception exception) {
//                    System.out.println(exception.getMessage());
//                }
////                Epic epic = new Epic();
////                epic.setTaskName(jsonObject.get("taskName").getAsString());
////                epic.setDescription(jsonObject.get("description").getAsString());
////                epic.setDuration(Duration.ofMinutes(jsonObject.get("duration").getAsInt()));
////                epic.setStartTime(LocalDateTime.parse(jsonObject.get("dateTime").getAsString(), Task.formatter));
////                epic.setStatus(TaskStatus.valueOf(jsonObject.get("status").getAsString()));
                if (idString.isEmpty()) {
                    System.out.println(epic);
                    epic = taskManager.createNewEpic(epic);
                    response = "Новая задача " + epic.getType() + " с id = " + epic.getId() + " создана";
                    exc.sendResponseHeaders(201, 0);
                    try (OutputStream os = exc.getResponseBody()) {
                        os.write(response.getBytes(DEFAULT_CHARSET));
                        return;
                    }
                } else {
                    epic.setId(idInt);
                    epic.setStatus(TaskStatus.valueOf(jsonObject.get("status").getAsString()));
                    try {
                        taskManager.updateEpic(epic);
                        response = "Задача " + epic.getType() + " с id = " + idInt + " обновлена";
                        sendText(exc, response);
                        return;
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                break;
            case "GET":
                System.out.println("GET epics");
                if (idString.isEmpty()) {
                    response = gson.toJson(taskManager.getAllEpics());
//                    response = taskManager.getAllEpics().toString();
                } else {
                    try {
                        response = gson.toJson(taskManager.getEpicById(idInt));
//                        response = taskManager.getEpicById(idInt).toString();
                    } catch (InMemoryTaskNotFoundException e) {
                        sendNotFound(exc, idInt);
                    }
                }
                break;
            case "DELETE":
                System.out.println("DELETE epics");
                if (idString.isEmpty() || idInt == 0) {
                    response = "Ошибка в запросе - укажите порядковый id задачи в числовом виде, начиная с 1";
                    byte[] resp = response.getBytes(DEFAULT_CHARSET);
                    exc.sendResponseHeaders(400, resp.length);
                    exc.getResponseBody().write(resp);
                    exc.close();
                    return;
                } else {
                    try {
                        taskManager.deleteTaskById(idInt);
                        response = "Задача " + TaskType.EPIC + " с id= " + idInt + " удалена";
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


