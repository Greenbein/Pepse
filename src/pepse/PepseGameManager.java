package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.*;
import pepse.world.trees.Tree;
import pepse.world.trees.Flora;

import java.util.List;
import java.util.Vector;

/**
 *  Game manager of Pepses
 */
public class PepseGameManager extends GameManager {
    private Avatar avatar;
    private float rightBorder;
    private float leftBorder;
    private GameObject energyBar;
    public GameObject collisionBar;
    private WindowController windowController;
    private  Terrain terrain;
    private float screenWidth;
    private float spawnZone;

    @Override
    /**
     * initialises the game
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader,
                inputListener, windowController);
        this.windowController = windowController;
        this.screenWidth = windowController.getWindowDimensions().x();
        this.windowController.setTargetFramerate(40);
        spawnSky();
        spawnNight();
        spawnSunAndHalo();

        spawnWorld(-(int)this.screenWidth*2, (int)this.screenWidth*3);
        this.rightBorder = this.screenWidth*2;
        this.leftBorder = -this.screenWidth*2;
        spawnAvatar(inputListener,imageReader);

        collisionBar =  new GameObject(new Vector2(0,100), new Vector2(20,20),null);
        collisionBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(collisionBar);
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void spawnSky(){
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    private void spawnWorld(int minX,int maxX){
        spawnGround(minX,maxX);
        spawnTrees(minX,maxX);
    }
    private void spawnGround(int minX, int maxX){
        this.terrain =  new Terrain(windowController.getWindowDimensions(),25);
        List<Block> groundBlocks = terrain.createInRange(minX, maxX);
        for (Block b : groundBlocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }
    }

    private void spawnSunAndHalo(){
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 60);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    private void spawnNight(){
        // from here Guy edited
        GameObject night = Night.create(windowController.getWindowDimensions(), 30);
        // want to hide other objects from camera in game
        gameObjects().addGameObject(night, Layer.FOREGROUND);
    }

    private void spawnAvatar(UserInputListener inputListener,ImageReader imageReader){
        this.avatar = new Avatar(windowController.getWindowDimensions(),inputListener,imageReader,this);
        this.avatar.setTag("avatar");
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
    }

    private void spawnTrees(int minX, int maxX){
        Flora treesSpawner = new Flora(this.terrain.getSkyLineCoordinates());
        List<Tree> trees = treesSpawner.createInRange(minX,maxX);
        for (Tree t : trees) {
            List<Block>trunk = t.getTrunkBlocks();
            List<Block>leaves = t.getLeafBlocks();
            List<Block>fruits = t.getFruitBlocks();
            for (Block trunkBlock : trunk) {
                gameObjects().addGameObject(trunkBlock, Layer.STATIC_OBJECTS);
            }
            for (Block fruitBlock : fruits) {
                gameObjects().addGameObject(fruitBlock, Layer.DEFAULT);
            }
            for (Block leafBlock : leaves) {
                gameObjects().addGameObject(leafBlock, -1);
            }
        }
    }


    private void expandMap(){
        float farRightX = this.avatar.getTopLeftCorner().x()+this.screenWidth*2f;
        float farLeftX = this.avatar.getTopLeftCorner().x()-this.screenWidth*2f;
        if(farRightX>=this.rightBorder){
           spawnWorld((int)farRightX,(int)(farRightX+this.screenWidth));
           this.rightBorder=farRightX+this.screenWidth;
        }
        if(farLeftX<=this.leftBorder){
            spawnWorld((int)(farLeftX-this.screenWidth),(int)(farLeftX));
            this.leftBorder=farLeftX-this.screenWidth;
        }
    }
    @Override
    /**
     * this function updates the game
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        gameObjects().removeGameObject(energyBar,Layer.UI);
        TextRenderable text = new TextRenderable(String.valueOf(this.avatar.getEnergy()));
        energyBar =  new GameObject(Vector2.ZERO, new Vector2(50,50),text);
        energyBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(energyBar, Layer.UI);
        expandMap();
    }

    /**
     * this function removes an object from the game
     * @param object the object we would remove
     * @param layer the layer we would remove the object from
     */
    public void removeObject(GameObject object,int layer) {
        gameObjects().removeGameObject(object,layer);
    }

    /**
     * main func runs the game
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
