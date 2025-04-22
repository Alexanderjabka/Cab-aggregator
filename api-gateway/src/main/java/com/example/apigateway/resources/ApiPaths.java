package com.example.apigateway.resources;

public class ApiPaths {
    public static final String API_PREFIX = "/api/v1/";

    public static final String DEBUG_HEADERS = API_PREFIX + "drivers/debug-headers";

    public static final String PASSENGERS = API_PREFIX + "passengers";
    public static final String PASSENGERS_STATUS = API_PREFIX + "passengers/status/**";
    public static final String PASSENGER_BY_ID = API_PREFIX + "passengers/{id}";
    public static final String PASSENGERS_IS_FREE = API_PREFIX + "passengers/isFree/**";

    public static final String CARS = API_PREFIX + "cars";
    public static final String CARS_STATUS = API_PREFIX + "cars/status/**";
    public static final String CAR_BY_ID = API_PREFIX + "cars/{id}";

    public static final String DRIVERS = API_PREFIX + "drivers";
    public static final String DRIVERS_STATUS = API_PREFIX + "drivers/status/**";
    public static final String DRIVER_BY_ID = API_PREFIX + "drivers/{id}";
    public static final String DRIVERS_ASSIGN = API_PREFIX + "drivers/assign";
    public static final String DRIVERS_RELEASE = API_PREFIX + "drivers/release/**";

    public static final String RIDES = API_PREFIX + "rides";
    public static final String RIDES_STATUS = API_PREFIX + "rides/status";
    public static final String RIDE_BY_ID = API_PREFIX + "rides/{id}";
    public static final String RIDES_CHANGE_STATUS = API_PREFIX + "rides/change-status/**";

    public static final String RATING = API_PREFIX + "rating";
    public static final String RATING_AVG_PASSENGER = API_PREFIX + "rating/average/passenger-rating/**";
    public static final String RATING_AVG_DRIVER = API_PREFIX + "rating/average/driver-rating/**";
}
