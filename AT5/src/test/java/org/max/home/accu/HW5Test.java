package org.max.home.accu;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.Method;
import io.restassured.response.Response;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.max.home.error.Error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HW5Test extends AbstractTest {
    private static final Logger logger
            = LoggerFactory.getLogger(HW5Test.class);
    public static final String FORECASTS200 = "/forecasts/v1/daily/1day/257514";
    public static final String FORECASTS400 = "/forecasts/v1/daily/10day/690127";

    @Test
    void testAccuweatherForecastOneDay() {
        logger.info("testAccuweatherForecastOneDay запущен!");

        stubFor(get(urlPathEqualTo(FORECASTS200)).withHeader("Content-Type", containing("JSON"))
                .withQueryParam("i", containing("tring"))
                .willReturn(aResponse().withStatus(200).withBody("Проверка!!!")));

        String responseBody = given().queryParam("i", "string123")
                .header("Content-Type", "JSONChecking")
                .when().request(Method.GET, getBaseUrl() + FORECASTS200)
                .then().statusCode(200).extract().body().asString();

        Assertions.assertEquals("Проверка!!!", responseBody);

        logger.info("testAccuweatherForecastOneDay завершён!");

    }

    @Test
    void testAccuweatherForecastTenDays() {
        logger.info("testAccuweatherForecastTenDays запущен!");

        stubFor(get(urlPathEqualTo(FORECASTS400)).withQueryParam("i", equalTo("string"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse().withStatus(400).withBody("Ошибка!!!")));

        String responseBody = given().queryParam("i", "string")
                .header("Accept", "text/xml")
                .when().get(getBaseUrl() + FORECASTS400)
                .then().statusCode(400).extract().body().asString();

        Assertions.assertEquals("Ошибка!!!", responseBody);
        logger.info("testAccuweatherForecastTenDays завершён!");
    }

    @Test
    void testAccuweatherForecastTenDaysResponceEntity() {
        logger.info("testAccuweatherForecastTenDaysResponceEntity запущен");

        stubFor(get(urlPathMatching(FORECASTS400)).withQueryParam("i", equalTo("string"))
                .withHeader("Content-Type", containing("value"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "text/html")
                        .withBody("Ошибка123")));

        Response response = given().queryParam("i", "string")
                .header("Content-Type", "value")
                .when().get(getBaseUrl() + FORECASTS400 + "400");

        Assertions.assertAll(() -> Assertions.assertEquals(400, response.statusCode()),
                () -> Assertions.assertEquals("Ошибка123", response.body().asString()),
                () -> Assertions.assertEquals("text/html", response.header("Content-Type")));

        logger.info("testAccuweatherForecastTenDaysResponceEntity завершён!");
    }

    @Test
    void testAccuweatherForecastTenDaysErrorClass() throws IOException {
        logger.info("testAccuweatherForecastTenDaysErrorClass запущен");
        //given
        ObjectMapper mapper = new ObjectMapper();
        Error bodyResponse = new Error();
        bodyResponse.setCode("Unauthorized");
        bodyResponse.setMessage("Api Authorization failed");
        bodyResponse.setReference("/forecasts/v1/daily/10day/100?apikey=SipxdGArG1oT7JHAbLgGsAjIFArWwPCZ");

        stubFor(get(urlPathEqualTo(FORECASTS400))
                .withQueryParam("apikey", equalTo("1234567890"))
                .withHeader("Accept", equalTo("text/xml"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "text/html")
                        .withBody(mapper.writeValueAsString(bodyResponse))));
// Вариант 1

        String errorString = given().queryParam("apikey", "1234567890")
                .header("Accept", "text/xml")
                .when().get(getBaseUrl() + FORECASTS400)
                .then().statusCode(400).time(lessThan(2000L))
                .assertThat().header("Content-Type", "text/html")
                .extract().body().asString();

        Error error = mapper.readValue(errorString, Error.class);

        Assertions.assertEquals("Unauthorized", error.getCode());
        Assertions.assertEquals("Api Authorization failed", error.getMessage());
        Assertions.assertEquals("/forecasts/v1/daily/10day/100?apikey=SipxdGArG1oT7JHAbLgGsAjIFArWwPCZ",
                error.getReference());

// Вариант 2

        Response response = given().queryParam("apikey", "1234567890")
                .header("Accept", "text/xml")
                .when().get(getBaseUrl() + FORECASTS400);

        String errorStr = response.body().asString();
        Error er = mapper.readValue(errorStr, Error.class);

        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertEquals("Unauthorized", er.getCode());
        Assertions.assertEquals("text/html", response.header("Content-Type"));
        Assertions.assertEquals("Api Authorization failed", er.getMessage());
        Assertions.assertEquals("/forecasts/v1/daily/10day/100?apikey=SipxdGArG1oT7JHAbLgGsAjIFArWwPCZ",
                er.getReference());


        logger.info("testAccuweatherForecastTenDaysErrorClass завершён!");
    }

    @Test
    void testGBResponce() throws IOException, URISyntaxException {
        logger.info("testGBResponce запущен!");
        //given
        stubFor(get(urlPathEqualTo("/gb.ru/lessons/384443"))
                .withQueryParam("api", equalTo("1234567890"))
                .withQueryParam("token", equalTo("0987654321"))
                .withHeader("Connection", equalTo("keep-alive"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/html")
                        .withStatus(200)
                        .withBody("GeekBrains is greeting you - student")));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet((getBaseUrl() + "/gb.ru/lessons/384443"));
        request.addHeader("Connection", "keep-alive");

        URI uri = new URIBuilder(request.getURI())
                .addParameter("api", "1234567890")
                .addParameter("token", "0987654321")
                .build();
        request.setURI(uri);

        //when
        HttpResponse response = httpClient.execute(request);

        //then
        verify(getRequestedFor(urlPathEqualTo("/gb.ru/lessons/384443")));

        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Content-Type: text/html", response.getFirstHeader("Content-Type").toString());

        HttpEntity entity = response.getEntity();
        String bodyString = EntityUtils.toString(entity, "UTF-8");
        assertEquals("GeekBrains is greeting you - student", bodyString);
        logger.info("testGBResponce завершён!");
    }
}
