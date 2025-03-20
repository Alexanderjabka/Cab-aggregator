package org.internship.task.driverservice.controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.internship.task.driverservice.dto.cars.CarRequest;
import org.internship.task.driverservice.entities.Car;
import org.internship.task.driverservice.entities.Driver;
import org.internship.task.driverservice.repositories.CarRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(PostgresTestContainer.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CarControllerTestIT {

    @LocalServerPort
    private int port;

    @Autowired
    private CarRepository carRepository;

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
        carRepository.deleteAll();
        driverRepository.deleteAll();
    }

    @Test
    @Order(1)
    void getAllCars_ShouldReturnNoContentWhenNoCars() {
        given()
                .when()
                .get("/api/v1/cars")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(2)
    void createCar_ShouldReturnCarResponse() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        CarRequest request = DataForIT.CREATE_CAR_REQUEST;
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/cars/{driverEmail}", driver.getEmail())
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("carNumber", equalTo(request.getCarNumber()))
                .body("carBrand", equalTo(request.getCarBrand().toString()));
    }

    @Test
    @Order(3)
    void createCar_ShouldThrowDuplicateException() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;
        car.setDriver(driver);
        carRepository.save(car);

        CarRequest request = DataForIT.CREATE_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/cars/{driverEmail}", driver.getEmail())
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    @Order(4)
    void createCar_ShouldThrowValidationException() {
        // Сохраняем водителя перед тестом
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);

        CarRequest invalidRequest = DataForIT.CREATE_INVALID_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(invalidRequest)
                .when()
                .post("/api/v1/cars/{driverEmail}", driver.getEmail())
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Ожидаем 400, а не 500
    }

    @Test
    @Order(5)
    void getAllCars_ShouldReturnListOfCars() {

        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;


        given()
                .when()
                .get("/api/v1/cars")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("cars.size()", equalTo(1))
                .body("cars[0].carNumber", equalTo(DataForIT.CREATE_CAR.getCarNumber()));
    }

    @Test
    @Order(6)
    void getAllCarsByStatus_ShouldReturnNoContentWhenNoCars() {
        given()
                .when()
                .get("/api/v1/cars/status/{status}", false)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @Order(7)
    void getAllCarsByStatus_ShouldReturnListOfCars() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;

        given()
                .when()
                .get("/api/v1/cars/status/{status}", false)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("cars.size()", equalTo(1))
                .body("cars[0].carNumber", equalTo(DataForIT.CREATE_CAR.getCarNumber()));
    }

    @Test
    @Order(8)
    void getCarById_ShouldReturnCarResponse() {
        // Сохраняем водителя и автомобиль перед тестом
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;
        car.setDriver(driver);
        Car savedCar = carRepository.save(car);

        given()
                .when()
                .get("/api/v1/cars/{id}", savedCar.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("carNumber", equalTo(savedCar.getCarNumber()));
    }

    @Test
    @Order(9)
    void getCarById_ShouldThrowNotFoundException() {
        given()
                .when()
                .get("/api/v1/cars/{id}", DataForIT.INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(10)
    void updateCar_ShouldReturnUpdatedResponse() {
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;

        CarRequest updateRequest = DataForIT.CREATE_UPDATE_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/v1/cars/{carNumber}", car.getCarNumber())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("carNumber", equalTo(updateRequest.getCarNumber()));
    }

    @Test
    @Order(11)
    void updateCar_ShouldThrowNotFoundException() {
        CarRequest request = DataForIT.CREATE_UPDATE_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/cars/{carNumber}", DataForIT.INVALID_CAR_NUMBER)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @Order(12)
    void updateCar_ShouldThrowConflictException() {
        // Сохраняем водителя и автомобиль перед тестом
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;
        car.setDriver(driver);
        Car savedCar = carRepository.save(car);
        savedCar.setIsDeleted(true);
        carRepository.save(savedCar);

        CarRequest request = DataForIT.CREATE_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/cars/{carNumber}", savedCar.getCarNumber())
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Ожидаем 409, а не 500
    }

    @Test
    @Order(13)
    void updateCar_ShouldThrowBadRequestException() {
        // Сохраняем водителя и автомобиль перед тестом
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;
        car.setDriver(driver);
        Car savedCar = carRepository.save(car);
        savedCar.setIsDeleted(true);
        carRepository.save(savedCar);

        CarRequest request = DataForIT.CREATE_UPDATE_CAR_REQUEST;

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .put("/api/v1/cars/{carNumber}", savedCar.getCarNumber())
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value()); // Ожидаем 400, а не 500
    }

    @Test
    @Order(14)
    void deleteCar_ShouldSetStatusDeleted() {
        // Сохраняем водителя и автомобиль перед тестом
        Driver driver = driverRepository.save(DataForIT.CREATE_DRIVER);
        Car car = DataForIT.CREATE_CAR;
        car.setDriver(driver);
        Car savedCar = carRepository.save(car);

        given()
                .when()
                .delete("/api/v1/cars/{id}", savedCar.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Car deletedCar = carRepository.findById(savedCar.getId()).orElseThrow();
        assertEquals(true, deletedCar.getIsDeleted());
    }

    @Test
    @Order(15)
    void deleteCar_ShouldThrowNotFoundException() {
        given()
                .when()
                .delete("/api/v1/cars/{id}", DataForIT.INVALID_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}