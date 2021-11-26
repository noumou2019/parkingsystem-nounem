package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.Index;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.constants.Regular;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

/**
 * Ticket management.
 */
public class TicketDAO {
    /**
     * Instancie Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("TicketDAO");
    /**
     * Instancie DataBaseConfig.
     */
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * Save the ticket in the database.
     * @param ticket ticket to save.
     * @return True if the ticket is saved.
     */
    public boolean saveTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            //ps.setInt(1,ticket.getId());
            ps.setInt(Index.ONE, ticket.getParkingSpot().getId());
            ps.setString(Index.TWO, ticket.getVehicleRegNumber());
            ps.setDouble(Index.THREE, ticket.getPrice());
            ps.setTimestamp(Index.FOUR, Timestamp.valueOf(ticket.getInTime()));
            ps.setTimestamp(Index.FIVE, (ticket.getOutTime() == null)
                    ? null : Timestamp.valueOf(ticket.getOutTime()));
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Collect the ticket linked to the license plate.
     * @param vehicleRegNumber Registration number
     *                         of the vehicle to be recovered.
     * @return Vehicle ticket requested.
     */
    public Ticket getTicket(final String vehicleRegNumber) {
        Ticket ticket
                = getGetTicket(vehicleRegNumber, DBConstants.GET_TICKET_EXIT);
        return ticket;
    }

    /**
     * Collect the ticket linked to the license plate for test.
     * @param vehicleRegNumber Registration number
     *                         of the vehicle to be recovered.
     * @return Vehicle ticket requested.
     */
    public Ticket getTestTicket(final String vehicleRegNumber) {
        Ticket ticket
                = getGetTicket(vehicleRegNumber, DBConstants.GET_TICKET_TEST);
        return ticket;
    }

    /**
     * Collect the ticket for other method.
     * @param vehicleRegNumber Registration number
     *      *                   of the vehicle to be recovered.
     * @param getTicket Command for SQL server.
     * @return Vehicle ticket requested.
     */
    private Ticket getGetTicket(
            final String vehicleRegNumber, final String getTicket) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ticket ticket = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(getTicket);
            //ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
            ps.setString(Index.ONE, vehicleRegNumber);
            rs = ps.executeQuery();
            if (rs.next()) {
                ticket = new Ticket();
                ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(Index.ONE),
                        ParkingType.valueOf(rs.getString(Index.SIX)), false);
                ticket.setParkingSpot(parkingSpot);
                ticket.setId(rs.getInt(Index.TWO));
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(rs.getDouble(Index.THREE));
                ticket.setInTime(rs.getTimestamp(Index.FOUR).toLocalDateTime());
                ticket.setOutTime(rs.getTimestamp(Index.FIVE) == null
                        ? null : rs.getTimestamp(Index.FIVE).toLocalDateTime());
            }
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return ticket;
    }
    /**
     * Updates the requested ticket.
     * @param ticket Ticket to modify.
     * @return True if ticket changed.
     */
    public boolean updateTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(Index.ONE, ticket.getPrice());
            ps.setTimestamp(Index.TWO, Timestamp.valueOf(ticket.getOutTime()));
            ps.setInt(Index.THREE, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }
    /**
     * Updates the requested ticket.
     * @param ticket Ticket to modify.
     * @return True if ticket changed.
     */
    public boolean updateTestTicket(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_TEST_TICKET);
            ps.setTimestamp(Index.ONE,
                    Timestamp.valueOf(ticket.getInTime().minusHours(1)));
            ps.setInt(Index.TWO, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            LOGGER.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    /**
     * Checks the regularity of a user's vehicle for test.
     * @param ticket Ticket of the vehicle concerned.
     * @param dataBaseTConfig DataBase concerned.
     * @return The multiplier used to apply the reduction.
     */
    public double checkRegular(final Ticket ticket,
                               final DataBaseConfig dataBaseTConfig) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = dataBaseTConfig.getConnection();
            ps = con.prepareStatement(DBConstants.CHECK_REGULARITY);
            ps.setString(Index.ONE, ticket.getVehicleRegNumber());
            ps.setTimestamp(Index.TWO, Timestamp.valueOf(ticket.getInTime()
                    .minusMonths(Regular.MONTH_FOR_REDUCTION)));
            rs = ps.executeQuery();
            if (rs != null) {
                rs.next();
            }
            int regular = rs.getInt("REGULAR");
            if (regular >= Regular.MINIMUM_REGULAR) {
                return Regular.REGULAR_REDUCTION;
            } else {
                return 1;
            }
        } catch (Exception ex) {
            LOGGER.error("Error regular check info", ex);
        } finally {
            dataBaseTConfig.closeResultSet(rs);
            dataBaseTConfig.closePreparedStatement(ps);
            dataBaseTConfig.closeConnection(con);
        }
        return 1;
    }
    /**
     * Lets see dataBaseConfig.
     * @return dataBaseConfig.
     */
    public DataBaseConfig getDataBaseConfig() {
        return dataBaseConfig;
    }

    /**
     * Modifies dataBaseConfig.
     * @param dataBaseConf is the new configuration.
     */
    public void setDataBaseConfig(final DataBaseConfig dataBaseConf) {
        this.dataBaseConfig = dataBaseConf;
    }
}
