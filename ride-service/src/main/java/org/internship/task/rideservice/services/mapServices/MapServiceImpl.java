package org.internship.task.rideservice.services.mapServices;

import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.MapExceptions.ERROR_GET_COORDINATES;
import static org.internship.task.rideservice.util.constants.RideConstants.EARTH_RADIUS;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.internship.task.rideservice.clients.MapClient;
import org.internship.task.rideservice.dto.clientsdDto.GeocodeResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    private final MapClient mapClient;
    @Value("${api.key}")
    private String apiKey;

    public double[] getCoordinates(String address) {
        try {
            GeocodeResponse response = mapClient.getCoordinates(apiKey, address);

            if (response.getFeatures().isEmpty()) {
                throw new RuntimeException("No coordinates found for address: " + address);
            }

            List<Double> coordinates = response.getFeatures().get(0).getGeometry().getCoordinates();
            return new double[] {coordinates.get(0), coordinates.get(1)};
        } catch (Exception e) {
            throw new RuntimeException(ERROR_GET_COORDINATES + e.getMessage());
        }
    }

    public double getDistance(String startAddress, String finishAddress) {
        double[] startCord = getCoordinates(startAddress);
        double[] finishCord = getCoordinates(finishAddress);

        double startLat = startCord[1];
        double startLon = startCord[0];
        double finishLat = finishCord[1];
        double finishLon = finishCord[0];

        double deltaLat = Math.toRadians(finishLat - startLat);
        double deltaLon = Math.toRadians(finishLon - startLon);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
            Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(finishLat))
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }
}
