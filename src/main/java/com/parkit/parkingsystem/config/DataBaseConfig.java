package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Management of the connection with the SQL database and of this information.
 */
public class DataBaseConfig {
    /**
     * Instancie Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("DataBaseConfig");

    /**
     * Used to connect to the SQL database.
     * @return the connection to the database.
     * @throws ClassNotFoundException Thrown when an application
     * tries to load in a class through its string name using:
     * The forName method in class Class but no definition
     * for the class with the specified name could be found.
     * @throws SQLException An exception that provides information
     * on a database access error or other errors.
     */
    public Connection getConnection()
            throws ClassNotFoundException, SQLException {

        Properties p = new Properties();
        Connection connection = null;
        InputStream is = null;
        try {
            is = this.getClass().getClassLoader()
                    .getResourceAsStream("dataConfig.properties");
            p.load(is);
            LOGGER.info("Create DB connection");
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(p.getProperty("url"),
                    p.getProperty("user"), p.getProperty("password"));
        } catch (RuntimeException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (RuntimeException | IOException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Used to disconnect to the SQL database.
     * @param con the connection to the database.
     */
    public void closeConnection(final Connection con) {
        if (con != null) {
            try {
                con.close();
                LOGGER.info("Closing DB connection");
            } catch (SQLException e) {
                LOGGER.error("Error while closing connection", e);
            }
        }
    }

    /**
     * Close the PreparedStatement with error handling.
     * @param ps PreparedStatement open.
     */
    public void closePreparedStatement(final PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                LOGGER.info("Closing Prepared Statement");
            } catch (SQLException e) {
                LOGGER.error("Error while closing prepared statement", e);
            }
        }
    }

    /**
     * Close the ResultSet with error handling.
     * @param rs ResultSet open.
     */
    public void closeResultSet(final ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                LOGGER.info("Closing Result Set");
            } catch (SQLException e) {
                LOGGER.error("Error while closing result set", e);
            }
        }
    }
}
