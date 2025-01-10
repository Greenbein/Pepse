package pepse.world;

import danogl.GameObject;
import danogl.gui.WindowController;
import pepse.PepseGameManager;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;
import java.util.ArrayList;
import java.util.List;

/**
 * RenderingController is responsible for rendering trees and terrain blocks
 * It renders only the block that ar in the frame
 * All blocks that were ever created are saved in allGameObjects
 * It renders special blocks by sending them to
 * GameObjectCollection in PepseGameManager
 */
public class RenderingController {
    private final List<GameObject> allGameObjects;
    private final PepseGameManager pepse;
    private final GameObject avatar;
    private final WindowController windowController;
    private Terrain terrain;
    private final int radius;
    private int maxRight;
    private int minLeft;

    /**
     * Constructor of RenderingController
     * @param avatar - the main character of the game. With its coords we will
     *               decide whether the block should be rendered or not
     * @param windowController - we use to get window dimensions
     * @param pepse - helps us to use public method the main game class (PepseGameManager)
     */
    public RenderingController(GameObject avatar, WindowController windowController, PepseGameManager pepse) {
        this.allGameObjects = new ArrayList<>();
        this.avatar = avatar;
        this.windowController = windowController;
        this.pepse = pepse;
        this.terrain = new Terrain(windowController.getWindowDimensions(), 24);
        this.radius = (int) (windowController.getWindowDimensions().x() * 1.5f);
        spawnStartZone();
    }

    // spawn the first part of the world while initializing the game
    private void spawnStartZone() {
        this.maxRight = (int) (this.windowController.getWindowDimensions().x() * 2.0f);
        this.minLeft = (int) (-this.windowController.getWindowDimensions().x() * 2.0f);
        spawnWorld(this.minLeft, this.maxRight);
    }

    /**
     * The method updates trees and terrain that should be rendered
     */
    public void updateAllObjectsArr() {
        int avatarX = (int) this.avatar.getTopLeftCorner().x();
        if (avatarX + this.radius >= this.maxRight) {
            spawnWorld(this.maxRight, this.maxRight + this.radius);
            this.maxRight += this.radius;
        }
        if (avatarX - this.radius <= this.minLeft) {
            spawnWorld(this.minLeft - this.radius, this.minLeft);
            this.minLeft -= this.radius;
        }
    }

    // spawn a new ground and trees' blocks
    private void spawnWorld(int minX, int maxX) {
        spawnGround(minX, maxX);
        spawnTrees(minX, maxX);
    }

    // spawn a new ground blocks
    private void spawnGround(int minX, int maxX) {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 25);
        List<Block> groundBlocks = terrain.createInRange(minX, maxX);
        allGameObjects.addAll(groundBlocks);
    }

    // spawn a new trees' blocks
    private void spawnTrees(int minX, int maxX) {
        Flora treesSpawner = new Flora(this.terrain.getSkyLineCoordinates(this.avatar));
        List<Tree> trees = treesSpawner.createInRange(minX, maxX);
        for (Tree t : trees) {
            allGameObjects.addAll(t.getTrunkBlocks());
            allGameObjects.addAll(t.getLeafBlocks());
            allGameObjects.addAll(t.getFruitBlocks());
        }
    }

    /**
     * select all blocks that are in the frame and send them to the main GameObjectCollection
     * We claim that block in frame if its distance from
     * the avatar is less or equals to half o screen width
     */
    public void selectObjectsAndDraw() {
        float avatarX = this.avatar.getCenter().x();
        List<GameObject> objectsToDraw = new ArrayList<>();
        float windowWidth = this.windowController.getWindowDimensions().x();
        for (GameObject gameObject : allGameObjects) {
            if (gameObject.getCenter().x() < avatarX + windowWidth / 2.0f &&
                    gameObject.getCenter().x() > avatarX - windowWidth / 2.0f) {
                objectsToDraw.add(gameObject);
            }
        }
        this.pepse.drawObjects(objectsToDraw);
    }

    /**
     * The method removes the selected object from the database of all ever created blocks
     * @param gameObject  - the object we want to delete
     */
    public void removeObject(GameObject gameObject) {
        allGameObjects.remove(gameObject);
    }
}