package config;

public class HydroConfig {

    // Hydro
    public static final int SEALEVEL = 50;
    public static final int SOURCEABONDANCE = 2;

    // Evaporation
    public static final double SURFACE_EVAPORATION_COEF = 0.25;
    public static final double SOL_EVAPORATION_COEF = 0.05;
    public static final double EVAPORATION_MAX = 0.001;
    public static final double RAIN_PER_LOOP = 0.02;
    public static final double ECOULEMENT_COEF = 0.02;

    private HydroConfig() {
    }
}