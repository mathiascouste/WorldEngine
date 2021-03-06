package test;

import view.AreaView;
import view.tools.ImageManager;
import model.world.World;
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
        
        
        AreaView aV = new AreaView(world);

        world.start();
        aV.start();
        aV.setVisible(true);
        
    }

    private static void loadImageManager() {
        ImageManager iM = ImageManager.createImageManager("fauna");
        iM.addImage("tree", "./ressource/image/tree.png");
        iM.loadAll();
    }
}
