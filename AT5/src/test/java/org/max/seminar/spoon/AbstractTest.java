package org.max.seminar.spoon;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.http.HttpResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;

public class AbstractTest {

    private static WireMockServer wireMockServer = new WireMockServer();
    private static final int port = 8080;
    private static String baseUrl;

    private static final Logger logger
            = LoggerFactory.getLogger(AbstractTest.class);

    @BeforeAll
    static void startServer() {
        baseUrl = "http://localhost:" + port;
        wireMockServer.start();
        configureFor("localhost", port);
        logger.info("Start WireMockServer on port {}",port);
    }

    @AfterAll
    static void stopServer() {
        wireMockServer.stop();
        logger.info("Stop WireMockServer");
    }

    //Вспомогательный метод - конвертор body to string
    public String convertResponseToString(HttpResponse response) throws IOException {
        logger.debug("convertResponseToString method call");
        try(InputStream responseStream = response.getEntity().getContent();
            Scanner scanner = new Scanner(responseStream, "UTF-8");) {
            String responseString = scanner.useDelimiter("\\Z").next();
            return responseString;
        }

    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
