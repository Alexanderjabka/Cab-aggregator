package org.internship.task.rideservice.services.mapServices;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class MapServiceImpl implements MapService{
    private static final String API_KEY = "5b3ce3597851110001cf6248102854531d824446b2e1ea2cbbad9f9d";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public double[] getCoordinates(String address) {
        String url = "https://api.openrouteservice.org/geocode/search?api_key=" + API_KEY + "&text=" + address;
        String response = restTemplate.getForObject(url, String.class);

        try {
            JsonNode jsonResponse = objectMapper.readTree(response);
            JsonNode coordinates = jsonResponse.get("features").get(0).get("geometry").get("coordinates");

            double lon = coordinates.get(0).asDouble();
            double lat = coordinates.get(1).asDouble();

            return new double[]{lon, lat};
        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения координат: " + e.getMessage());
        }
    }

    @Override
    public double getDistance(String startAddress, String finishAddress) {
        double[] startCoord = getCoordinates(startAddress);
        double[] finishCoord = getCoordinates(finishAddress);

        double startLat = startCoord[1]; // Широта начального адреса
        double startLon = startCoord[0]; // Долгота начального адреса
        double finishLat = finishCoord[1]; // Широта конечного адреса
        double finishLon = finishCoord[0]; // Долгота конечного адреса

        double dLat = Math.toRadians(finishLat - startLat);
        double dLon = Math.toRadians(finishLon - startLon);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(finishLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        final double EARTH_RADIUS = 6371; // Радиус Земли в км

        return EARTH_RADIUS * c; // Расстояние в километрах
    }
}
