package com.parkit.parkingsystem;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataBaseConfigTest {
    private final DataBaseConfig dataBaseConfig = new DataBaseConfig();
    @Test
    public void getConnectionTest() {
        Connection con;
        boolean succes = false;
        try {
            con = dataBaseConfig.getConnection();
            if(con != null){succes = true;}
            dataBaseConfig.closeConnection(con);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        assertTrue(succes);
    }
}
