package org.max.home.spoon;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.max.seminar.spoon.AbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimilarRecipesTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(SimilarRecipesTest.class);

    @Test
    void get_shouldReturn500() throws IOException {
        logger.info("Тест код ответ 500 запущен");
        //given
        logger.debug("Формирование мока для GET /recipes/715538/similar");
        stubFor(get(urlPathEqualTo("/recipes/715538/similar"))
                .willReturn(aResponse()
                        .withStatus(500).withBody("ERROR")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/recipes/715538/similar");
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/recipes/715538/similar")));
        assertEquals(500, response.getStatusLine().getStatusCode());
        assertEquals("ERROR", convertResponseToString(response));
    }
}