package org.internship.task.ratingservice.controllers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.COMMENT;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.DRIVER_ID;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.ID;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.INVALID_ID;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.IS_DELETED;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.PASSENGER_ID;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.PASSENGER_RATINGS;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.RATINGS;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.RIDE_ID;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.SCORE;
import static org.internship.task.ratingservice.TestDataIT.DataForIT.WHO_RATE;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.internship.task.ratingservice.dto.RatingListResponse;
import org.internship.task.ratingservice.entities.Rating;
import org.internship.task.ratingservice.repositories.RatingRepository;
import org.internship.task.ratingservice.testContainerConfig.PostgresTestContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PostgresTestContainer.class)
@ActiveProfiles("test")
@WireMockTest
@Slf4j
@Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class RatingControllerTestIT {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @Autowired
    private RatingRepository ratingRepository;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresTestContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.getInstance()::getPassword);
        registry.add("server.url.ride", () -> wireMockExtension.baseUrl());
    }

    @BeforeAll
    static void setUpContainer() {
        PostgresTestContainer.getInstance().start();
    }

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
        ratingRepository.deleteAll();
    }

//    @Test
//    @Order(1)
//    public void testCreateRating_Success() {
//        WireMockStubs.setupGetRideByIdAndAbilityToRate(wireMockExtension, RIDE_ID, RIDE_RESPONSE);
//
//        given()
//            .contentType(ContentType.JSON)
//            .body(RATING_REQUEST)
//            .when()
//            .post("/api/v1/rating")
//            .then()
//            .statusCode(200)
//            .body("rideId", equalTo(RIDE_ID.intValue()))
//            .body("driverId", equalTo(DRIVER_ID.intValue()))
//            .body("passengerId", equalTo(PASSENGER_ID.intValue()))
//            .body("score", equalTo(SCORE.intValue()))
//            .body("comment", equalTo(COMMENT))
//            .body("whoRate", equalTo(WHO_RATE.name()));
//    }
//
//    @Test
//    @Order(2)
//    public void testCreateRating_AlreadyRated() {
//        WireMockStubs.setupGetRideByIdAndAbilityToRate(wireMockExtension, RIDE_ID, RIDE_RESPONSE);
//        ratingRepository.save(DataForIT.CREATE_RATING());
//
//        given()
//            .contentType(ContentType.JSON)
//            .body(RATING_REQUEST)
//            .when()
//            .post("/api/v1/rating")
//            .then()
//            .statusCode(HttpStatus.BAD_REQUEST.value());
//    }

    @Test
    @Order(3)
    public void testGetAllRatings_ReturnsListOfRatings() {
        ratingRepository.saveAll(RATINGS);

        Response response = given()
            .when()
            .get("/api/v1/rating")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        RatingListResponse ratingListResponse = response.as(RatingListResponse.class);
        assertThat(ratingListResponse.ratings()).hasSize(1);
    }

    @Test
    @Order(4)
    public void testGetAllRatings_ReturnsNoContentWhenNoRatings() {
        given()
            .when()
            .get("/api/v1/rating")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(5)
    public void testDeleteRating_Success() {
        Rating rating = ratingRepository.save(
            new Rating(ID, DRIVER_ID, PASSENGER_ID, RIDE_ID, SCORE, COMMENT, WHO_RATE, IS_DELETED));

        given()
            .when()
            .delete("/api/v1/rating/{id}", rating.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(6)
    public void testDeleteRating_NotFound() {
        given()
            .when()
            .delete("/api/v1/rating/{id}", INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(7)
    public void testGetAveragePassengerRating_Success() {
        ratingRepository.saveAll(PASSENGER_RATINGS);

        given()
            .when()
            .get("/api/v1/rating/average/passenger-rating/{passengerId}", PASSENGER_ID)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(equalTo("5.0"));
    }

    @Test
    @Order(8)
    public void testGetAveragePassengerRating_NoRatings() {
        given()
            .when()
            .get("/api/v1/rating/average/passenger-rating/{passengerId}", PASSENGER_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(9)
    public void testGetAverageDriverRating_Success() {
        ratingRepository.saveAll(RATINGS);

        given()
            .when()
            .get("/api/v1/rating/average/driver-rating/{driverId}", DRIVER_ID)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body(equalTo("5.0"));
    }

    @Test
    @Order(10)
    public void testGetAverageDriverRating_NoRatings() {
        given()
            .when()
            .get("/api/v1/rating/average/driver-rating/{driverId}", DRIVER_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}