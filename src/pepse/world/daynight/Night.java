package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;

import static pepse.PepseConstants.BASE_GROUND_COLOR;

/**
 * the class implements nights's functionality in the game
 */
public class Night {
    // need to go over color of night black
    private static final Color BASIC_NIGHT_COLOR =
            Color.decode("#000000");
    private static final Renderable NIGHT_RENDERABLE =
            new RectangleRenderable
                    (ColorSupplier.approximateColor(BASIC_NIGHT_COLOR));
    private static final String TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,
                windowDimensions, NIGHT_RENDERABLE);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(TAG);
        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength, // the transition time between initial value to final value
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // need to go over this is the way we transition
                null
        );
//        new Transition<Float>(
//                night, // the game object being changed
//                night.renderer()::setOpaqueness, // the method to call
//                0f, // initial transition value
//                MIDNIGHT_OPACITY, // final transition value
//
//                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
//                ???, // transition fully over half a day
//        Transition.TransitionType.???, // Choose appropriate ENUM value
//        null
//);// nothing further to execute upon reaching final value
        return night;
    }
}
