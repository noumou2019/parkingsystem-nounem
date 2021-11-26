package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Parking management.
 */
public class ParkingSpotDAO {
    /**
     * Instancie Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("ParkingSpotDAO");
    /**
     * Instancie DataBaseConfig.
     */
    private DataBaseConfig dataBaseConfig = new DataBaseConfig();
    /**
     * Obtain the available parking space number.
     * @param parkingType Type of vehicle accommodated by the parking.
     * @return The number of available parking spaces.
     */
    public int getNextAvailableSlot(final ParkingType parkingType) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int result = -1;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
            ps.setString(1, parkingType.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt(1);
            }
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
        } catch (Exception ex) {
            LOGGER.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    /**
     * Updates the parking lot.
     * @param parkingSpot Parking space to update.
     * @return True if parkingSpot is update.
     */
    public boolean updateParking(final ParkingSpot parkingSpot) {
        //update the availability fo that parking slot
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            dataBaseConfig.closePreparedStatement(ps);
            return updateRowCount == 1;
        } catch (Exception ex) {
            LOGGER.error("Error updating parking info", ex);
            return false;
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
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
