package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import static pepse.PepseConstants.START_HEIGHT_FACTOR;

/**
 *
 */
public class Sun{
    private static final String SUN_TAG= "sunTag";
    private static final Renderable OVAL = new OvalRenderable(Color.YELLOW);
    private static final float DEFAULT_SUN_SIZE = 100.0f;
    private static final float ROTATION_RADIUS = 300.0f;
    private static final float INITIAL_ANGLE = 0.0f;
    private static final float FINAL_ANGLE = 360.0f;

    /**
     *
     * @param windowDimensions
     * @param cycleLength
     * @return
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        Vector2 initialSunCenter =  new Vector2(windowDimensions.x()/2 -DEFAULT_SUN_SIZE/2,
                windowDimensions.y()*START_HEIGHT_FACTOR-DEFAULT_SUN_SIZE/2 - ROTATION_RADIUS);
        GameObject mySun = new GameObject(initialSunCenter, Vector2.ONES.mult(DEFAULT_SUN_SIZE), OVAL);
        mySun.setTag(SUN_TAG);
        Vector2 cycleCenter =  new Vector2(windowDimensions.x()/2-DEFAULT_SUN_SIZE/2,
                windowDimensions.y()*START_HEIGHT_FACTOR - DEFAULT_SUN_SIZE/2);
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
