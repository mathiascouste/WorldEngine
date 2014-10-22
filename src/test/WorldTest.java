package test;

import view.AreaView;
import view.MainFrame;
import view.tools.ImageManager;
import model.world.World;
import model.world.area.EntityArea;
import model.world.generator.WorldGenerator;

public class WorldTest {

    private WorldTest() {
    }

    public static void main(String[] args) {
        loadImageManager();

        World world = new World(1, 1);
        world.printInfo();
        WorldGenerator wG = new WorldGenerator(world);
        wG.generateMap();

        EntityArea a = world.getCentralArea();
        
        AreaView aV = new AreaView(world);

        world.start();
        Thread t = new Thread(world.getCentralArea());
        t.start();
        
        aV.setVisible(true);
        
        /*MainFrame fen = new MainFrame();
        fen.setWorld(world);
        fen.setCurrentArea(a);
        fen.validate();
        fen.setVisible(true);

        world.start();
        fen.start();*/
    }

    private static void loadImageManager() {
        ImageManager iM = ImageManager.createImageManager("fauna");
        iM.addImage("tree", "./ressource/image/tree.png");
        iM.loadAll();
    }
}
