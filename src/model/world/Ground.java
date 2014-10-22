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
        switch (g) {
        case EARTH:
            return 1;
        case WATER:
            return 0;
        case ROCK:
            return 0;
        case GRASS:
            return 0.9;
        case SAND:
            return 1.3;
        default:
            return 0;
        }
    }
}
