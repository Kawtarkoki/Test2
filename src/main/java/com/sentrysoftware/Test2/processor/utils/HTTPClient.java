package com.sentrysoftware.Test2.processor.utils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
public class HTTPClient {
    public static JsonNode get(String url) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (InputStream inputStream = connection.getInputStream()) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readTree(inputStream);
                }
            } else {
                System.out.println("HTTP GET request failed with response code: " + responseCode);
                return null;
            }
        } finally{
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
