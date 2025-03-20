package org.internship.task.rideservice.dto.clientsdDto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class GeocodeResponse {
    private List<Feature> features;

    @Getter
    @Setter
    public static class Feature {
        private Geometry geometry;
    }

    @Getter
    @Setter
    public static class Geometry {
        private List<Double> coordinates;
    }
}
