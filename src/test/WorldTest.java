package test;

import view.MainFrame;
import view.tools.ImageManager;
import model.world.Area;
import model.world.World;

public class WorldTest {

    private WorldTest() {
    }

    public static void main(String[] args) {
        loadImageManager();

        World world = new World(1, 1);
        world.printInfo();

        MainFrame fen = new MainFrame();
        Area a = world.getCentralArea();
        fen.setWorld(world);
        fen.setCurrentArea(a);
        fen.validate();
        fen.setVisible(true);

        world.start();
        fen.start();
    }

    private static void loadImageManager() {
        ImageManager iM = ImageManager.createImageManager("fauna");
        iM.addImage("tree", "./ressource/image/tree.png");
        iM.loadAll();
    }
}
