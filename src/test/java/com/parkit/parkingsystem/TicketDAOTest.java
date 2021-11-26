package com.parkit.parkingsystem;

import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TicketDAOTest {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static TicketDAO ticketDAO;
    private static Ticket ticket;
    private final FareCalculatorService fcs = new FareCalculatorService();
    @BeforeAll
   private static void setUp() {
       ticketDAO = new TicketDAO();
       ticketDAO.setDataBaseConfig(dataBaseTestConfig);
       ticket = new Ticket();
   }
    @BeforeEach
    public void setUpPerTest() {
        ticket = ticketDAO.getTicket("ABCDEF");
    }

    @Test
    public void getTicketTest() {
        ticket = ticketDAO.getTestTicket("ABCDEF");
        assertNotNull(ticket);
    }
    @Test
    public void checkRegularTest() {
       double regular = ticketDAO.checkRegular(ticket, dataBaseTestConfig);
       assertEquals(1, (int)regular);
    }

    @Test
    public void saveTicketTest() {
       boolean reussite = ticketDAO.saveTicket(ticket);
       assertTrue(reussite);
    }

    @Test
    public void updateTicketTest() {
        LocalDateTime outTime = LocalDateTime.now().plusHours(1);
        ticket.setOutTime(outTime);
        double regular = ticketDAO.checkRegular(ticket, dataBaseTestConfig);
        fcs.calculateFare(ticket, regular);
        boolean reussite = ticketDAO.updateTicket(ticket);
        assertTrue(reussite);
    }
    @Test
    public void updateTestTicketTest() {
        LocalDateTime outTime = LocalDateTime.now().plusHours(1);
        ticket.setOutTime(outTime);
        double regular = ticketDAO.checkRegular(ticket, dataBaseTestConfig);
        fcs.calculateFare(ticket, regular);
        boolean reussite = ticketDAO.updateTestTicket(ticket);
        assertTrue(reussite);
    }
}
