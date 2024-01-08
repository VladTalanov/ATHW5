package org.max;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Демо для демонстрации запуска сервиса
 */
public class RunWireMock {

    public static void main(String[] args) {
        WireMockServer wireMockServer = new WireMockServer();
        wireMockServer.start();
        configureFor("localhost", 8080);

        stubFor(get(urlEqualTo("/test/urlequal")).willReturn(aResponse().withBody("Welcome to test!")));

        //бесконечный цикл
        while (true) {

        }
    }
}
