package org.internship.task.rideservice.controllers;

import static org.internship.task.rideservice.util.TestDataForMapTests.create;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import org.internship.task.rideservice.services.mapServices.MapServiceImpl;
import org.internship.task.rideservice.util.TestDataForMapTests;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MapControllerTest {
    @Mock
    private MapServiceImpl mapService;

    @InjectMocks
    private MapController mapController;

    private TestDataForMapTests testData;

    @BeforeEach
    void setUp() {
        testData = create();
    }

    @Test
    void getCoordinates_ShouldReturnCoordinates_WhenAddressIsValid() {
        when(mapService.getCoordinates(testData.getStartAddress())).thenReturn(testData.getStartCoordinates());

        double[] actualCoordinates = mapController.getCoordinates(testData.getStartAddress());

        assertNotNull(actualCoordinates);
        assertEquals(testData.getStartCoordinates().length, actualCoordinates.length);
        assertArrayEquals(testData.getStartCoordinates(), actualCoordinates);
    }

    @Test
    void getDistance_ShouldReturnDistance_WhenStartAndFinishAreValid() {
        when(mapService.getDistance(testData.getStartAddress(), testData.getFinishAddress()))
            .thenReturn(testData.getDistanceBetweenStartAndFinish());

        double actualDistance = mapController.getDistance(testData.getStartAddress(), testData.getFinishAddress());

        assertEquals(testData.getDistanceBetweenStartAndFinish(), actualDistance);
    }
}