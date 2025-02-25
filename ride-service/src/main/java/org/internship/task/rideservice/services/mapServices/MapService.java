package org.internship.task.rideservice.services.mapServices;

public interface MapService {

    double[] getCoordinates(String address);

    double getDistance(String startAddress, String finishAddress);

}
