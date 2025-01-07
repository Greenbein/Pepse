package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.*;

import java.util.List;
import java.util.Vector;

/**
 *  Game manager of Pepses
 */
public class PepseGameManager extends GameManager {
    private Avatar avatar;
    private GameObject energyBar;
    private WindowController windowController;
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
        this.windowController.setTargetFramerate(60);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        // check later to be sure it's background
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        Terrain terrain =  new Terrain(windowController.getWindowDimensions(),1);
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block b : blocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }
        // from here Guy edited
        GameObject night = Night.create(windowController.getWindowDimensions(),30);
        // want to hide other objects from camera in game
        gameObjects().addGameObject(night, Layer.FOREGROUND);

        GameObject sun =  Sun.create(windowController.getWindowDimensions(),60);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);

        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);

//        Vector2 avatarCoords =  new Vector2(windowController.getWindowDimensions().x(),
//                windowController.getWindowDimensions().y()*(2.0f/3.0f));

        avatar = new Avatar(windowController.getWindowDimensions(),inputListener,imageReader);
        gameObjects().addGameObject(avatar, Layer.DEFAULT);

        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
    }

    @Override
    /**
     * this function updates Pepse game manager
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        gameObjects().removeGameObject(energyBar,Layer.UI);
        TextRenderable text = new TextRenderable(String.valueOf(this.avatar.getEnergy()));
        energyBar =  new GameObject(Vector2.ZERO, new Vector2(100,100),text);
        gameObjects().addGameObject(energyBar, Layer.UI);
    }

    /**
     * main func runs the game
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
