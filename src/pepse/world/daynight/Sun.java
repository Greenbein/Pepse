package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
/**
 * Here we build a class for sun object.
 * It rotates according to day time around a specific point on a skyline
 */
public class Sun{
    private static final String SUN_TAG= "sun";
    private static final Renderable OVAL = new OvalRenderable(Color.YELLOW);
    private static final float START_HEIGHT_FACTOR = 2.0f / 3.0f;
    private static final float DEFAULT_SUN_SIZE = 100.0f;
    private static final float ROTATION_RADIUS = 300.0f;
    private static final float INITIAL_ANGLE = 0.0f;
    private static final float FINAL_ANGLE = 360.0f;

    /**
     * This method creates a new Sun in a form of GameObject
     * The sun is spawned above a central point on the skyline
     * Then transition rotate the sun
     * @param windowDimensions - dimensions of the screen
     * @param cycleLength - transition time
     * @return sun object of  type GameObject
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Vector2 initialSunCenter =  new Vector2(windowDimensions.x()/2 -DEFAULT_SUN_SIZE/2,
                windowDimensions.y()*START_HEIGHT_FACTOR-DEFAULT_SUN_SIZE/2 - ROTATION_RADIUS);
        GameObject mySun = new GameObject(initialSunCenter, Vector2.ONES.mult(DEFAULT_SUN_SIZE), OVAL);
        mySun.setTag(SUN_TAG);
        Vector2 cycleCenter =  new Vector2(windowDimensions.x()/2-DEFAULT_SUN_SIZE/2,
                windowDimensions.y()*START_HEIGHT_FACTOR - DEFAULT_SUN_SIZE/2);
        mySun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        new Transition<Float>(
                mySun,
                (Float angle)-> mySun.setCenter(
                        initialSunCenter.subtract(cycleCenter).
                                rotated(angle).add(cycleCenter)),
                INITIAL_ANGLE,
                FINAL_ANGLE,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);
        return mySun;
    }
}
