package org.max.seminar.spoon;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.max.home.spoon.AbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassifyCuisineTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ClassifyCuisineTest.class);


    @Test
    void post_shouldReturn200() throws IOException {
        logger.info("Тест код ответ 200 запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        ClassifyCuisineDTO bodyResponse = new ClassifyCuisineDTO();
        bodyResponse.setCuisine("CuisineValue");

        logger.debug("Формирование мока для POST /recipes/cuisine");
        stubFor(post(urlEqualTo("/recipes/cuisine"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(containing("\"title\": \"Pork roast with green beans\""))
                .willReturn(aResponse()
                        .withStatus(200).withBody(mapper.writeValueAsString(bodyResponse))));


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getBaseUrl()+"/recipes/cuisine");
        request.addHeader("Content-Type", "application/x-www-form-urlencoded");
        request.setEntity(new StringEntity("{" +
                "\"title\": \"Pork roast with green beans\"" +
                "}"));
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(postRequestedFor(urlEqualTo("/recipes/cuisine"))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        ClassifyCuisineDTO bodyToCheck = mapper.readValue(response.getEntity().getContent(), ClassifyCuisineDTO.class);
        assertEquals("CuisineValue", bodyToCheck.getCuisine());
    }

    @Test
    void post_shouldReturn403() throws IOException {
        logger.info("Тест код ответ 403 запущен");
        //given
        logger.debug("Формирование мока для POST /recipes/cuisine");
        stubFor(post(urlEqualTo("/recipes/cuisine"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(containing("\"title\": \"Pork roast with green beans\""))
                .willReturn(aResponse()
                        .withStatus(403).withBody("ERROR")));


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(getBaseUrl()+"/recipes/cuisine");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity("{" +
                "\"title\": \"Pork roast with green beans\"" +
                "}"));
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(postRequestedFor(urlEqualTo("/recipes/cuisine"))
                .withHeader("Content-Type", equalTo("application/json")));
        assertEquals(403, response.getStatusLine().getStatusCode());
        assertEquals("ERROR", convertResponseToString(response));
    }

}
