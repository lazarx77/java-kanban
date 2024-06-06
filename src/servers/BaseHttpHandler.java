package servers;

import adapters.LocalDateTimeAdapter;
import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {


    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected TaskManager taskManager;
    protected String response; //поле для хранения ответа
    protected String method; //поле для хранения вызываемого метода Http
    protected String idString; //переменная для получения id задачи из пути запроса
    protected int idInt; // она же но в int
    protected InputStream inputStream;
    protected String body;
    protected JsonElement jsonElement;
    protected JsonObject jsonObject;

    protected final Gson gson = new GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create(); // завершаем построение gson

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        //Предварительная обработка запроса
        System.out.println("Началась обработка запроса от клиента.");

        response = ""; //переменная для формирования ответа
        method = exc.getRequestMethod(); //вызываем метод запроса
        URI requestURI = exc.getRequestURI();
        String path = requestURI.getPath(); //получаем путь запроса
        String[] splitStrings = path.split("/"); //делим путь на составляющие
        idString = "";
        idInt = 0;
        if (splitStrings.length == 3) { // получаем переданный в пути id задачи
            idString = splitStrings[2];
            try {
                idInt = Integer.parseInt(idString);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        inputStream = exc.getRequestBody();
        body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
//        jsonElement = JsonParser.parseString(body);
//        jsonObject = jsonElement.getAsJsonObject();
    }

    protected void sendText(HttpExchange exc, String text) throws IOException {
        exc.sendResponseHeaders(200, 0);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        try (OutputStream os = exc.getResponseBody()) {
            os.write(text.getBytes(DEFAULT_CHARSET));
        }
    }

    protected void sendNotFound(HttpExchange exc, int id) throws IOException {
        byte[] resp = ("Не найдена задача указанным id=" + id).getBytes(DEFAULT_CHARSET);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exc.sendResponseHeaders(404, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }

    protected void sendHasInteractions(HttpExchange exc) throws IOException {
        byte[] resp = ("Задача пересекается с существующими. " +
                "Обновление или создание данной задачи невозможно").getBytes(DEFAULT_CHARSET);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exc.sendResponseHeaders(406, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }
}