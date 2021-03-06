package test;

import view.tools.WELogger;
import model.entity.Entity;
import model.entity.flora.Tree;

public class EntityTest {

    private EntityTest() {
    }

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
}
