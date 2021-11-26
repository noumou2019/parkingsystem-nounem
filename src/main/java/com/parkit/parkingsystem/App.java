package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * App class containing the main method to start the application.
 */
public final class App {
    private App() { }

    /**
     * Instancie Logger.
     */
    private static final Logger LOGGER = LogManager.getLogger("App");

    /**
     * Main method initializing the application.
     * @param args Tableau de chaînes servant à executer le programme.
     */
    public static void main(final String[] args) {
        LOGGER.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
