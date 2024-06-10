package servers;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    /*обработчик запросов prioritized */

    protected PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exc) throws IOException {
        super.handle(exc);

        if (method.equals("GET")) {
            System.out.println("GET prioritized");
            response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exc, response, 200);
        } else {
            response = "Метод не разрешен! Доступный метод для prioritized: GET.";
            sendText(exc, response, 405);
        }
    }
}
