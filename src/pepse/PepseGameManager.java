package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
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

import java.awt.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    private GameObject velocityBar;
    private GameObject gameObjectsSizeBar;
    private GameObject deletedCounterBar;
    private WindowController windowController;
    private RenderingController renderingController;

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
        this.windowController.setTargetFramerate(100);
        this.windowController.setTargetFramerate(40);
        spawnSky();
        spawnNight();
        spawnSunAndHalo();
        spawnAvatar(inputListener,imageReader);
        //spawnFirstPartOfTheWorld();
        createEnergyBar();
        createVelocityBar();
        createCollisionBar();
        createGameObjectsSizeBar();
        createDeletedCounterBar();
        this.renderingController =  new RenderingController(this.avatar,windowController,this);
    }

    private void spawnFirstPartOfTheWorld() {
        spawnWorld(-(int)(this.screenWidth*1.5f),(int)(this.screenWidth*1.5f));
        this.rightBorder = this.screenWidth*1.5f;
        this.leftBorder = -this.screenWidth*1.5f;
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
        setCamera(new Camera(avatar, Vector2.ZERO,
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    private void spawnTrees(int minX, int maxX){
        Flora treesSpawner = new Flora(this.terrain.getSkyLineCoordinates(this.avatar));
        List<Tree> trees = treesSpawner.createInRange(minX,maxX);
        for (Tree t : trees) {
            List<Block>trunk = t.getTrunkBlocks();
            List<Block>leaves = t.getLeafBlocks();
            List<Block>fruits = t.getFruitBlocks();
            for (Block trunkBlock : trunk) {
                trunkBlock.renderer().setRenderable(null);
                gameObjects().addGameObject(trunkBlock, Layer.STATIC_OBJECTS);
            }
            for (Block fruitBlock : fruits) {
                fruitBlock.renderer().setRenderable(null);
                gameObjects().addGameObject(fruitBlock, Layer.DEFAULT);
            }
            for (Block leafBlock : leaves) {
                leafBlock.renderer().setRenderable(null);
                gameObjects().addGameObject(leafBlock, -1);
            }
        }
    }

    private void createEnergyBar(){
        this.energyBar =  new GameObject(Vector2.ZERO, new Vector2(20,20),null);
        this.energyBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(energyBar, Layer.UI);
    }

    private void createCollisionBar() {
        collisionBar =  new GameObject(new Vector2(0,20), new Vector2(20,20),null);
        collisionBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(collisionBar, Layer.UI);
    }

    private void createVelocityBar(){
        this.velocityBar =  new GameObject(new Vector2(0,40), new Vector2(20,20),null);
        this.velocityBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(velocityBar, Layer.UI);
    }

    private void createGameObjectsSizeBar() {
        this.gameObjectsSizeBar =  new GameObject(new Vector2(0,60), new Vector2(20,20),null);
        this.gameObjectsSizeBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(gameObjectsSizeBar,Layer.UI);
    }

    private void createDeletedCounterBar(){
        this.deletedCounterBar =  new GameObject(new Vector2(0,80), new Vector2(20,20),null);
        this.deletedCounterBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(deletedCounterBar, Layer.UI);
    }


    private void expandMap(){
        float farRightX = this.avatar.getTopLeftCorner().x()+this.screenWidth*1.5f;
        float farLeftX = this.avatar.getTopLeftCorner().x()-this.screenWidth*1.5f;
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
        updateEnergyBar();
        updateVelocityBar();
        updateGameObjectsSizeBar();
        this.renderingController.updateAllObjectsArr();
        this.renderingController.selectObjectsAndDraw();
//        expandMap();
    }

    private boolean inOldCollection(GameObject obj){
        for(GameObject o: gameObjects()){
            if(o.getTopLeftCorner().x() == obj.getTopLeftCorner().x() && o.getTopLeftCorner().y() == obj.getTopLeftCorner().y()){
                return true;
            }
        }
        return false;
    }

    private boolean isInList(GameObject obj, List<GameObject> list){
        for(GameObject o: list){
            if(o.getTopLeftCorner().x() == obj.getTopLeftCorner().x()
                    && o.getTopLeftCorner().y() == obj.getTopLeftCorner().y()){
                return true;
            }
        }
        return false;
    }

    public void drawObjects(List<GameObject> objects) {
        int deletedCounter = 0;
        for(GameObject oldGameObject: gameObjects()) {
            if(!isInList(oldGameObject,objects)){
                removeByTag(oldGameObject);
                deletedCounter++;
            }
        }
        updateDeletedCounterBar(deletedCounter);
        for(GameObject potentialNewObject: objects){
            if(!inOldCollection(potentialNewObject)){
                addByTag(potentialNewObject);
            }
        }
    }

    private void addByTag(GameObject o) {
        switch (o.getTag()) {
            case "trunk", "ground":
                gameObjects().addGameObject(o, Layer.STATIC_OBJECTS);
                break;
            case "leaf":
                gameObjects().addGameObject(o, -1);
                break;
            case "fruit":
                gameObjects().addGameObject(o, Layer.DEFAULT);
                break;
            default:
                gameObjects().addGameObject(o, Layer.BACKGROUND);
        }
    }

    private void removeByTag(GameObject o) {
        switch (o.getTag()) {
            case "trunk", "ground":
                gameObjects().removeGameObject(o, Layer.STATIC_OBJECTS);
                break;
            case "leaf":
                gameObjects().removeGameObject(o, -1);
                break;
            case "fruit":
                gameObjects().removeGameObject(o, Layer.DEFAULT);
                break;
            default:
                break;
        }
    }



    private void updateVelocityBar() {
        TextRenderable velocityText = new TextRenderable("Avatar's velocity :" + this.avatar.getVelocity().toString());
        this.velocityBar.renderer().setRenderable(velocityText);
    }

    private void updateDeletedCounterBar(int counter) {
        TextRenderable deletedCounterText = new TextRenderable("Objects have been deleted :"+counter);
        this.deletedCounterBar.renderer().setRenderable(deletedCounterText);
    }

    private void updateGameObjectsSizeBar() {
        int counter = 0;
        for (GameObject o : gameObjects()) {
            counter++;
        }
        TextRenderable sizeText = new TextRenderable("There are :"+ counter+" objects");
        this.gameObjectsSizeBar.renderer().setRenderable(sizeText);
    }

    private void updateEnergyBar() {
        TextRenderable text = new TextRenderable(String.valueOf(this.avatar.getEnergy()));
        this.energyBar.renderer().setRenderable(text);
    }

    public void removeObject(GameObject object,int layer) {
        if(object.getTag().equals("fruit")){
            this.renderingController.removeObject(object);
        }
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
