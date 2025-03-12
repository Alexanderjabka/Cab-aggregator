package org.internship.task.passengerservice.controllersIT;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.internship.task.passengerservice.dto.PassengerRequest;
import org.internship.task.passengerservice.entities.Passenger;
import org.internship.task.passengerservice.repositories.PassengerRepository;
import org.internship.task.passengerservice.testContainerConfig.PostgresTestContainer;
import org.internship.task.passengerservice.testDataIT.DataForIT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PostgresTestContainer.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PassengerControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private PassengerRepository passengerRepository;

    @BeforeAll
    static void setUpContainer() {
        PostgresTestContainer.getInstance().start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresTestContainer.getInstance()::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.getInstance()::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.getInstance()::getPassword);
    }

    @BeforeEach
    void setUpBeforeEach() {
        passengerRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }


    @Test
    @Order(1)
    void getAllPassengers_ShouldReturnNoContentWhenNoPassengers() {
        passengerRepository.deleteAll();

        given()
            .when()
            .get("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(2)
    void createPassenger_ShouldReturnPassengerResponse() {
        PassengerRequest request = DataForIT.CREATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("name", equalTo(request.getName()))
            .body("email", equalTo(request.getEmail()));
    }

    @Test
    @Order(3)
    void createPassenger_ShouldThrowDuplicateException() {
        PassengerRequest request = DataForIT.CREATE_REQUEST;
        passengerRepository.save(DataForIT.PASSENGER);

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(4)
    void createPassenger_ShouldThrowValidationException() {
        PassengerRequest invalidRequest = DataForIT.INVALID_CREATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(invalidRequest)
            .when()
            .post("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Order(5)
    void getAllPassengers_ShouldReturnListOfPassengers() {
        passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .get("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("passengers.size()", equalTo(1))
            .body("passengers[0].name", equalTo(DataForIT.PASSENGER.getName()));
    }

    @Test
    @Order(6)
    void getAllPassengersByStatus_ShouldReturnNoContentWhenNoPassengers() {
        passengerRepository.deleteAll();

        given()
            .when()
            .get("/api/v1/passengers/status/{status}", false)
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(7)
    void getAllPassengersByStatus_ShouldReturnListOfPassengers() {
        passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .get("/api/v1/passengers/status/{status}", false)
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("passengers.size()", equalTo(1))
            .body("passengers[0].name", equalTo(DataForIT.PASSENGER.getName()));
    }

    @Test
    @Order(8)
    void getPassengerByIdAndStatus_ShouldReturnPassenger() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .get("/api/v1/passengers/isFree/{id}", savedPassenger.getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", equalTo(savedPassenger.getName()))
            .body("email", equalTo(savedPassenger.getEmail()));
    }

    @Test
    @Order(9)
    void getPassengerByIdAndStatus_ShouldThrowNotFoundException() {
        given()
            .when()
            .get("/api/v1/passengers/isFree/{id}", DataForIT.INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(10)
    void deletePassenger_ShouldSetStatusDeleted() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .delete("/api/v1/passengers/{id}", savedPassenger.getId())
            .then()
            .statusCode(HttpStatus.NO_CONTENT.value());

        Passenger deletedPassenger = passengerRepository.findById(savedPassenger.getId()).orElseThrow();
        assertEquals(true, deletedPassenger.getIsDeleted());
    }

    @Test
    @Order(11)
    void deletePassenger_ShouldThrowNotFoundException() {
        given()
            .when()
            .delete("/api/v1/passengers/{id}", DataForIT.INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(12)
    void updatePassenger_ShouldReturnUpdatedResponse() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);
        PassengerRequest updateRequest = DataForIT.UPDATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(updateRequest)
            .when()
            .put("/api/v1/passengers/{email}", savedPassenger.getEmail())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", equalTo(updateRequest.getName()))
            .body("email", equalTo(updateRequest.getEmail()));
    }

    @Test
    @Order(13)
    void updatePassenger_ShouldThrowNotFoundException() {
        PassengerRequest request = DataForIT.UPDATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .put("/api/v1/passengers/{email}", "nonexistent@example.com")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(14)
    void updatePassenger_ShouldThrowConflictException() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);
        PassengerRequest request = DataForIT.INVALID_CREATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .put("/api/v1/passengers/{email}", savedPassenger.getEmail())
            .then()
            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Order(15)
    void updatePassenger_ShouldThrowBadRequestException() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);
        savedPassenger.setIsDeleted(true);
        passengerRepository.save(savedPassenger);

        PassengerRequest request = DataForIT.UPDATE_REQUEST;

        given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .put("/api/v1/passengers/{email}", savedPassenger.getEmail())
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(16)
    void getPassenger_ShouldReturnPassengerResponse() {
        Passenger savedPassenger = passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .get("/api/v1/passengers/{id}", savedPassenger.getId())
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("name", equalTo(savedPassenger.getName()))
            .body("email", equalTo(savedPassenger.getEmail()));
    }

    @Test
    @Order(17)
    void getPassenger_ShouldThrowNotFoundException() {
        given()
            .when()
            .get("/api/v1/passengers/{id}", DataForIT.INVALID_ID)
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(18)
    void getAllPassengers_ShouldReturnPagedResults() {
        passengerRepository.save(DataForIT.PASSENGER);

        given()
            .when()
            .get("/api/v1/passengers")
            .then()
            .statusCode(HttpStatus.OK.value())
            .body("passengers[0].name", equalTo("sasha"));
    }
}