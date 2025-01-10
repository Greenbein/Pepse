package pepse.world;

import danogl.GameObject;
import danogl.gui.WindowController;
import pepse.PepseGameManager;
import pepse.world.trees.Flora;
import pepse.world.trees.Tree;
import java.util.ArrayList;
import java.util.List;

public class RenderingController {
    private final List<GameObject> allGameObjects;
    private final PepseGameManager pepse;
    private final GameObject avatar;
    private final WindowController windowController;
    private Terrain terrain;
    private final int radius;
    private int maxRight;
    private int minLeft;

    public RenderingController(GameObject avatar, WindowController windowController, PepseGameManager pepse) {
        this.allGameObjects = new ArrayList<>();
        this.avatar = avatar;
        this.windowController = windowController;
        this.pepse = pepse;
        this.terrain = new Terrain(windowController.getWindowDimensions(), 24);
        this.radius = (int) (windowController.getWindowDimensions().x() * 1.5f);
        spawnStartZone();
    }

    private void spawnStartZone() {
        this.maxRight = (int) (this.windowController.getWindowDimensions().x() * 2.0f);
        this.minLeft = (int) (-this.windowController.getWindowDimensions().x() * 2.0f);
        spawnWorld(this.minLeft, this.maxRight);
    }

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

    public void spawnWorld(int minX, int maxX) {
        spawnGround(minX, maxX);
        spawnTrees(minX, maxX);
    }

    public void spawnGround(int minX, int maxX) {
        this.terrain = new Terrain(windowController.getWindowDimensions(), 25);
        List<Block> groundBlocks = terrain.createInRange(minX, maxX);
        allGameObjects.addAll(groundBlocks);
    }

    private void spawnTrees(int minX, int maxX) {
        Flora treesSpawner = new Flora(this.terrain.getSkyLineCoordinates(this.avatar));
        List<Tree> trees = treesSpawner.createInRange(minX, maxX);
        for (Tree t : trees) {
            allGameObjects.addAll(t.getTrunkBlocks());
            allGameObjects.addAll(t.getLeafBlocks());
            allGameObjects.addAll(t.getFruitBlocks());
        }
    }

    public void selectObjectsAndDraw() {
        float avatarX = this.avatar.getCenter().x();
        List<GameObject> objectsToDraw = new ArrayList<>();
        float windowWidth = this.windowController.getWindowDimensions().x();
        for (GameObject gameObject : allGameObjects) {
            if (!gameObject.getTag().equals("cloud")) {
                if (gameObject.getCenter().x() < avatarX + windowWidth / 2.0f && gameObject.getCenter().x() > avatarX - windowWidth / 2.0f) {
                    objectsToDraw.add(gameObject);
                }
            } else {
                if (Math.abs(gameObject.getCenter().x() - avatarX) < 2 * windowWidth) {
                    objectsToDraw.add(gameObject);
                }
            }
        }
        this.pepse.drawObjects(objectsToDraw);
    }

    public void removeObject(GameObject gameObject) {
        allGameObjects.remove(gameObject);
    }
}