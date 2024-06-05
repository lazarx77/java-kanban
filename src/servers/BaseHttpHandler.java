package servers;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BaseHttpHandler {
    protected void sendText(HttpExchange exc, String text) throws IOException {
        exc.sendResponseHeaders(200, 0);
        try (OutputStream os = exc.getResponseBody()) {
            //String response = "Привет " + name + "! Рады видеть на нашем сервере.";
            os.write(text.getBytes());
        }





        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exc.sendResponseHeaders(200, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }

    protected void sendNotFound(HttpExchange exc, int id) throws IOException {
        byte[] resp = ("Не назван указанный id=" + id).getBytes(StandardCharsets.UTF_8);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exc.sendResponseHeaders(404, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }

    protected void sendHasInteractions(HttpExchange exc)  throws IOException {
        byte[] resp = ("Задача пересекается с существующими. " +
                "Обновление или создание данной задачи невозможно").getBytes(StandardCharsets.UTF_8);
        exc.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exc.sendResponseHeaders(406, resp.length);
        exc.getResponseBody().write(resp);
        exc.close();
    }

}