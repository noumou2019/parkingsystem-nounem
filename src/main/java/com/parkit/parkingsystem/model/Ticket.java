package com.parkit.parkingsystem.model;

import java.time.LocalDateTime;

/**
 * Contains all the information of the vehicle and associated parking.
 */
public class Ticket {
    /**
     * The ticket id.
     */
    private int id;
    /**
     * The parking space.
     */
    private ParkingSpot parkingSpot;
    /**
     * The registration plate.
     */
    private String vehicleRegNumber;
    /**
     * The price of parking.
     */
    private double price;
    /**
     * The time of arrival.
     */
    private LocalDateTime inTime;
    /**
     * The moment of exit.
     */
    private LocalDateTime outTime;

    /**
     * See the ticket id.
     * @return The ticket id.
     */
    public int getId() {
        return id;
    }

    /**
     * Modifies the ticket id.
     * @param iD The ticket id.
     */
    public void setId(final int iD) {
        this.id = iD;
    }

    /**
     * See the parking space.
     * @return The parking space.
     */
    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    /**
     * Modifies the parking space.
     * @param parkingSpace The parking space.
     */
    public void setParkingSpot(final ParkingSpot parkingSpace) {
        this.parkingSpot = parkingSpace;
    }

    /**
     * See the registration plate.
     * @return The registration plate.
     */
    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    /**
     * Modifies the registration plate.
     * @param vehicleRegNumb The registration plate.
     */
    public void setVehicleRegNumber(final String vehicleRegNumb) {
        this.vehicleRegNumber = vehicleRegNumb;
    }

    /**
     * See the price of parking.
     * @return The price of parking.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Modifies the price of parking.
     * @param cost The price of parking.
     */
    public void setPrice(final double cost) {
        this.price = cost;
    }

    /**
     * See the time of arrival.
     * @return The time of arrival.
     */
    public LocalDateTime getInTime() {
        return inTime;
    }

    /**
     * Modifies the time of arrival.
     * @param arrivalTime The time of arrival.
     */
    public void setInTime(final LocalDateTime arrivalTime) {
        this.inTime = arrivalTime;
    }

    /**
     * See the moment of exit.
     * @return The moment of exit.
     */
    public LocalDateTime getOutTime() {
        return outTime;
    }

    /**
     * Modifies the moment of exit.
     * @param exitTime The moment of exit.
     */
    public void setOutTime(final LocalDateTime exitTime) {
        this.outTime = exitTime;
    }

    /**
     * Display the ticket in the console.
     * @return Ticket information in character string.
     */
    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", parkingSpot=" + parkingSpot
                + ", vehicleRegNumber='" + vehicleRegNumber + '\''
                + ", price=" + price
                + ", inTime=" + inTime
                + ", outTime=" + outTime
                + '}';
    }
}
