package view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import view.tools.WELogger;

import model.world.Area;

public class AreaPanel extends JPanel implements MouseMotionListener,
        MouseWheelListener, Runnable {
    private static final long serialVersionUID = 1L;

    private static final int SIZE = 500;
    private static final int MARGIN = 50;
    private static final int OFFSET = 16;
    private static final int FPS = 50;
    private boolean top, left, bottom, right;

    private Area area;
    private int offsetX, offsetY;
    private double scale;

    public AreaPanel() {
        this(null);
    }

    public AreaPanel(Area area) {
        this.area = area;
        this.setSize(500, 500);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);

        this.scale = 1;
        this.offsetX = 0;
        this.offsetY = 0;

        this.top = this.left = this.bottom = this.right = false;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(scale, scale);
        g2.translate(-this.offsetX, -this.offsetY);
        this.area.draw(g);
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent arg0) {
        double scroll = -arg0.getPreciseWheelRotation();
        if (scroll > 0) {
            double maxScale = 16;
            if (scale < maxScale) {
                this.scale *= Math.pow(2, scroll);
                this.offsetX *= Math.pow(2, scroll);
                this.offsetY *= Math.pow(2, scroll);
            }
        }
        if (scroll < 0) {
            double minScale = 1.0 / 2.0;
            if (scale > minScale) {
                this.scale *= Math.pow(2, scroll);
                this.offsetX *= Math.pow(2, scroll);
                this.offsetY *= Math.pow(2, scroll);
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
        this.offsetX += arg0.getX();
        this.offsetY += arg0.getY();
        this.offsetX -= arg0.getX();
        this.offsetY -= arg0.getY();
    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
        int x = arg0.getX();
        int y = arg0.getY();
        if (x < MARGIN) {
            this.left = true;
        } else {
            this.left = false;
        }
        if (y < MARGIN) {
            this.top = true;
        } else {
            this.top = false;
        }
        if (x > SIZE - MARGIN) {
            this.right = true;
        } else {
            this.right = false;
        }
        if (y > SIZE - MARGIN) {
            this.bottom = true;
        } else {
            this.bottom = false;
        }
    }

    @Override
    public void run() {
        while (true) {
            this.doOffsets();
            this.repaint();
            try {
                Thread.sleep(1000 / FPS);
            } catch (InterruptedException e) {
                WELogger.log(e.getMessage());
            }
        }
    }

    private void doOffsets() {
        if (this.top && offsetY >= OFFSET) {
            offsetY -= OFFSET / this.scale;
        }
        if (this.bottom && offsetY <= SIZE - OFFSET) {
            offsetY += OFFSET / this.scale;
        }
        if (this.left && offsetX >= OFFSET) {
            offsetX -= OFFSET / this.scale;
        }
        if (this.right && offsetX <= SIZE - OFFSET) {
            offsetX += OFFSET / this.scale;
        }
    }
}
