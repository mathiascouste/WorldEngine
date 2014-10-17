package model.world.generator;

import java.util.ArrayList;
import java.util.List;

public class Source {
    private static List<Source> sources = new ArrayList<Source>();
    private int x, y, z;

    public Source(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        sources.add(this);
    }
    
    public static Source getSource(int x, int y, int z) {
        for(Source s : sources) {
            if(s.isHere(x, y, z)) {
                return s;
            }
        }
        return null;
    }

    public boolean isHere(int x, int y, int z) {
        return this.x == x && this.y == y && this.z == z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
