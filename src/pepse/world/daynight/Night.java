package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;


/**
 * the class implements nights's functionality in the game
 */
public class Night {
    private static final Color BASIC_NIGHT_COLOR =
            Color.BLACK;
    private static final Renderable NIGHT_RENDERABLE =
            new RectangleRenderable
                    (ColorSupplier.approximateColor(BASIC_NIGHT_COLOR));
    private static final String NIGHT_TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * this function creates game object of night
     * @param windowDimensions dimensions of the game window used for creating the block
     * @param cycleLength the amount of time we are at day
     * @return game object of night
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,
                windowDimensions, NIGHT_RENDERABLE);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(NIGHT_TAG);
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength, // the transition time between initial value to final value
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // transition type
                null
        );
        return night;
    }
}
