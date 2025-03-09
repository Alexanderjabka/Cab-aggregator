package org.internship.task.rideservice.controllers;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.internship.task.rideservice.testDataIT.DataForIT.CHANGE_STATUS_JSON;
import static org.internship.task.rideservice.testDataIT.DataForIT.CREATE_RIDE;
import static org.internship.task.rideservice.testDataIT.DataForIT.CREATE_RIDE_JSON;
import static org.internship.task.rideservice.testDataIT.DataForIT.DRIVER_ID;
import static org.internship.task.rideservice.testDataIT.DataForIT.FINISH_ADDRESS;
import static org.internship.task.rideservice.testDataIT.DataForIT.FINISH_LATITUDE;
import static org.internship.task.rideservice.testDataIT.DataForIT.FINISH_LONGITUDE;
import static org.internship.task.rideservice.testDataIT.DataForIT.ID;
import static org.internship.task.rideservice.testDataIT.DataForIT.INVALID_ID;
import static org.internship.task.rideservice.testDataIT.DataForIT.NEW_FINISH_ADDRESS;
import static org.internship.task.rideservice.testDataIT.DataForIT.NEW_START_ADDRESS;
import static org.internship.task.rideservice.testDataIT.DataForIT.ORDER_DATE_TIME;
import static org.internship.task.rideservice.testDataIT.DataForIT.PASSENGER_HAS_ACTIVE_RIDE_MESSAGE;
import static org.internship.task.rideservice.testDataIT.DataForIT.PASSENGER_ID;
import static org.internship.task.rideservice.testDataIT.DataForIT.PRICE;
import static org.internship.task.rideservice.testDataIT.DataForIT.RIDE_NOT_FOUND_MESSAGE;
import static org.internship.task.rideservice.testDataIT.DataForIT.RIDE_STATUS_INCORRECT_MESSAGE;
import static org.internship.task.rideservice.testDataIT.DataForIT.START_ADDRESS;
import static org.internship.task.rideservice.testDataIT.DataForIT.START_LATITUDE;
import static org.internship.task.rideservice.testDataIT.DataForIT.START_LONGITUDE;
import static org.internship.task.rideservice.testDataIT.DataForIT.STATUS_CREATED;
import static org.internship.task.rideservice.testDataIT.DataForIT.STATUS_EN_ROUTE_TO_PASSENGER;
import static org.internship.task.rideservice.testDataIT.DataForIT.UPDATE_RIDE_JSON;
import static org.internship.task.rideservice.testDataIT.DataForIT.WIREMOCK_API_KEY;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.internship.task.rideservice.dto.RideListResponse;
import org.internship.task.rideservice.dto.RideResponse;
import org.internship.task.rideservice.entities.Ride;
import org.internship.task.rideservice.enums.Status;
import org.internship.task.rideservice.repositories.RideRepository;
import org.internship.task.rideservice.testContainerConfig.PostgresTestContainer;
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
class RideControllerTestIT {
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();

