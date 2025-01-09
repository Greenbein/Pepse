package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * This class implements the sun's halo functionality in the game
 */
public class SunHalo {
    private static final Color HALO_COLOR = new Color(255,255,0,20);
    private static final String SUN_HALO_TAG = "sunHalo";
    private static final Renderable HALO_RENDERABLE = new OvalRenderable(HALO_COLOR);
    private static final float DEFAULT_HALO_SUN_SIZE = 200;

    /**
     * this function creates and updates the sun halo based on given
     * game object sun
     * @param sun in the game
     * @return game object of the sun halo
     */
    public static GameObject create(GameObject sun){
        GameObject sunHalo = new GameObject(
                sun.getTopLeftCorner(),
                new Vector2(DEFAULT_HALO_SUN_SIZE,DEFAULT_HALO_SUN_SIZE),
                HALO_RENDERABLE);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        sunHalo.addComponent((float deltatime) -> {
            sunHalo.setCenter(sun.getCenter());
        });
        return sunHalo;
    }
}
