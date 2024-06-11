package serverTests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.Subtask;
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

public class HttpTaskManagerSubtasksTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer server = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();
    Epic epic1;
    Subtask subtask2;
    Subtask subtask3;
    String epicJson1;
    String subtaskJson2;
    String subtaskJson3;

    public HttpTaskManagerSubtasksTest() throws IOException {
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

        subtask3 = new Subtask();
        subtask3.setTaskName("subtask3_name");
        subtask3.setDescription("subtask3_description");
        subtask3.setStatus(TaskStatus.DONE);
        subtask3.setDuration(Duration.ofMinutes(20));
        subtask3.setStartTime(LocalDateTime.of(2022, 12, 1, 13, 45, 0));
        subtask3.setEpicId(1);
        subtaskJson3 = gson.toJson(subtask3);

    }

    @AfterEach
    public void shutDown() {
        server.stop(0);
    }

    @Test
    public void testAddSubtask() throws IOException {
        // создаём задачу

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics");
            //создаем epic для subtasks
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            url = URI.create("http://localhost:8080/subtasks");
            //создаем subtasks
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                    .build();

            //проверяем код создания подзадачи
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson3))
                    .build();

            //проверяем код создания подзадачи
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(201, response.statusCode());

            // проверяем, что создались 2 подзадачи с корректным именем
            List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
            Subtask subtask = subtasksFromManager.getFirst();

            assertNotNull(subtasksFromManager, "Задачи не возвращаются");
            assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
            assertEquals("subtask2_name", subtask.getTaskName(subtask.getId()), "Некорректное имя " +
                    "задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testUpdateSubtask() throws IOException {

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        try {
            URI url = URI.create("http://localhost:8080/epics/");
            //создаем epic
            HttpRequest request = HttpRequest //id = 1
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(epicJson1))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
            //создаем подзадачи
            url = URI.create("http://localhost:8080/subtasks");
            request = HttpRequest //id = 2
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            request = HttpRequest // id = 3
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson3))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());

            //обновляем задачу
            Subtask subtask3Updated = new Subtask();
            subtask3Updated.setTaskName("subtask3_updated_name");
            subtask3Updated.setDescription("subtask3_description_updated");
            subtask3Updated.setStatus(TaskStatus.IN_PROGRESS);
            subtask3Updated.setDuration(Duration.ofMinutes(20));
            subtask3Updated.setStartTime(LocalDateTime.of(2022, 12, 5, 13, 45,
                    0));
            subtask3Updated.setEpicId(1);
            String subtaskJson4 = gson.toJson(subtask3Updated);

            // создаём запрос на обновление задачи
            url = URI.create("http://localhost:8080/subtasks/3");
            //создаем успешный запрос
            request = HttpRequest // id = 3
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson4))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(201, response.statusCode());


            //создаем задачу, которая не будет записана
            Subtask subtaskNot3Updated = new Subtask();
            subtaskNot3Updated.setTaskName("subtask3_updated_name");
            subtaskNot3Updated.setDescription("subtask3_description");
            subtaskNot3Updated.setStatus(TaskStatus.DONE);
            subtaskNot3Updated.setDuration(Duration.ofMinutes(20));
            subtaskNot3Updated.setStartTime(LocalDateTime.of(2022, 12, 1, 12, 25,
                    0));
            subtaskNot3Updated.setEpicId(1);
            String subtaskJson5 = gson.toJson(subtaskNot3Updated);

            // создаём запрос на обновление задачи
            url = URI.create("http://localhost:8080/subtasks/3");

            //создаем неудачный запрос (задача не будет добавлена из-за пересечения)
            request = HttpRequest // id = 3
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson5))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(406, response.statusCode());

            //создаем неудачный запрос отсутствующей задачи
            url = URI.create("http://localhost:8080/subtasks/4");

            //создаем неудачный запрос (задача не будет добавлена из-за пересечения)
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .POST(HttpRequest.BodyPublishers.ofString(subtaskJson2))
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            // проверяем, что обновленная задача сохранена с корректным именем
            List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
            System.out.println(subtasksFromManager);

            Subtask subtask = subtasksFromManager.get(1);

            assertNotNull(subtasksFromManager, "Задачи не возвращаются");
            //проверяем количество созданных задач
            assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
            //проеряем обновление задачи
            assertEquals("subtask3_updated_name", subtask.getTaskName(subtask.getId()), "Некорректное" +
                    " имя задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и" +
                    " повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testGetSubtask() throws IOException {
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

            //создаем запрос на получение отсутствующей задачи
            url = URI.create("http://localhost:8080/subtasks/3");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());

            //создаем запрос на получение всех задач
            url = URI.create("http://localhost:8080/subtasks/");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            class SubtasksListTypeToken extends TypeToken<List<Subtask>> {
            }
            List<Subtask> subtasksFromJson = gson.fromJson(response.body(), new SubtasksListTypeToken().getType());
            Subtask subtask = subtasksFromJson.getFirst();

            assertNotNull(subtasksFromJson, "Задачи не возвращаются");
            //проверяем количество созданных задач
            assertEquals(1, subtasksFromJson.size(), "Некорректное количество задач");
            //проеряем обновление задачи
            assertEquals("subtask2_name", subtask.getTaskName(subtask.getId()), "Некорректное имя " +
                    "задачи");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }

    @Test
    public void testDeleteSubtask() throws IOException {
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
            //пытаемся удалить несуществующую задачу
            url = URI.create("http://localhost:8080/subtasks/3"); // id = 3

            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // проверяем код ответа
            assertEquals(404, response.statusCode());

            //создаем запрос на удаление существующей задачи
            url = URI.create("http://localhost:8080/subtasks/2");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .DELETE()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());

            //создаем запрос на получение всех подзадач
            url = URI.create("http://localhost:8080/subtasks/");
            request = HttpRequest
                    .newBuilder()
                    .uri(url)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            class SubtasksListTypeToken extends TypeToken<List<Subtask>> {
            }
            List<Subtask> subtasksFromJson = gson.fromJson(response.body(), new SubtasksListTypeToken().getType());

            //проверяем количество созданных задач (едонственная задача удалена, список пуст)
            assertEquals(0, subtasksFromJson.size(), "Некорректное количество подзадач");
        } catch (InterruptedException e) {
            System.out.println("Во время выполнения запроса возникла ошибка. Проверьте, пожалуйста, URL-адрес и " +
                    "повторите попытку");
        } catch (IllegalArgumentException e) {
            System.out.println("Введённый вами адрес не соответствует формату URL. Попробуйте, пожалуйста, снова");
        }
    }
}