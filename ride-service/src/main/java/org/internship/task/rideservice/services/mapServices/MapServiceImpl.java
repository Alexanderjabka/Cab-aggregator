package org.internship.task.rideservice.services.mapServices;

import static org.internship.task.rideservice.util.constantMessages.exceptionMessages.MapExceptions.ERROR_GET_COORDINATES;
import static org.internship.task.rideservice.util.constants.RideConstants.EARTH_RADIUS;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${server.url}")
    private String serverAddress;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;

    public double[] getCoordinates(String address) {
        String url = serverAddress + apiKey + "&text=" + address;
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode coordinates = jsonResponse.get("features").get(0).get("geometry").get("coordinates");

            double lon = coordinates.get(0).asDouble();
            double lat = coordinates.get(1).asDouble();

            return new double[]{lon, lat};
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
        double deltaLot = Math.toRadians(finishLon - startLon);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(finishLat)) * Math.sin(deltaLot / 2)
                        * Math.sin(deltaLot / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

}
