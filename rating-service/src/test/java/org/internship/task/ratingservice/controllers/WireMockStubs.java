package org.internship.task.ratingservice.controllers;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.internship.task.ratingservice.dto.clientsDto.GetRideResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class WireMockStubs {

    public static void setupGetRideByIdAndAbilityToRate(WireMockExtension wireMock, Long rideId,
                                                        GetRideResponse response) {
        wireMock.stubFor(
            WireMock.get(WireMock.urlEqualTo("/canBeRate/" + rideId))
                .willReturn(WireMock.aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        """
                            {
                                "id": %d,
                                "driverId": %d,
                                "passengerId": %d
                            }
                            """.formatted(response.getRideId(), response.getDriverId(), response.getPassengerId())
                    )
                ));
    }
}