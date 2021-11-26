package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParkingSpotDAOTest {

        private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
        private static ParkingSpotDAO parkingSpotDAO;

    @BeforeAll
        private static void setUp() {
            parkingSpotDAO = new ParkingSpotDAO();
            parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
    }

        @Test
        public void getNextAvailableSlotTest() {
            ParkingType parkingType = ParkingType.CAR;
            int libre = parkingSpotDAO.getNextAvailableSlot(parkingType);
            assertEquals(2, libre);
        }

        @Test
        public void updateParkingTest() {
            ParkingSpot parkingSpot = new ParkingSpot(2, ParkingType.CAR, true);
            boolean prise = parkingSpotDAO.updateParking(parkingSpot);
            assertTrue(prise);
        }
}
