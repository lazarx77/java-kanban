package serverTests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskStatus;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerPrioritizedTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();
    Epic epic1;
    Subtask subtask2;
    String epicJson1;
    String subtaskJson2;
    Task task3;
    String taskJson3;

    public HttpTaskManagerPrioritizedTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        taskManager.clearAll();
        server.start();
        epic1 = new Epic();
        epic1.setTaskName("epic1_name");
        epic1.setDescription("epic1_description");
        epicJson1 = gson.toJson(epic1);

        subtask2 = new Subtask();
        subtask2.setTaskName("subtask2_name");
        subtask2.setDescription("subtask1_description");
        subtask2.setStatus(TaskStatus.NEW);
        subtask2.setDuration(Duration.ofMinutes(20));
        subtask2.setStartTime(LocalDateTime.of(2022, 12, 1, 12, 25, 0));
        subtask2.setEpicId(1);
        subtaskJson2 = gson.toJson(subtask2);

        task3 = new Task();
        task3.setTaskName("task1_name");
        task3.setDescription("task1_description");
        task3.setStatus(TaskStatus.NEW);
        task3.setDuration(Duration.ofMinutes(20));
        task3.setStartTime(LocalDateTime.of(2022, 12, 1, 10, 25, 0));
        taskJson3 = gson.toJson(task3);
    }

    @AfterEach
    public void shutDown() {
        server.stop(0);
    }

    @Test
    public void testGetPrioritized() throws IOException {
        // создаём задачи
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics"); // id = 1
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

            url = URI.create("http://localhost:8080/subtasks");
            request = HttpRequest //id = 2
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/tasks"); // id = 3
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(taskJson3))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            url = URI.create("http://localhost:8080/prioritized/");
            //получаем задачу
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(200, response.statusCode());

            class PrioritizedListTypeToken extends TypeToken<List<Task>> {
            }
            List<Task> prioritizedFromJson = gson.fromJson(response.body(), new PrioritizedListTypeToken().getType());
            System.out.println(response.body());

            assertNotNull(prioritizedFromJson, "Задачи не возвращаются");
            //проверяем количество сохраненных в истории задач
            assertEquals(2, prioritizedFromJson.size(), "Некорректное количество задач");
            //проеряем наполнение истории

            assertEquals("[\n" +
                    "  {\n" +
                    "    \"id\": 3,\n" +
                    "    \"taskName\": \"task1_name\",\n" +
                    "    \"description\": \"task1_description\",\n" +
                    "    \"status\": \"NEW\",\n" +
                    "    \"duration\": 20,\n" +
                    "    \"startTime\": \"01.12.2022 - 10:25\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"epicId\": 1,\n" +
                    "    \"id\": 2,\n" +
                    "    \"taskName\": \"subtask2_name\",\n" +
                    "    \"description\": \"subtask1_description\",\n" +
                    "    \"status\": \"NEW\",\n" +
                    "    \"duration\": 20,\n" +
                    "    \"startTime\": \"01.12.2022 - 12:25\"\n" +
                    "  }\n" +
                    "]", response.body(), "Некорректное сохранение задач в prioritized");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }
}