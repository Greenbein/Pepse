package pepse.world.cloud;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * This class implements the  game object cloud
 */
public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255,
            255);
    private static final String CLOUD_TAG = "cloud";
    private static final List<List<Integer>> CLOUD_SHAPE = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );
    private static final float TRANSITION_TIME_CLOUD = 10f;
    private final List<Block>cloudBlocks;
    private final Vector2 topLeftCornerVectorCloud;
    private final float cloudOffScreenX;

    /**
     * constructor of game object cloud
     * @param cordsTopLeftCornerCloud holds the coords of the top left corner of the cloud
     * @param cloudOffScreenX hold the x coords that the cloud would exit the screen from
     *                        the right edge of the screen
     */
    public Cloud(Vector2 cordsTopLeftCornerCloud,float cloudOffScreenX ){
        this.cloudBlocks = new ArrayList<>();
        this.topLeftCornerVectorCloud = cordsTopLeftCornerCloud;
        this.cloudOffScreenX = cloudOffScreenX;
        createCloud(CLOUD_SHAPE);
    }

    // this function creates a block of a cloud
    private Block addCloudBlock(Vector2 topLeftCornerBlock, float maxX) {
        Block cloudBlock = new Block(topLeftCornerBlock, new
                RectangleRenderable(ColorSupplier.approximateMonoColor(
                BASE_CLOUD_COLOR)));
        cloudBlock.setTag(CLOUD_TAG);
        new ScheduledTask(
            cloudBlock,
            0,
            false,
             // callback for the cloud block movement
             ()-> new Transition<Float>(
                    cloudBlock,
                    (Float x)-> cloudBlock.setTopLeftCorner(new Vector2
                            (x + topLeftCornerBlock.x(),topLeftCornerBlock.y())),
                    0f,
                     maxX ,
                     Transition.LINEAR_INTERPOLATOR_FLOAT,
                     TRANSITION_TIME_CLOUD,
                     Transition.TransitionType.TRANSITION_LOOP,
                    null)

        );
        cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return cloudBlock;
    }

    // this func creates the whole cloud with the given list cloudShape
    // and with the func addCloudBlock for each block that is in the cloud
    private void createCloud(List<List<Integer>> cloudShape){
        for(int i = 0; i < cloudShape.size(); i++){
            List<Integer> row = cloudShape.get(i);
            for(int j = 0; j < cloudShape.get(i).size(); j++){
                if(row.get(j) == 1){
                    Vector2 cloudPosition = new Vector2(topLeftCornerVectorCloud.x()
                            + j * Block.SIZE,
                            topLeftCornerVectorCloud.y() + i * Block.SIZE);
                    this.cloudBlocks.add(addCloudBlock(cloudPosition,cloudOffScreenX));
                }
            }
        }
    }

    /**
     * this func is a get func for the cloud block list
     * @return the list of the cloud blocks
     */
    public List<Block> getCloudBlocks(){
        return cloudBlocks;
    }
}
