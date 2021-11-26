package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.Regular;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

/**
 * Parking management.
 */
public class ParkingService {
    /**
     * Instancie Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingService");
    /**
     * Instancie FareCalculatorService.
     */
    private static final FareCalculatorService FARE_CALCULATOR_SERVICE
            = new FareCalculatorService();
    /**
     * Instancie InputReaderUtil.
     */
    private final InputReaderUtil inputReaderUtil;
    /**
     * Instancie ParkingSpotDAO.
     */
    private final ParkingSpotDAO parkingSpotDAO;
    /**
     * Instancie TicketDAO.
     */
    private final TicketDAO ticketDAO;

    /**
     * Parking management builder.
     * @param inputReaderUti Input reader.
     * @param parkingSpoDAO Parking management.
     * @param tickeDAO Ticket management.
     */
    public ParkingService(final InputReaderUtil inputReaderUti,
                          final ParkingSpotDAO parkingSpoDAO,
                          final TicketDAO tickeDAO) {
        this.inputReaderUtil = inputReaderUti;
        this.parkingSpotDAO = parkingSpoDAO;
        this.ticketDAO = tickeDAO;
    }

    /**
     * Drives a vehicle in of the parking lot.
     * @param dataBase for use checkRegular.
     */
    public void processIncomingVehicle(final DataBaseConfig dataBase) {
        try {
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                String vehicleRegNumber = getVehichleRegNumber();
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);
                //allot this parking space and mark it's availability as false
                LocalDateTime inTime = LocalDateTime.now();
                Ticket ticket = new Ticket();
                //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER,
                // PRICE, IN_TIME, OUT_TIME)
                //ticket.setId(ticketID);
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0);
                ticket.setInTime(inTime);
                ticket.setOutTime(null);
                ticketDAO.saveTicket(ticket);
                if (Regular.REGULAR_REDUCTION.equals(
                        ticketDAO.checkRegular(ticket, dataBase))) {
                    System.out.println("Welcome back! "
                            + "As a recurring user of our parking lot, "
                            + "you'll benefit from a 5% discount.");
                }
                System.out.println("Generated Ticket and saved in DB");
                System.out.println("Please park your vehicle in spot number:"
                        + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number:"
                        + vehicleRegNumber + " is:" + inTime);
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process incoming vehicle", e);
        }
    }

    /**
     * Request to enter the license plate of the user vehicle.
     * @return License plate entry reader.
     */
    private String getVehichleRegNumber() {
        System.out.println("Please type the vehicle registration number "
                + "and press enter key");
        return inputReaderUtil.readVehicleRegistrationNumber();
    }

    /**
     * The next available parking space.
     * @return The parking space available.
     */
    public ParkingSpot getNextParkingNumberIfAvailable() {
        int parkingNumber = 0;
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehichleType();
            parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);
            if (parkingNumber > 0) {
                parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);
            } else {
                throw new Exception("Error fetching parking number from DB. "
                        + "Parking slots might be full");
            }
        } catch (IllegalArgumentException ie) {
            LOGGER.error("Error parsing user input for type of vehicle", ie);
        } catch (Exception e) {
            LOGGER.error("Error fetching next available parking slot", e);
        }
        return parkingSpot;
    }

    /**
     * Enter the type of vehicle to park.
     * @return The type of vehicle.
     */
    private ParkingType getVehichleType() {
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch (input) {
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    /**
     * Drives a vehicle out of the parking lot.
     *  @param dataBase for use checkRegular.
     */
    public void processExitingVehicle(final DataBaseConfig dataBase) {
        try {
            String vehicleRegNumber = getVehichleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);
            LocalDateTime outTime = LocalDateTime.now();
            ticket.setOutTime(outTime);
            double regular = ticketDAO.checkRegular(ticket, dataBase);
            FARE_CALCULATOR_SERVICE.calculateFare(ticket, regular);
            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:"
                        + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:"
                        + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. "
                        + "Error occurred");
            }
        } catch (Exception e) {
            LOGGER.error("Unable to process exiting vehicle", e);
        }
    }
}
