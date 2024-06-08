package servers;

import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.HistoryManager;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BaseHttpHandler implements HttpHandler {


    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected HistoryManager historyManager;
    protected TaskManager taskManager;
    protected String response; //поле для хранения ответа
    protected String method; //поле для хранения вызываемого метода Http
    protected String idString; //переменная для получения id задачи из пути запроса
    protected int idInt; // она же но в int
    protected InputStream inputStream;
    protected String body;
    protected JsonElement jsonElement;
    protected JsonObject jsonObject;
    protected String subtasksString = "";
    //private static final List<String> URL_NAMES = List.of("tasks", "epics","subtasks", "history", "prioritized");

    protected final Gson gson = HttpTaskServer.getGson();

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        //Предварительная обработка запроса
        System.out.println("Началась обработка запроса от клиента.");

        method = exc.getRequestMethod(); //вызываем метод запроса
        String path = exc.getRequestURI().getPath(); //получаем путь запроса
        String[] splitStrings = path.split("/"); //делим путь на составляющие

//        if (URL_NAMES.contains(splitStrings[1])) {
            idString = "";
            idInt = 0;
            if (splitStrings.length >= 3) { // получаем переданный в пути id задачи
                idString = splitStrings[2];
                try {
                    idInt = Integer.parseInt(idString);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
            if (splitStrings.length == 4 && splitStrings[3].equals("subtasks")) {
                subtasksString = splitStrings[3];
            }

            inputStream = exc.getRequestBody();
            body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
//        } else {
//            response = "Недопустимый URL. Доступные URL: " + URL_NAMES;
//            sendText(exc, response, 422);
//        }
    }

    protected void sendText(HttpExchange exc, String text, int code) throws IOException {
        exc.sendResponseHeaders(code, 0);
        if (code == 200) {
            exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        }
        try (OutputStream os = exc.getResponseBody()) {
            os.write(text.getBytes(DEFAULT_CHARSET));
        }
    }

    protected void sendNotFound(HttpExchange exc, int id) throws IOException {
        byte[] resp = ("Не найдена задача указанным id=" + id).getBytes(DEFAULT_CHARSET);
        exc.sendResponseHeaders(404, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }

    protected void sendHasInteractions(HttpExchange exc) throws IOException {
        byte[] resp = ("Задача пересекается с существующими. " +
                "Обновление или создание данной задачи невозможно").getBytes(DEFAULT_CHARSET);
        exc.sendResponseHeaders(406, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }
}