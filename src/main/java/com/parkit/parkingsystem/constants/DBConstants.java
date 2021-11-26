package com.parkit.parkingsystem.constants;

/**
 * Contains the queries for the SQL database.
 */
public final class DBConstants {
    private DBConstants() { }
    /**
     * Selects the lowest available parking number.
     */
    public static final String GET_NEXT_PARKING_SPOT
            = "select min(PARKING_NUMBER) from parking "
            + "where AVAILABLE = true and TYPE = ?";
    /**
     * Updates the selected parking space.
     */
    public static final String UPDATE_PARKING_SPOT
            = "update parking set available = ? where PARKING_NUMBER = ?";
    /**
     * Selects the available parking.
     */
    public static final String GET_PARKING_SPOT
            = "select AVAILABLE from parking where PARKING_NUMBER = ?";
    /**
     * Save a vehicle ticket.
     */
    public static final String SAVE_TICKET
            = "insert into ticket(PARKING_NUMBER, VEHICLE_REG_NUMBER, "
            + "PRICE, IN_TIME, OUT_TIME) values(?,?,?,?,?)";
    /**
     * Update a vehicle ticket.
     */
    public static final String UPDATE_TICKET
            = "update ticket set PRICE=?, OUT_TIME=? where ID=?";
    /**
     * SELECT * FROM test.ticket where ID = (SELECT MAX(ID) FROM test.ticket).
     */
    public static final String GET_TICKET_TEST
            = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, "
            + "t.OUT_TIME, p.TYPE from ticket t,parking p "
            + "where t.ID = (SELECT MAX(ID) FROM ticket) "
            + "and p.parking_number = t.parking_number "
            + "and t.VEHICLE_REG_NUMBER=? order by t.IN_TIME  limit 1";
    /**
     * Pick up the ticket from an outgoing vehicle.
     */
    public static final String GET_TICKET_EXIT
            = "select t.PARKING_NUMBER, t.ID, t.PRICE, t.IN_TIME, "
            + "t.OUT_TIME, p.TYPE from ticket t,parking p "
            + "where p.parking_number = t.parking_number "
            + "and t.VEHICLE_REG_NUMBER=? "
            + "and t.OUT_TIME is null order by t.IN_TIME  limit 1";
    /**
     * Checks if a user is regulated.
     */
    public static final String CHECK_REGULARITY
            = "select count(*) as REGULAR from ticket "
            + "where VEHICLE_REG_NUMBER = ? and OUT_TIME > ?";
    /**
     *  Modify the ticket entered for the test.
     */
    public static final String UPDATE_TEST_TICKET
            = "update ticket set IN_TIME=? where ID=?";
}
