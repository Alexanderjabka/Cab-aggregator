package org.internship.task.rideservice.controllers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.com.google.common.net.HttpHeaders;


public class WireMockStubs {

    public static void getPassengerByIdAndStatusResponse(WireMockExtension wireMockExtension, Long passengerId) {
        wireMockExtension.stubFor(
            WireMock.get(urlPathEqualTo("/api/v1/passengers/isFree/" + passengerId))
                .willReturn(aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody("{\"free\": true, \"id\": " + passengerId + "}")
                    .withStatus(200)));
    }

    public static void getAssignDriverResponse(WireMockExtension wireMockExtension, Long driverId) {
        wireMockExtension.stubFor(
            WireMock.post(urlPathEqualTo("/api/v1/drivers/assign"))  // Вероятно POST, а не PUT
                .willReturn(aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody("{\"driverId\": " + driverId + "}")  // Соответствует ожидаемой структуре
                    .withStatus(200)));
    }

    public static void getReleaseDriverResponse(WireMockExtension wireMockExtension, Long driverId) {
        wireMockExtension.stubFor(
            WireMock.put(urlPathEqualTo("/release/" + driverId))
                .willReturn(aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withStatus(200)));
    }

    public static void getCoordinatesResponse(WireMockExtension wireMockExtension, String apiKey, String address,
                                              double latitude, double longitude) {
        wireMockExtension.stubFor(
            WireMock.get(urlPathEqualTo("/search"))
                .withQueryParam("api_key", equalTo(apiKey))
                .withQueryParam("text", equalTo(address))
                .willReturn(aResponse()
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        "{\"features\": [{\"geometry\": {\"coordinates\": [" + longitude + ", " + latitude + "]}}]}")));
    }
}