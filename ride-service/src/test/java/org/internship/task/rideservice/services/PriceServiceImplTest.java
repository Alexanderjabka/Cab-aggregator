package org.internship.task.rideservice.services;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.internship.task.rideservice.services.priceServices.PriceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PriceServiceImplTest {

    @Test
    void setPriceForTheRide_ShouldReturnCorrectPriceForPositiveDistance() {
        double distanceInKm = 10.0;
        BigDecimal expectedPrice = new BigDecimal("11.00");

        BigDecimal actualPrice = PriceServiceImpl.setPriceForTheRide(distanceInKm);

        assertEquals(expectedPrice, actualPrice);
    }
    @Test
    void setPriceForTheRide_ShouldReturnZeroForZeroDistance() {
        double distanceInKm = 0.0;
        BigDecimal expectedPrice = new BigDecimal("0.00");

        BigDecimal actualPrice = PriceServiceImpl.setPriceForTheRide(distanceInKm);

        assertEquals(expectedPrice, actualPrice);
    }

}
