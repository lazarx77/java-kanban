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

public class HttpTaskManagerHistoryTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();
    Epic epic1;
    Subtask subtask2;
    String epicJson1;
    String subtaskJson2;

    public HttpTaskManagerHistoryTest() throws IOException {
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
    }

    @AfterEach
    public void shutDown() {
        server.stop(0);
    }

    @Test
    public void testGetHistory() throws IOException {
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

            url = URI.create("http://localhost:8080/subtasks/2");
            //получаем задачу
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(200, response.statusCode());

            //создаем запрос на получение epic
            url = URI.create("http://localhost:8080/epics/1");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            //создаем запрос на получение history
            url = URI.create("http://localhost:8080/history/");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            class HistoryListTypeToken extends TypeToken<List<Task>> {
            }
            List<Task> historyFromJson = gson.fromJson(response.body(), new HistoryListTypeToken().getType());
            System.out.println(response.body());

            assertNotNull(historyFromJson, "Задачи не возвращаются");
            //проверяем количество сохраненных в истории задач
            assertEquals(2, historyFromJson.size(), "Некорректное количество задач");
            //проеряем наполнение истории

            assertEquals("[\n" +
                    "  {\n" +
                    "    \"epicId\": 1,\n" +
                    "    \"id\": 2,\n" +
                    "    \"taskName\": \"subtask2_name\",\n" +
                    "    \"description\": \"subtask1_description\",\n" +
                    "    \"status\": \"NEW\",\n" +
                    "    \"duration\": 20,\n" +
                    "    \"startTime\": \"01.12.2022 - 12:25\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"endTime\": \"01.12.2022 - 13:05\",\n" +
                    "    \"subtasksIds\": [\n" +
                    "      2\n" +
                    "    ],\n" +
                    "    \"id\": 1,\n" +
                    "    \"taskName\": \"epic1_name\",\n" +
                    "    \"description\": \"epic1_description\",\n" +
                    "    \"status\": \"NEW\",\n" +
                    "    \"duration\": 20,\n" +
                    "    \"startTime\": \"01.12.2022 - 12:25\"\n" +
                    "  }\n" +
                    "]", response.body(), "Некорректное сохранение задач в истории");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }
}