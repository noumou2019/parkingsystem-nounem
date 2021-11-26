package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingSpotTest {
    private final ParkingSpot parkingSpot = new ParkingSpot(4, ParkingType.CAR, true);

    @Test
    public void equalsTest() {
        assertTrue(parkingSpot.equals(parkingSpot));
        assertFalse(parkingSpot.equals(null));
    }
    @Test
    public void setIDTest() {
        parkingSpot.setId(3);
        assertEquals(3, parkingSpot.getId());
    }

    @Test
    public void setParkingTypeTest() {
        parkingSpot.setParkingType(ParkingType.BIKE);
        assertEquals(ParkingType.BIKE, parkingSpot.getParkingType());
    }

    @Test
    public void hashCodeTest() {
        if(parkingSpot.getId() != 4){
            parkingSpot.setId(4);
        }
        assertEquals(4, parkingSpot.hashCode());
    }
}
