package com.parkit.parkingsystem;

import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    @Test
    public void toStringTest() {
        Ticket ticket = new Ticket();
        LocalDateTime intime = LocalDateTime.now().minusHours(1);
        LocalDateTime outtime = LocalDateTime.now();
        ticket.setInTime(intime);
        ticket.setOutTime(outtime);
        ticket.setId(4);
        ticket.setVehicleRegNumber("TEST");
        assertEquals(ticket.toString(),
                "Ticket{id=4, parkingSpot=null, vehicleRegNumber='TEST', price=0.0, inTime="
                        + intime + ", outTime=" + outtime + "}");
    }
}
