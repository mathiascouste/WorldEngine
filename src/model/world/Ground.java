package model.world;

public final class Ground {

    public static final int EARTH = 0;
    public static final int WATER = 1;
    public static final int ROCK = 2;
    public static final int GRASS = 3;
    public static final int SAND = 4;

    private Ground() {
    }

    public static int getElementsCount() {
        return 5;
    }

    public static double maxGroundWater(int g) {
        double maxGroundWater = 0;
        switch (g) {
        case EARTH:
            maxGroundWater = 1;
            break;
        case WATER:
            maxGroundWater = 0;
            break;
        case ROCK:
            maxGroundWater = 0.001;
            break;
        case GRASS:
            maxGroundWater = 0.9;
            break;
        case SAND:
            maxGroundWater = 1.3;
            break;
        default:
            maxGroundWater = 0;
        }
        return maxGroundWater;
    }
}
