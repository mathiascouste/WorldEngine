package model.world.generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.GenConfig;

public class HydroGenerator {
    private Calque relief;
    private Calque hydro;
    private Calque rain;
    private Random rand;
    private int taille;
    private List<Source> activeSources;
    private List<Source> passiveSources;

    public HydroGenerator(Calque relief, Random rand) {
        this.rand = rand;
        this.relief = relief;
        this.taille = this.relief.getTaille();
        this.hydro = new Calque(this.taille, 0);
        this.rain = new Calque(this.taille, 0);
        this.activeSources = new ArrayList<Source>();
        this.setPassiveSources(new ArrayList<Source>());
    }

    public Calque generateHydro() {
        this.placeOcean();

        this.placeSources();

        this.startFlow();
        this.startRain();

        return this.hydro;
    }

    private void placeOcean() {
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                this.hydro.getV()[i][j] = -1;

                if (this.relief.getV()[i][j] <= GenConfig.SEALEVEL) {
                    this.hydro.getV()[i][j] = GenConfig.SEALEVEL;
                } else {
                    this.hydro.getV()[i][j] = -1;
                }

            }
        }
    }

    private void placeSources() {
        int nSource = GenConfig.SOURCEABONDANCE;
        int x, y;
        while (nSource != 0) {
            x = rand.nextInt(this.taille);
            y = rand.nextInt(this.taille);
            if (this.hydro.getV()[x][y] == -1) {
                nSource--;
                this.activeSources.add(new Source(x, y,
                        this.relief.getV()[x][y]));
            }
        }
    }

    private void startRain() {
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                this.rain.getV()[i][j] = 10;
            }
        }
        for (int k = GenConfig.MAX_HEIGHT - 1; k > GenConfig.SEALEVEL; k--) {
            for (int x = 0; x < this.taille; x++) {
                for (int y = 0; y < this.taille; y++) {
                    if (this.relief.getV()[x][y] == k) {
                        Point north = new Point(x, y + 1);
                        Point south = new Point(x, y - 1);
                        Point east = new Point(x + 1, y);
                        Point west = new Point(x - 1, y - 1);
                        List<Point> pts = new ArrayList<Point>();
                        if (getAltitude(north) < k) {
                            pts.add(north);
                        }
                        if (getAltitude(south) < k) {
                            pts.add(south);
                        }
                        if (getAltitude(east) < k) {
                            pts.add(east);
                        }
                        if (getAltitude(west) < k) {
                            pts.add(west);
                        }
                        int maxDiff = 1;
                        for (Point p : pts) {
                            maxDiff += k - getAltitude(p);
                        }
                        for (Point p : pts) {
                            this.rain.getV()[p.x][p.y] += this.rain.getV()[x][y]
                                    / maxDiff;
                        }
                    }
                }
            }
        }
    }

    private void startFlow() {
        Source s = null;
        while (!this.activeSources.isEmpty()) {
            s = this.activeSources.remove(this.activeSources.size() - 1);

            Point north = new Point(s.getX(), s.getY() + 1), south = new Point(
                    s.getX(), s.getY() - 1), east = new Point(s.getX() + 1,
                    s.getY()), west = new Point(s.getX() - 1, s.getY());
            Point up = new Point(s.getX(), s.getY()), down = new Point(
                    s.getX(), s.getY());
            int filledSide = 0;

            if (this.relief.getV()[s.getX()][s.getY()] < s.getZ() - 1
                    && Source.getSource(down.x, down.y, s.getZ() - 1) == null) {
                if (getAltitude(down) >= GenConfig.SEALEVEL) {
                    this.activeSources.add(new Source(s.getX(), s.getY(), s
                            .getZ() - 1));
                }
            } else {
                filledSide = 0;
                if (isInWorld(north) && s.getZ() >= getAltitude(north)
                        && Source.getSource(north.x, north.y, s.getZ()) == null) {
                    this.activeSources.add(new Source(north.x, north.y, s
                            .getZ()));
                    this.hydro.getV()[north.x][north.y] = s.getZ();
                } else {
                    filledSide++;
                }

                if (isInWorld(south) && s.getZ() >= getAltitude(south)
                        && Source.getSource(south.x, south.y, s.getZ()) == null) {
                    this.activeSources.add(new Source(south.x, south.y, s
                            .getZ()));
                    this.hydro.getV()[south.x][south.y] = s.getZ();
                } else {
                    filledSide++;
                }

                if (isInWorld(east) && s.getZ() >= getAltitude(east)
                        && Source.getSource(east.x, east.y, s.getZ()) == null) {
                    this.activeSources
                            .add(new Source(east.x, east.y, s.getZ()));
                    this.hydro.getV()[east.x][east.y] = s.getZ();
                } else {
                    filledSide++;
                }

                if (isInWorld(west) && s.getZ() >= getAltitude(west)
                        && Source.getSource(west.x, west.y, s.getZ()) == null) {
                    this.activeSources
                            .add(new Source(west.x, west.y, s.getZ()));
                    this.hydro.getV()[west.x][west.y] = s.getZ();
                } else {
                    filledSide++;
                }

                // Code correspondant au remplissement d'un bassin
                if (filledSide == 4) {
                    if (sourceExists(up, getAltitude(up) + 1)) {
                        this.activateSource(up.x, up.y, getAltitude(up) + 1);
                    } else {
                        this.activeSources.add(new Source(up.x, up.y,
                                getAltitude(up) + 1));
                        this.hydro.getV()[up.x][up.y] = getAltitude(up) + 1;
                    }
                }

            }
            this.passiveSources.add(s);
        }
        for (Source p : this.passiveSources) {
            this.hydro.getV()[p.getX()][p.getY()] = p.getZ();
        }
    }

    private void activateSource(int x, int y, int z) {
        Source theSource = null;
        for (Source s : this.passiveSources) {
            if (s.isHere(x, y, z)) {
                theSource = s;
                break;
            }
        }
        if (theSource != null) {
            this.passiveSources.remove(theSource);
            this.activeSources.add(theSource);
        }
    }

    private boolean isInWorld(Point p) {
        return !(p.x < 0 || p.y < 0 || p.x > this.taille - 1 || p.y > this.taille - 1);
    }

    private boolean sourceExists(Point p, int altitude) {
        if (isInWorld(p)) {
            return this.hydro.getV()[p.x][p.y] == altitude;
        } else {
            return false;
        }
    }

    private int getAltitude(Point d) {
        if (isInWorld(d)) {
            return this.relief.getV()[d.x][d.y];
        } else {
            return GenConfig.MAX_HEIGHT;
        }
    }

    public List<Source> getPassiveSources() {
        return passiveSources;
    }

    public void setPassiveSources(List<Source> passiveSources) {
        this.passiveSources = passiveSources;
    }

    public List<Source> getActiveSources() {
        return activeSources;
    }

    public void setActiveSources(List<Source> activeSources) {
        this.activeSources = activeSources;
    }
}
