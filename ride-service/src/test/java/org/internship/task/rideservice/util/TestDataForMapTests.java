package org.internship.task.rideservice.util;

import lombok.Getter;

public class TestDataForMapTests {
    private final double[] startCoordinates;
    private final double[] finishCoordinates;
    @Getter
    private final String startAddress;
    @Getter
    private final String finishAddress;
    @Getter
    private final double distanceBetweenStartAndFinish;

    private TestDataForMapTests() {
        this.startCoordinates = new double[] {27.56667, 53.9};
        this.finishCoordinates = new double[] {30.283083, 53.769428};
        this.startAddress = "Minsk";
        this.finishAddress = "Mogilev";
        this.distanceBetweenStartAndFinish = 178.82476685810292;
    }

    public static TestDataForMapTests create() {
        return new TestDataForMapTests();
    }

    public double[] getStartCoordinates() {
        return startCoordinates.clone();
    }

    public double[] getFinishCoordinates() {
        return finishCoordinates.clone();
    }

}

