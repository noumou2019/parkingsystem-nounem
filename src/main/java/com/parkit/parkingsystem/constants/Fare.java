package com.parkit.parkingsystem.constants;

/**
 * Contains pricing.
 */
public final class Fare {
    private Fare() { }
    /**
     * Price for bikes.
     */
    public static final double BIKE_RATE_PER_HOUR = 1.0;
    /**
     * Price for cars.
     */
    public static final double CAR_RATE_PER_HOUR = 1.5;
    /**
     * Duration of gratuity.
     */
    public static final double FREE_TIME = 0.5; // 30 minutes gratuites
    /**
     * Round the price to 2 decimals.
     */
    public static final double ROUND = 100.0;
    /**
     * Turn seconds into hours.
     */
    public static final int SECONDS_INTO_HOURS = 3600;
}
