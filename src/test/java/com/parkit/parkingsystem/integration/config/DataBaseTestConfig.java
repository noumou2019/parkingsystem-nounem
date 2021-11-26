package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.Index;
import com.parkit.parkingsystem.constants.Regular;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 *  Management of the connection with the SQL database and of this information, for test
 */
public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger LOGGER = LogManager.getLogger("DataBaseTestConfig");

    /**
     *  Used to connect to the SQL database
     * @return the connection to the database
     * @throws ClassNotFoundException Thrown when an application tries to load in a class through its string name using:
     * The forName method in class Class but no definition for the class with the specified name could be found.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        LOGGER.info("Create DB connection");
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:8889/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&autoReconnect=true&useSSL=false","root","root");

    }

    /**
     * Used to disconnect to the SQL database
     * @param con the connection to the database
     */
    public void closeConnection(Connection con){
        if(con!=null){
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection",e);
            }
        }
    }

    /**
     * Close the PreparedStatement with error handling
     * @param ps PreparedStatement open
     */
    public void closePreparedStatement(PreparedStatement ps) {
        if(ps!=null){
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement",e);
            }
        }
    }

    /**
     * Close the ResultSet with error handling
     * @param rs ResultSet open
     */
    public void closeResultSet(ResultSet rs) {
        if(rs!=null){
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set",e);
            }
        }
    }
    /**
     * Checks the regularity of a user's vehicle.
     * @param ticket Ticket of the vehicle concerned.
     * @return The multiplier used to apply the reduction.
     */
    public double checkRegularTest(final Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
        try {
            con = dataBaseTestConfig.getConnection();
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
            dataBaseTestConfig.closeResultSet(rs);
            dataBaseTestConfig.closePreparedStatement(ps);
            dataBaseTestConfig.closeConnection(con);
        }
        return 1;
    }
}
