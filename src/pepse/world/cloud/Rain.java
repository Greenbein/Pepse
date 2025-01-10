package pepse.world.cloud;

import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.RandomDoArrayGenerator;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Rain {
    private static final Color BASE_RAIN_COLOR = new Color(27, 117,
            149);
    private static final float GRAVITY = 1000;
    private Random rand;
    private static final String RAIN_TAG = "rain";
    private static final int CLOUD_WIDTH_BLOCKS = 6;
    private static final int CLOUD_HEIGHT_BLOCKS = 6;
    private static final int MIN_DROPS_NUMBER = 3;
    private static final int[][] CLOUD_ARR =
            {{0,1,1,0,0,0},
            {1,1,1,0,1,0},
            {1,1,1,1,1,1},
            {1,1,1,1,1,1},
            {0,1,1,1,0,0},
            {0,0,0,0,0,0}};
    private Vector2 topLeftCloudCorner;
    private PepseGameManager pepse;
    public Rain(List<Block>cloudBlocks, PepseGameManager pepse) {
         this.rand = new Random();
         this.pepse = pepse;
         this.topLeftCloudCorner = new Vector2(cloudBlocks.get(0).getTopLeftCorner().x()-Block.SIZE,
                 cloudBlocks.get(0).getTopLeftCorner().y());
//        this.topLeftCloudCorner = new Vector2(200,200);
    }
    public void generateRain(){
        List<Block>dropBlocks = new ArrayList<>();
        int[][]tempArray =  CLOUD_ARR;
//        int dropsNumber = this.rand.nextInt(5) + MIN_DROPS_NUMBER;
        int dropsNumber = 1;
        RandomDoArrayGenerator.placeRandomNumberXTimes(tempArray,dropsNumber,2,1);
        System.out.println("----------------------------------------------");
        System.out.println(dropsNumber);
        for (int i = 0; i<CLOUD_WIDTH_BLOCKS; i++) {
            for (int j = 0; j<CLOUD_HEIGHT_BLOCKS; j++) {
                if(tempArray[i][j] == 2){
                    System.out.println("I have created a new drop");
                    Vector2 currentCoordinates = new Vector2(this.topLeftCloudCorner.x()+j*Block.SIZE,this.topLeftCloudCorner.y()+i*Block.SIZE);
                    Block dropBlock =  new Block(currentCoordinates,new RectangleRenderable(ColorSupplier.approximateColor(BASE_RAIN_COLOR)));
                    dropBlock.setDimensions(new Vector2(10,10));
                    dropBlock.transform().setAccelerationY(GRAVITY);
                    dropBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                    dropBlock.setTag(RAIN_TAG);
                    new Transition<Float>(
                            dropBlock, // the game object being changed
                            dropBlock.renderer()::setOpaqueness, // the method to call
                            1.0f, // initial transition value
                            0.0f, // final transition value
                            Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                            5, // the transition time between initial value to final value
                            Transition.TransitionType.TRANSITION_ONCE, // transition type
                            ()->this.pepse.removeObject(dropBlock,Layer.DEFAULT));
                    dropBlocks.add(dropBlock);
                }
            }
        }
        for (Block block : dropBlocks) {
            this.pepse.addObject(block, -195);
        }
    }
}
