package view;

import javax.swing.JFrame;

import model.world.World;
import model.world.area.EntityArea;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private World world;
    private EntityArea currentArea;
    private AreaPanel areaPanel;

    public MainFrame() {
        this.world = null;
        this.currentArea = null;
        this.areaPanel = new AreaPanel();

        this.setSize(500, 510);

        this.add(this.areaPanel);
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public EntityArea getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(EntityArea currentArea) {
        this.currentArea = currentArea;
        this.areaPanel.setArea(this.currentArea);
    }

    public AreaPanel getAreaPanel() {
        return areaPanel;
    }

    public void setAreaPanel(AreaPanel areaPanel) {
        this.areaPanel = areaPanel;
    }

    public static void main(String[] args) {
        MainFrame fen = new MainFrame();
        World w = new World();
        EntityArea a = w.getCentralArea();
        fen.setWorld(w);
        fen.setCurrentArea(a);
        fen.validate();
        fen.setVisible(true);
        fen.start();
    }

    public void start() {
        Thread thread = new Thread(this.areaPanel);
        thread.run();
    }
}
