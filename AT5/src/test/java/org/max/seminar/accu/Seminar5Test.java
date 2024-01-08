package org.max.seminar.accu;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;

public class Seminar5Test extends AbstractTest {

    public static final String AUTOCOMPLETE = "/locations/v1/cities/autocomplete";

    @Test
    void get_shouldReturn200WithRestAssure() {

        stubFor(get(urlPathEqualTo(AUTOCOMPLETE))
                .withQueryParam("s", equalTo("string"))
                .withQueryParam("i", containing("integer"))
                .willReturn(aResponse().withStatus(200).withBody("Привет!!!")));

        HashMap<String,String> hashMap = new HashMap();
        hashMap.put("s", "string");
        hashMap.put("i", "integer123");

        String string = given().queryParams(hashMap).when().get(getBaseUrl()
                        + AUTOCOMPLETE)
                .then().statusCode(200).extract().body().asString();

        Assertions.assertEquals("Привет!!!", string);

        //___________________________________________________________________________

        stubFor(get(urlPathMatching(AUTOCOMPLETE + "400"))
                .willReturn(aResponse().withStatus(400).withBody("ПОКА")));

//        given().when().get(getBaseUrl() + LOCATIONS_V_1_CITIES_AUTOCOMPLETE)
//                .then().statusCode(200);
//
        String string1 = given().queryParam("q", "Samara")
                .when().get(getBaseUrl() + AUTOCOMPLETE + "400" + "?yes=yes")
                .then().statusCode(400).extract().body().asString();

        Assertions.assertEquals("ПОКА", string1);
    }
}
