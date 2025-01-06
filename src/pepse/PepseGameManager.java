package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.*;
import java.util.List;

/**
 *  Game manager of Pepse
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
        System.out.println("Window height: "+ windowController.getWindowDimensions().y());
        List<Block> blocks = terrain.createInRange(0, (int) windowController.getWindowDimensions().x());
        for (Block b : blocks) {
            gameObjects().addGameObject(b, Layer.STATIC_OBJECTS);
        }
    }

    /**
     * main func runs the game
     * @param args
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