    @Autowired
    private RideRepository rideRepository;
    @LocalServerPort
    private int port;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresTestContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.getInstance()::getPassword);
        registry.add("server.url.driver", () -> wireMockExtension.baseUrl());
        registry.add("server.url.passenger", () -> wireMockExtension.baseUrl());
        registry.add("server.url.map", () -> wireMockExtension.baseUrl());
        registry.add("api.key", () -> WIREMOCK_API_KEY);
    }

    @BeforeAll
    static void setUpContainer() {
        PostgresTestContainer.getInstance().start();
    }

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
        rideRepository.deleteAll();
    }

    @Test
    @Order(1)
    public void testCreateRide_Success() {
        WireMockStubs.getPassengerByIdAndStatusResponse(wireMockExtension, PASSENGER_ID);
        WireMockStubs.getAssignDriverResponse(wireMockExtension, DRIVER_ID);
        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, START_ADDRESS, START_LATITUDE,
            START_LONGITUDE);
        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, FINISH_ADDRESS, FINISH_LATITUDE,
            FINISH_LONGITUDE);

        given()
            .contentType(ContentType.JSON)
            .body(CREATE_RIDE_JSON)
            .when()
            .post("/api/v1/rides")
            .then()
            .statusCode(201)
            .body("passengerId", equalTo(1))
            .body("driverId", equalTo(1))
            .body("status", equalTo(STATUS_CREATED));
    }

    @Test
    @Order(2)
    public void testCreateRide_BadRequest() {
        Ride ride = CREATE_RIDE;
        ride.setStatus(Status.CREATED);
        rideRepository.save(ride);

        WireMockStubs.getPassengerByIdAndStatusResponse(wireMockExtension, PASSENGER_ID);
        WireMockStubs.getAssignDriverResponse(wireMockExtension, DRIVER_ID);
        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, START_ADDRESS, START_LATITUDE,
            START_LONGITUDE);
        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, FINISH_ADDRESS, FINISH_LATITUDE,
            FINISH_LONGITUDE);

        given()
            .contentType(ContentType.JSON)
            .body(CREATE_RIDE_JSON)
            .when()
            .post("/api/v1/rides")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("message", equalTo(PASSENGER_HAS_ACTIVE_RIDE_MESSAGE));
    }

    @Test
    @Order(3)
    void getAllRides_ReturnsListOfRides() {
        Ride ride =
            new Ride(ID, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, Status.CREATED, ORDER_DATE_TIME,
                PRICE);
        rideRepository.save(ride);

        Response response = given()
            .when()
            .get("/api/v1/rides")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        RideListResponse rideListResponse = response.as(RideListResponse.class);
        assertThat(rideListResponse.rides()).hasSize(1);
        assertThat(rideListResponse.rides().get(0).getId()).isEqualTo(ID);
    }

    @Test
    @Order(4)
    void getAllRides_ReturnsNoContentWhenNoRides() {
        given()
            .when()
            .get("/api/v1/rides")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(5)
    void getAllRidesByStatus_ReturnsListOfRides() {
        Ride ride =
            new Ride(ID, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, Status.CREATED, ORDER_DATE_TIME,
                PRICE);
        rideRepository.save(ride);

        Response response = given()
            .queryParam("status", STATUS_CREATED)
            .when()
            .get("/api/v1/rides/status")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        RideListResponse rideListResponse = response.as(RideListResponse.class);
        assertThat(rideListResponse.rides()).hasSize(1);
        assertThat(rideListResponse.rides().get(0).getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    @Order(6)
    void getAllRidesByStatus_ReturnsNoContentWhenNoRidesWithStatus() {
        given()
            .queryParam("status", STATUS_CREATED)
            .when()
            .get("/api/v1/rides/status")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(7)
    void getRideById_ReturnsRide() {
        Ride ride =
            new Ride(null, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, Status.CREATED, ORDER_DATE_TIME,
                PRICE);
        ride = rideRepository.save(ride);
        Long rideId = ride.getId();

        Response response = given()
            .when()
            .get("/api/v1/rides/{id}", rideId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        RideResponse rideResponse = response.as(RideResponse.class);
        assertThat(rideResponse.getId()).isEqualTo(rideId);
        assertThat(rideResponse.getStatus()).isEqualTo(Status.CREATED);
    }

    @Test
    @Order(8)
    void getRideById_ReturnsNotFoundWhenRideDoesNotExist() {
        given()
            .when()
            .get("/api/v1/rides/{id}", INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(9)
    void getRideByIdAndAbilityToRate_ReturnsRideWhenStatusAllowsRating() {
        Ride ride =
            new Ride(null, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, Status.COMPLETED, ORDER_DATE_TIME,
                PRICE);
        ride = rideRepository.save(ride);
        Long rideId = ride.getId();

        Response response = given()
            .when()
            .get("/api/v1/rides/canBeRate/{id}", rideId)
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        RideResponse rideResponse = response.as(RideResponse.class);
        assertThat(rideResponse.getId()).isEqualTo(rideId);
        assertThat(rideResponse.getStatus()).isEqualTo(Status.COMPLETED);
    }

    @Test
    @Order(10)
    void getRideByIdAndAbilityToRate_ThrowsExceptionWhenStatusDoesNotAllowRating() {
        Ride ride =
            new Ride(ID, PASSENGER_ID, DRIVER_ID, START_ADDRESS, FINISH_ADDRESS, Status.CREATED, ORDER_DATE_TIME,
                PRICE);
        rideRepository.save(ride);

        given()
            .when()
            .get("/api/v1/rides/canBeRate/{id}", ID)
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(11)
    void getRideByIdAndAbilityToRate_ReturnsNotFoundWhenRideDoesNotExist() {
        given()
            .when()
            .get("/api/v1/rides/canBeRate/{id}", INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(12)
    public void testUpdateRide_Success() {
        Ride ride = CREATE_RIDE;
        ride.setStatus(Status.CREATED);
        ride = rideRepository.save(ride);

        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, NEW_START_ADDRESS, START_LATITUDE,
            START_LONGITUDE);
        WireMockStubs.getCoordinatesResponse(wireMockExtension, WIREMOCK_API_KEY, NEW_FINISH_ADDRESS, FINISH_LATITUDE,
            FINISH_LONGITUDE);

        given()
            .contentType(ContentType.JSON)
            .body(UPDATE_RIDE_JSON)
            .when()
            .put("/api/v1/rides/{id}", ride.getId())
            .then()
            .statusCode(200)
            .body("passengerId", equalTo(1))
            .body("driverId", equalTo(1))
            .body("status", equalTo(STATUS_CREATED))
            .body("startAddress", equalTo(NEW_START_ADDRESS))
            .body("finishAddress", equalTo(NEW_FINISH_ADDRESS));

        Ride updatedRide = rideRepository.findById(ride.getId()).orElseThrow();
        assertThat(updatedRide.getStartAddress()).isEqualTo(NEW_START_ADDRESS);
        assertThat(updatedRide.getFinishAddress()).isEqualTo(NEW_FINISH_ADDRESS);
    }

    @Test
    @Order(13)
    public void testUpdateRide_RideNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body(UPDATE_RIDE_JSON)
            .when()
            .put("/api/v1/rides/{id}", INVALID_ID)
            .then()
            .statusCode(404)
            .body("message", equalTo(RIDE_NOT_FOUND_MESSAGE + INVALID_ID));
    }

    @Test
    @Order(14)
    public void testUpdateRide_RideStatusIncorrect() {
        Ride ride = CREATE_RIDE;
        ride.setStatus(Status.COMPLETED);
        rideRepository.save(ride);

        given()
            .contentType(ContentType.JSON)
            .body(UPDATE_RIDE_JSON)
            .when()
            .put("/api/v1/rides/{id}", 1)
            .then()
            .statusCode(400)
            .body("message", equalTo(RIDE_STATUS_INCORRECT_MESSAGE + Status.COMPLETED));
    }

    @Test
    @Order(15)
    public void testChangeStatus_Success() {
        Ride ride = CREATE_RIDE;
        rideRepository.save(ride);

        WireMockStubs.getReleaseDriverResponse(wireMockExtension, DRIVER_ID);

        given()
            .contentType(ContentType.JSON)
            .body(CHANGE_STATUS_JSON)
            .when()
            .put("/api/v1/rides/change-status/{id}", ride.getId())
            .then()
            .statusCode(200)
            .body("status", equalTo(STATUS_EN_ROUTE_TO_PASSENGER));

        Ride updatedRide = rideRepository.findById(ride.getId()).orElseThrow();
        assertThat(updatedRide.getStatus()).isEqualTo(Status.EN_ROUTE_TO_PASSENGER);
    }

    @Test
    @Order(16)
    public void testChangeStatus_RideNotFound() {
        given()
            .contentType(ContentType.JSON)
            .body(CHANGE_STATUS_JSON)
            .when()
            .put("/api/v1/rides/change-status/{id}", INVALID_ID)
            .then()
            .statusCode(404)
            .body("message", equalTo(RIDE_NOT_FOUND_MESSAGE + INVALID_ID));
    }

    @Test
    @Order(17)
    public void testChangeStatus_RideStatusIncorrect() {
        Ride ride = CREATE_RIDE;
        ride.setStatus(Status.COMPLETED);
        rideRepository.save(ride);

        given()
            .contentType(ContentType.JSON)
            .body(CHANGE_STATUS_JSON)
            .when()
            .put("/api/v1/rides/change-status/{id}", ride.getId())
            .then()
            .statusCode(400)
            .body("message", equalTo(RIDE_STATUS_INCORRECT_MESSAGE + Status.COMPLETED));
    }
}