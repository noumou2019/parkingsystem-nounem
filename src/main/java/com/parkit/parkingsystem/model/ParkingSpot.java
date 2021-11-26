package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;

/**
 * Parking information.
 */
public class ParkingSpot {
    /**
     * Place number attribute.
     */
    private int number;
    /**
     * Place type attribute.
     */
    private ParkingType parkingType;
    /**
     * Attribute of the availability of the place.
     */
    private boolean isAvailable;

    /**
     * Parking information builder.
     * @param numb Parking number.
     * @param parkingTyp Car or bike parking.
     * @param isAvailabl Parking availability.
     */
    public ParkingSpot(final int numb,
                       final ParkingType parkingTyp, final boolean isAvailabl) {
        this.number = numb;
        this.parkingType = parkingTyp;
        this.isAvailable = isAvailabl;
    }

    /**
     * See the parking number.
     * @return Parking number.
     */
    public int getId() {
        return number;
    }

    /**
     * Modifies the parking number.
     * @param numb New Parking number.
     */
    public void setId(final int numb) {
        this.number = numb;
    }

    /**
     * See the type of parking.
     * @return type of parking.
     */
    public ParkingType getParkingType() {
        return parkingType;
    }

    /**
     * Modifies type of parking.
     * @param parkingTyp New type of parking.
     */
    public void setParkingType(final ParkingType parkingTyp) {
        this.parkingType = parkingTyp;
    }

    /**
     * See the availability of the place.
     * @return the availability of the place.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Modifies the availability of the place.
     * @param availabl new availability of the place.
     */
    public void setAvailable(final boolean availabl) {
        isAvailable = availabl;
    }

    /**
     * Checks if two objects are equal.
     * @param o Object compare with the one called.
     * @return True if equal, false otherwise.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParkingSpot that = (ParkingSpot) o;
        return number == that.number;
    }

    /**
     * Returns the number.
     * @return number.
     */
    @Override
    public int hashCode() {
        return number;
    }
}
