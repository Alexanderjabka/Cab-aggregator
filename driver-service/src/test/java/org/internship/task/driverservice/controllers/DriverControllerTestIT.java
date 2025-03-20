package org.internship.task.driverservice.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.internship.task.driverservice.dto.drivers.DriverRequest;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.enums.Gender;
import org.internship.task.driverservice.repositories.DriverRepository;
import org.internship.task.driverservice.testContainerConfig.PostgresTestContainer;
import org.internship.task.driverservice.testDataIT.DataForIT;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PostgresTestContainer.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DriverControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private DriverRepository driverRepository;

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
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        driverRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getAllDrivers_ShouldReturnListOfDrivers() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .get("/api/v1/drivers")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("drivers.size()", equalTo(1))
                .body("drivers[0].email", equalTo(driver.getEmail()));
    }

    @Test
    @Order(2)
    void getAllDrivers_ShouldReturnNoContent() {
        given()
                .when()
                .get("/api/v1/drivers")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(3)
    void getAllDriversByStatus_ShouldReturnListOfDrivers() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .get("/api/v1/drivers/status/{status}", false)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("drivers.size()", equalTo(1))
                .body("drivers[0].email", equalTo(driver.getEmail()));
    }

    @Test
    @Order(4)
    void getAllDriversByStatus_ShouldReturnNoContent() {
        given()
                .when()
                .get("/api/v1/drivers/status/{status}", false)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(5)
    void getDriverById_ShouldReturnDriver() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .get("/api/v1/drivers/{id}", driver.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(driver.getEmail()));
    }

    @Test
    @Order(6)
    void getDriverById_ShouldReturnNotFound() {
        given()
                .when()
                .get("/api/v1/drivers/{id}", DataForIT.INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(7)
    void createDriver_ShouldCreateDriver() {
        DriverRequest request = DataForIT.CREATE_DRIVER_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/drivers")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("email", equalTo(request.getEmail()));
    }

    @Test
    @Order(8)
    void createDriver_WithExistingEmail_ShouldReturnConflict() {
        driverRepository.save(DataForIT.CREATE_DRIVER);

        DriverRequest request = DataForIT.CREATE_DRIVER_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/drivers")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(9)
    void createDriver_WithInvalidData_ShouldReturnBadRequest() {
        DriverRequest invalidRequest = DataForIT.CREATE_INVALID_DRIVER_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when()
                .post("/api/v1/drivers")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Order(10)
    void updateDriver_ShouldUpdateDriver() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        DriverRequest updateRequest = DataForIT.CREATE_UPDATE_DRIVER_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/v1/drivers/{email}", driver.getEmail())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("email", equalTo(updateRequest.getEmail()));
    }

    @Test
    @Order(11)
    void updateDriver_WithInvalidEmail_ShouldReturnNotFound() {
        DriverRequest request = DataForIT.CREATE_UPDATE_DRIVER_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/drivers/{email}", DataForIT.INVALID_EMAIL)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(12)
    void updateDriver_WithExistingEmail_ShouldReturnConflict() {
        Driver driver1 = driverRepository.save(DataForIT.CREATE_DRIVER);
        Driver driver2 = driverRepository.save(new Driver(
                2L, "another", "another@example.com", "+375298876655", Gender.MALE, null, false, false
        ));

        DriverRequest request = DataForIT.CREATE_UPDATE_DRIVER_REQUEST;
        request.setEmail(driver2.getEmail());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/drivers/{email}", driver1.getEmail())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(13)
    void assignDriver_ShouldAssignDriver() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .put("/api/v1/drivers/assign")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("isInRide", equalTo(true));
    }

    @Test
    @Order(14)
    void assignDriver_WithNoFreeDrivers_ShouldReturnNotFound() {
        given()
                .when()
                .put("/api/v1/drivers/assign")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(15)
    void releaseDriver_ShouldReleaseDriver() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        driver.setIsInRide(true);
        driverRepository.save(driver);

        given()
                .when()
                .put("/api/v1/drivers/release/{driverId}", driver.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(16)
    void releaseDriver_WithInvalidId_ShouldReturnNotFound() {
        given()
                .when()
                .put("/api/v1/drivers/release/{driverId}", DataForIT.INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(17)
    void releaseDriver_WithAlreadyFreeDriver_ShouldReturnBadRequest() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .put("/api/v1/drivers/release/{driverId}", driver.getId())
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @Order(18)
    void deleteDriver_ShouldDeleteDriver() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        given()
                .when()
                .delete("/api/v1/drivers/{id}", driver.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(19)
    void deleteDriver_WithInvalidId_ShouldReturnNotFound() {
        given()
                .when()
                .delete("/api/v1/drivers/{id}", DataForIT.INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}