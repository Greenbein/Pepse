package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.*;

import java.util.List;
import java.util.Vector;

/**
 *  Game manager of Pepses
 */
public class PepseGameManager extends GameManager {
    @Override
    /**
     * initialises the game
     */
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader,
                inputListener, windowController);
        GameObject sky = Sky.create(windowController.getWindowDimensions());
        // check later to be sure it's background
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        Terrain terrain =  new Terrain(windowController.getWindowDimensions(),15);
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
    }

    /**
     * main func runs the game
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
