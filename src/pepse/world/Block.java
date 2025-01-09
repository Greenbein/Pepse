package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 *  This class implements block functionality in game
 */
public class Block extends GameObject {
    public static final int SIZE = 40;

    /**
     * constructor of block
     * @param topLeftCorner top left corner of the block
     * @param renderable variable for picture
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
