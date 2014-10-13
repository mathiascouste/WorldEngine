package view.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WELogger {
    private static final Logger LOGGER = Logger.getLogger(WELogger.class
            .getName());

    public static void log(String log) {
        LOGGER.setLevel(Level.INFO);
        LOGGER.info(log);
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    private WELogger() {
    }
}