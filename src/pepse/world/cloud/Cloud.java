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

import static pepse.PepseConstants.START_HEIGHT_FACTOR;

public class Cloud {
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255,
            255);
    private static final Renderable CLOUD_RENDERABLE = new
            RectangleRenderable(ColorSupplier.approximateMonoColor(
            BASE_CLOUD_COLOR));
    private static final String CLOUD_TAG = "cloud";
    private static final List<List<Integer>> CLOUD_SHAPE = List.of(
            List.of(0, 1, 1, 0, 0, 0),
            List.of(1, 1, 1, 0, 1, 0),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(1, 1, 1, 1, 1, 1),
            List.of(0, 1, 1, 1, 0, 0),
            List.of(0, 0, 0, 0, 0, 0)
    );
    private final List<Block>cloudBlocks;
    private final Vector2 topLeftCornerVectorCloud;
    private final float rightSideScreen;

    public Cloud(Vector2 positionVector,float rightSideScreen ){
        this.cloudBlocks = new ArrayList<>();
        this.topLeftCornerVectorCloud = positionVector;
        this.rightSideScreen = rightSideScreen;
        createCloud(CLOUD_SHAPE);
    }

    private Block addCloudBlock(Vector2 topLeftCornerBlock, float maxX) {
        Block cloudBlock = new Block(topLeftCornerBlock, CLOUD_RENDERABLE);
        cloudBlock.setTag(CLOUD_TAG);
        new ScheduledTask(
            cloudBlock,
            0,
            false,
             ()-> new Transition<Float>(
                    cloudBlock,
                    (Float x)-> cloudBlock.setTopLeftCorner(new Vector2
                            (x + topLeftCornerBlock.x(),topLeftCornerBlock.y())),
                    0f,
                     maxX ,
                     Transition.LINEAR_INTERPOLATOR_FLOAT,
                    10,
                     Transition.TransitionType.TRANSITION_LOOP,
                    null)

        );
        cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        return cloudBlock;
    }

    private void createCloud(List<List<Integer>> cloudShape){
        for(int i = 0; i < cloudShape.size(); i++){
            List<Integer> row = cloudShape.get(i);
            for(int j = 0; j < cloudShape.get(i).size(); j++){
                if(row.get(j) == 1){
                    Vector2 cloudPosition = new Vector2(topLeftCornerVectorCloud.x()
                            + j * Block.SIZE,
                            topLeftCornerVectorCloud.y() + i * Block.SIZE);
                    this.cloudBlocks.add(addCloudBlock(cloudPosition,rightSideScreen));
                }
            }
        }
    }

    public List<Block> getCloudBlocks(){
        return cloudBlocks;
    }
}
