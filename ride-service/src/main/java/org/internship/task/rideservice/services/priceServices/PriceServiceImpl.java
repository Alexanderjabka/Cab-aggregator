package org.internship.task.rideservice.services.priceServices;

import static org.internship.task.rideservice.util.constants.RideConstants.PRICE_PER_KILOMETER;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceServiceImpl {

    public static BigDecimal setPriceForTheRide(double distanceInKm) {
        BigDecimal distance = BigDecimal.valueOf(distanceInKm);
        BigDecimal tripCost = distance.multiply(PRICE_PER_KILOMETER);

        tripCost = tripCost.setScale(2, RoundingMode.HALF_UP);

        return tripCost;
    }

}
