package org.max.demo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Демонстрация работы библиотеки WireMock
 */
public class WireMockTest {


    static WireMockServer wireMockServer = new WireMockServer();

    //Инициализация http сервера
    @BeforeAll
    static void startServer() {
        wireMockServer.start();
        configureFor("localhost", 8080);
    }

    //Демонстрация совпадения url
    @Test
    void testUrlEqual() throws IOException {
        //given
        stubFor(get(urlEqualTo("/test/urlequal")).willReturn(aResponse().withBody("Welcome to test!")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/test/urlequal");
        //when
        HttpResponse httpResponse = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlEqualTo("/test/urlequal")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("Welcome to test!", convertResponseToString(httpResponse));
    }

    //Демонстрация совпадения url по маске
    @Test
    void testURLMatching() throws IOException {
        //given
        stubFor(get(urlPathMatching("/test/urlmatching/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("\"library\": \"WireMock\"")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/test/urlmatching/wiremock");
        //when
        HttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertResponseToString(httpResponse);
        //then
        verify(getRequestedFor(urlEqualTo("/test/urlmatching/wiremock")));
        assertEquals(200, httpResponse.getStatusLine().getStatusCode());
        assertEquals("application/json", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("\"library\": \"WireMock\"", stringResponse);
    }

    //Демонстрация совпадения header
    @Test
    void testHeaderMatching() throws IOException {
        //given
        stubFor(get(urlPathEqualTo("/test/headermatching/wiremock"))
                .withHeader("Accept", matching("text/.*"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "text/html")
                        .withBody("503 Service Unavailable")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/test/headermatching/wiremock");
        request.addHeader("Accept", "text/html");

        //when
        HttpResponse httpResponse = httpClient.execute(request);
        String stringResponse = convertResponseToString(httpResponse);
        //then
        verify(getRequestedFor(urlEqualTo("/test/headermatching/wiremock")));
        assertEquals(503, httpResponse.getStatusLine().getStatusCode());
        assertEquals("text/html", httpResponse.getFirstHeader("Content-Type").getValue());
        assertEquals("503 Service Unavailable", stringResponse);
    }

    //Демонстрация совпадения body
    @Test
    void testBodyMatching() throws IOException {
        //given
        stubFor(post(urlEqualTo("/test/bodymatching/wiremock"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"library\": \"WireMock\""))
                .withRequestBody(containing("\"creator\": \"GB\""))
                .willReturn(aResponse()
                        .withStatus(200)));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/test/bodymatching/wiremock");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("{\n" +
                "    \"library\": \"WireMock\",\n" +
                "    \"creator\": \"GB\"\n" +
                "}"));

        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(postRequestedFor(urlEqualTo("/test/bodymatching/wiremock"))
                .withHeader("Content-Type", equalTo("application/json")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    //Демонстрация приоритета при определении мок ответа
    @Test
    void testPriority() throws IOException {
        //given
        stubFor(get(urlPathMatching("/test/priority/.*"))
                .atPriority(1)
                .willReturn(aResponse()
                        .withStatus(200)));
        stubFor(get(urlPathEqualTo("/test/priority/wiremock"))
                .atPriority(2)
                .withHeader("Accept", matching("text/.*"))
                .willReturn(aResponse()
                        .withStatus(503)));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/test/priority/wiremock");
        request.addHeader("Accept", "text/xml");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlEqualTo("/test/priority/wiremock")));
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    //Остановка http сервиса
    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
    }

    //Вспомогательный метод - конвертор body to string
    private String convertResponseToString(HttpResponse response) throws IOException {

        try(InputStream responseStream = response.getEntity().getContent();
            Scanner scanner = new Scanner(responseStream, "UTF-8");) {
            String responseString = scanner.useDelimiter("\\Z").next();
            return responseString;
        }
    }
}
