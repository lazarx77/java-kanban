package serverTests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerEpicsTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerEpicsTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.clearAll();
        server.start();
    }

    @AfterEach
    public void shutDown() {
        server.stop(0);
    }

    @Test
    public void testAddEpic() throws IOException {
        // создаём задачу
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        String epicJson1 = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics");
            //создаем успешный запрос
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());


            // проверяем, что создалась одна задача с корректным именем
            List<Epic> epicsFromManager = taskManager.getAllEpics();
            Epic epic = epicsFromManager.getFirst();

            assertNotNull(epicsFromManager, "Задачи не возвращаются");
            assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
            assertEquals("epic1_name", epic.getTaskName(epic.getId()), "Некорректное имя задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testUpdateEpic() throws IOException {
        // создаём задачи
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        String epicJson1 = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics/");
            //создаем epic
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            Epic epic2 = new Epic();
            epic2.setTaskName("epic2_name");
            epic2.setDescription("epic2_description");
            String epicJson2 = gson.toJson(epic2);

            client = HttpClient.newHttpClient();
            url = URI.create("http://localhost:8080/epics");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson2))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            //обновляем задачу
            Epic epic2Updated = new Epic();
            epic2Updated.setTaskName("epic2_updated_name");
            epic2Updated.setDescription("epic2_description");
            String epicJson3 = gson.toJson(epic2Updated);

            // создаём запрос на обновление задачи
            url = URI.create("http://localhost:8080/epics/2");
            //создаем успешный запрос
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson3))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());

            //создаем неудачный запрос отсутствующей задачи
            url = URI.create("http://localhost:8080/tasks/3");

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            // проверяем, что создалась одна задача с корректным именем
            List<Epic> tasksFromManager = taskManager.getAllEpics();
            Epic epic = tasksFromManager.get(1);

            assertNotNull(tasksFromManager, "Задачи не возвращаются");
            //проверяем количество созданных задач
            assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
            //проеряем обновление задачи
            assertEquals("epic2_updated_name", epic.getTaskName(epic.getId()), "Некорректное имя задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и" +
                    " повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testGetEpic() throws IOException {
        // создаём задачу
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        String epicJson1 = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics");
            //создаем задачу в менеджере
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());

            url = URI.create("http://localhost:8080/epics/1");
            //получаем задачу
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(200, response.statusCode());

            //создаем запрос на получение отсутствующей задачи
            url = URI.create("http://localhost:8080/epics/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());


            //создаем запрос на получение всех задач
            url = URI.create("http://localhost:8080/epics/");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            class EpicsListTypeToken extends TypeToken<List<Epic>> {
            }
            List<Epic> epicsFromJson = gson.fromJson(response.body(), new EpicsListTypeToken().getType());
            Epic epic = epicsFromJson.getFirst();

            assertNotNull(epicsFromJson, "Задачи не возвращаются");
            //проверяем количество созданных задач
            assertEquals(1, epicsFromJson.size(), "Некорректное количество эпик-задач");
            //проеряем обновление задачи
            assertEquals("epic1_name", epic.getTaskName(epic.getId()), "Некорректное имя задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testDeleteEpic() throws IOException {
        // создаём задачу
        Epic epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        String epicJson1 = gson.toJson(epic1);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics");
            //создаем задачу в менеджере
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            // вызываем рест, отвечающий за создание задач
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());

            url = URI.create("http://localhost:8080/epics/2");
            //пытаемся удалить несуществующую задачу
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(404, response.statusCode());

            //создаем запрос на удаление существующей задачи
            url = URI.create("http://localhost:8080/epics/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());


            //создаем запрос на получение всех задач
            url = URI.create("http://localhost:8080/epics/");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            class EpicsListTypeToken extends TypeToken<List<Epic>> {
            }
            List<Epic> tasksFromJson = gson.fromJson(response.body(), new EpicsListTypeToken().getType());

            //проверяем количество созданных задач (едонственная задача удалена, список пуст)
            assertEquals(0, tasksFromJson.size(), "Некорректное количество задач");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }
}