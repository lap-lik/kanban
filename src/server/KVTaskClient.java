package server;

import exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final HttpClient client;
    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        client = HttpClient.newHttpClient();
        apiToken = registeredApiToken();
    }

    private String registeredApiToken() {
        try {
            URI uri = URI.create(url + "/register");
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось загрузить запрос, статус запроса: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Не удалось загрузить запрос.", e);
        }
    }

    public void put(String key, String json) {
        try {
            URI uri = URI.create(url + "/save" + key + "?API_TOKEN=" + apiToken);
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось загрузить запрос, статус запроса: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Не удалось загрузить запрос.", e);
        }
    }

    public String load(String key) {
        try {
            URI uri = URI.create(url + "/load" + key + "?API_TOKEN=" + apiToken);
            HttpRequest httpRequest = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new ManagerSaveException("Не удалось загрузить запрос, статус запроса: " + response.statusCode());
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Не удалось загрузить запрос.", e);
        }
    }
}
