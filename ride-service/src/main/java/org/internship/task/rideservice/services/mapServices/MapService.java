package org.internship.task.rideservice.services.mapServices;

public interface MapService {
    public double[] getCoordinates(String address);
    public double getDistance(String startAddress, String finishAddress);
}
