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
import pepse.world.cloud.Cloud;
import pepse.world.cloud.Rain;
import pepse.world.daynight.*;
import java.util.List;

/**
 *  Game manager of Pepses
 */
public class PepseGameManager extends GameManager {
    private Avatar avatar;
    private GameObject energyBar;
    private WindowController windowController;
    private RenderingController renderingController;
    private Cloud cloud;
    private Rain rain;

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
        this.windowController.setTargetFramerate(40);
        spawnSky();
        spawnNight();
        spawnSunAndHalo();
        spawnAvatar(inputListener,imageReader);
        createEnergyBar();
        this.renderingController =  new RenderingController(this.avatar,windowController,this);
        spawnCloud();
        this.rain = new Rain(this.cloud.getCloudBlocks(),this);
    }

    private void spawnSky(){
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    private void spawnSunAndHalo(){
        GameObject sun = Sun.create(windowController.getWindowDimensions(), 60);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    private void spawnNight(){
        GameObject night = Night.create(windowController.getWindowDimensions(), 30);
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

    private void spawnCloud(){
        Vector2 initialTopLeftCorner = new Vector2(- Block.SIZE *6,
                this.windowController.getWindowDimensions().y()/40);
        this.cloud = new Cloud(initialTopLeftCorner,
                this.windowController.getWindowDimensions().x() + Block.SIZE *6);
        for(Block cloudBlock: cloud.getCloudBlocks()){
            gameObjects().addGameObject(cloudBlock, -190);
        }
    }

    @Override
    /**
     * this function updates the game
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        updateEnergyBar();
        this.renderingController.updateAllObjectsArr();
        this.renderingController.selectObjectsAndDraw();
    }

    //========================Methods for real time rendering========================

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
        for(GameObject oldGameObject: gameObjects()) {
            if(!isInList(oldGameObject,objects)){
                removeByTag(oldGameObject);
            }
        }
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
            case "rain":
                gameObjects().addGameObject(o, Layer.BACKGROUND);
                break;
            default:
                break;
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

    public void addObject(GameObject object,int layer){
        gameObjects().addGameObject(object,layer);
    }

    public void removeObject(GameObject object,int layer) {
        if(object.getTag().equals("fruit")){
            this.renderingController.removeObject(object);
        }
        gameObjects().removeGameObject(object,layer);
    }

    public void generateRain(){
        this.rain.generateRain();
    }

    /**
     * main func runs the game
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }





    //========================Create different bars========================

    private void createEnergyBar(){
        this.energyBar =  new GameObject(Vector2.ZERO, new Vector2(20,20),null);
        this.energyBar.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(energyBar, Layer.UI);
    }

    //========================Update different bars========================
    private void updateEnergyBar() {
        TextRenderable text = new TextRenderable(String.valueOf(this.avatar.getEnergy()));
        this.energyBar.renderer().setRenderable(text);
    }
}


