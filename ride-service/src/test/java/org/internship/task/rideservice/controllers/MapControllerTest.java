package org.internship.task.rideservice.controllers;

import org.internship.task.rideservice.services.mapServices.MapServiceImpl;
import org.internship.task.rideservice.util.TestDataForMapTests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapControllerTest {
    @Mock
    private MapServiceImpl mapService;

    @InjectMocks
    private MapController mapController;


    @Test
    void getCoordinates_ShouldReturnCoordinates_WhenAddressIsValid() {
        when(mapService.getCoordinates(TestDataForMapTests.getSTART_ADDRESS())).thenReturn(
                TestDataForMapTests.getSTART_COORDINATES());

        double[] actualCoordinates = mapController.getCoordinates(TestDataForMapTests.getSTART_ADDRESS());

        assertNotNull(actualCoordinates);
        assertEquals(TestDataForMapTests.getSTART_COORDINATES().length, actualCoordinates.length);
        assertArrayEquals(TestDataForMapTests.getSTART_COORDINATES(), actualCoordinates);
    }

    @Test
    void getDistance_ShouldReturnDistance_WhenStartAndFinishAreValid() {
        when(mapService.getDistance(TestDataForMapTests.getSTART_ADDRESS(), TestDataForMapTests.getFINISH_ADDRESS()))
                .thenReturn(TestDataForMapTests.getDISTANCE_BETWEEN_START_AND_FINISH());

        double actualDistance =
                mapController.getDistance(TestDataForMapTests.getSTART_ADDRESS(), TestDataForMapTests.getFINISH_ADDRESS());

        assertEquals(TestDataForMapTests.getDISTANCE_BETWEEN_START_AND_FINISH(), actualDistance);
    }
}