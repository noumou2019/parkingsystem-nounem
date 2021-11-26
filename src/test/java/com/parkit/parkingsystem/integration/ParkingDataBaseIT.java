package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.Regular;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    private static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.setDataBaseConfig(dataBaseTestConfig);
        ticketDAO = new TicketDAO();
        ticketDAO.setDataBaseConfig(dataBaseTestConfig);
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    private static void tearDown(){

    }

    @Test
    public void testParkingACar() throws SQLException, ClassNotFoundException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(dataBaseTestConfig);
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT);
        ps.setString(1, String.valueOf(ticketDAO.getTicket("ABCDEF").getParkingSpot().getId()));
        ResultSet rs = ps.executeQuery();
        rs.next();
        LocalDateTime inTime = ticketDAO.getTicket("ABCDEF").getInTime();
        assertEquals("Ticket{id=1, parkingSpot=com.parkit.parkingsystem.model.ParkingSpot@1, vehicleRegNumber='ABCDEF', price=0.0, inTime=" + inTime + ", outTime=null}",ticketDAO.getTicket("ABCDEF").toString());
        assertFalse(rs.getBoolean("AVAILABLE"));
    }

    @Test
    public void testParkingABike() throws SQLException, ClassNotFoundException {
        when(inputReaderUtil.readSelection()).thenReturn(2);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("GHIJKL");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle(dataBaseTestConfig);
        Connection con = dataBaseTestConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.GET_PARKING_SPOT);
        ps.setString(1, String.valueOf(ticketDAO.getTicket("GHIJKL").getParkingSpot().getId()));
        ResultSet rs = ps.executeQuery();
        rs.next();
        assertFalse(rs.getBoolean("AVAILABLE"));
    }

    @Test
    public void testParkingLotExit() throws SQLException, ClassNotFoundException {
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticketDAO.updateTestTicket(ticketDAO.getTicket("ABCDEF"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        parkingService.processExitingVehicle(dataBaseTestConfig);
        LocalDateTime verif = LocalDateTime.now();
        assertEquals(0.75, ticketDAO.getTestTicket("ABCDEF").getPrice());
        assertEquals(verif.format(formatter), ticketDAO.getTestTicket("ABCDEF").getOutTime().format(formatter));
    }

    @Test
    public void testParkingLotExitBike() throws SQLException, ClassNotFoundException {
        testParkingABike();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        ticketDAO.updateTestTicket(ticketDAO.getTicket("GHIJKL"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        parkingService.processExitingVehicle(dataBaseTestConfig);
        LocalDateTime verif = LocalDateTime.now();
        assertEquals(verif.format(formatter), ticketDAO.getTestTicket("GHIJKL").getOutTime().format(formatter));
    }

    @Test
    public void testRegularCalculateFare() throws SQLException, ClassNotFoundException {
        testParkingLotExit();
        testParkingLotExit();
        testParkingLotExit();
        testParkingLotExit();
        Ticket ticket = ticketDAO.getTestTicket("ABCDEF");
        TicketDAO ticketDAO = new TicketDAO();
        double regular = ticketDAO.checkRegular(ticket, dataBaseTestConfig);
        assertEquals(0.71, ticket.getPrice());
        assertEquals(0.95, regular);
    }

    @Test
    public void testRegularCalculateFareBike() throws SQLException, ClassNotFoundException {
        testParkingLotExitBike();
        testParkingLotExitBike();
        testParkingLotExitBike();
        testParkingLotExitBike();
        Ticket ticket = ticketDAO.getTestTicket("GHIJKL");
        TicketDAO ticketDAO = new TicketDAO();
        double regular = ticketDAO.checkRegular(ticket, dataBaseTestConfig);
        boolean goodPrice = false;
        if (ticket.getPrice() == 0.47 || ticket.getPrice() == 0.48) {goodPrice = true;}
        assertTrue(goodPrice);
        assertEquals(0.95, regular);
    }

    @Test
    public void testFreeCalculateFare() throws SQLException, ClassNotFoundException {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("GHIJKL");
        testParkingABike();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle(dataBaseTestConfig);
        Ticket ticket = ticketDAO.getTestTicket("GHIJKL");
        assertEquals(0, ticket.getPrice());
    }
}
