package view.tools;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WELogger {
    private static final Logger LOGGER = Logger.getLogger(WELogger.class
            .getName());

    private WELogger() {
    }

    public static void log(String log) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info(log);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void log(IOException e) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info(e.getMessage());
    }

    public static void log(InterruptedException e) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info(e.getMessage());
    }
}