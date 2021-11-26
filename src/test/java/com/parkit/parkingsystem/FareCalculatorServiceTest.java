package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.Regular;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FareCalculatorServiceTest {

    private static FareCalculatorService fareCalculatorService;
    private Ticket ticket = new Ticket();
    private double regular = 1;

    @BeforeAll
    private static void setUp() {
        fareCalculatorService = new FareCalculatorService();
    }

    @BeforeEach
    private void setUpPerTest() {
        ticket = new Ticket();
    }

    @Test
    public void calculateFareCar(){
        LocalDateTime inTime = LocalDateTime.now().minusHours(1); // 1 heure de moins, car
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals(ticket.getPrice(), Math.round((Fare.CAR_RATE_PER_HOUR * (1 * Fare.FREE_TIME))*100.0)/100.0);
    }

    @Test
    public void calculateFareBike(){
        LocalDateTime inTime = LocalDateTime.now().minusHours(1); // 1 heure de moins, bike
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);

        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals(ticket.getPrice(), Math.round((Fare.BIKE_RATE_PER_HOUR * (1 * Fare.FREE_TIME))*100.0)/100.0);
    }

    @Test
    public void calculateFareUnkownType(){
        LocalDateTime inTime = LocalDateTime.now().minusHours(1); // 1 heure de moins, unkown
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, null,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket, regular));
    }

    @Test
    public void calculateFareBikeWithFutureInTime(){
        LocalDateTime inTime = LocalDateTime.now().plusHours(1); // 1 heure de plus, bike
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket, regular));
    }

    @Test
    public void calculateFareBikeWithLessThanOneHourParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45); // 45 minutes de moins, bike
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals(Math.round((((0.75 - Fare.FREE_TIME) * Fare.BIKE_RATE_PER_HOUR))*100.0)/100.0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessThanOneHourParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(45); // 45 minutes de moins, car
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals(Math.round((((0.75 - Fare.FREE_TIME) * Fare.CAR_RATE_PER_HOUR))*100.0)/100.0, ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithLessFreeTimeParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(15); // FreeTime défini à 30 minutes
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals((0 * Fare.CAR_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithFreeTimeParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusMinutes(30); // FreeTime défini à 30 minutes
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals( (0 * Fare.BIKE_RATE_PER_HOUR) , ticket.getPrice());
    }

    @Test
    public void calculateFareCarWithMoreThanADayParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusDays(1); // 1 jour de moins
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals( Math.round(((24 - Fare.FREE_TIME) * Fare.CAR_RATE_PER_HOUR)*100.0)/100.0 , ticket.getPrice());
    }

    @Test
    public void calculateFareBikeWithMoreThanADayParkingTime(){
        LocalDateTime inTime = LocalDateTime.now().minusDays(1); // 1 jour de moins
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals( Math.round(((24 - Fare.FREE_TIME) * Fare.BIKE_RATE_PER_HOUR)*100.0)/100.0 , ticket.getPrice());
    }

    @Test
    public void calculateRegularFareCarWithMoreThanADayParkingTime(){
        regular = Regular.REGULAR_REDUCTION;
        LocalDateTime inTime = LocalDateTime.now().minusDays(1); // 1 jour de moins
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals( Math.round((((24 - Fare.FREE_TIME) * Fare.CAR_RATE_PER_HOUR)*Regular.REGULAR_REDUCTION)*100.0)/100.0 , ticket.getPrice());
    }

    @Test
    public void calculateRegularFareBikeWithMoreThanADayParkingTime(){
        regular = Regular.REGULAR_REDUCTION;
        LocalDateTime inTime = LocalDateTime.now().minusDays(1); // 1 jour de moins
        LocalDateTime outTime = LocalDateTime.now();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setInTime(inTime);
        ticket.setOutTime(outTime);
        ticket.setParkingSpot(parkingSpot);
        fareCalculatorService.calculateFare(ticket, regular);
        assertEquals( Math.round((((24 - Fare.FREE_TIME) * Fare.BIKE_RATE_PER_HOUR)*Regular.REGULAR_REDUCTION)*100.0)/100.0 , ticket.getPrice());
    }
}
