package measure;

public class ThreadsEfficiency {
    private static double efficiency = 0;
    private static int cpt = 0;

    private ThreadsEfficiency() {
    }

    public static long measureNextSleep(long delay, long elapsedTime) {
        long diff = delay - elapsedTime;
        if (diff > 0) {
            double eff = elapsedTime / delay;
            updateEfficiency(eff);
        } else {
            updateEfficiency(1);
            diff = 0;
        }
        return diff;
    }

    private static void updateEfficiency(double i) {
        if (cpt != 0) {
            efficiency = (efficiency * (cpt) + i) / (++cpt);
        } else {
            cpt++;
            efficiency = i;
        }
    }

    public static double getEfficiency() {
        return efficiency;
    }
}
