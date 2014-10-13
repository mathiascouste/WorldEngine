package test;

import view.tools.WELogger;
import model.world.entity.flora.Tree;
import model.world.entity.Entity;

public class EntityTest {
    public static void main(String[] args) {
        Entity ent = new Tree();
        ent.born();
        try {
            Thread.sleep((long) 10000);
        } catch (InterruptedException e) {
            WELogger.log(e.getMessage());
        }
        ent.die();
    }

    private EntityTest() {
    }
}
