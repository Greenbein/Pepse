package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Tree class. It contains two lists of block.
 * The first one for its trunk and the second one for its foliage
 */
public class Tree{
    private static final Color BASE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    private final Random rand;
    private static final String FRUIT = "fruit";
    private static final String LEAF = "leaf";
    private static final String TRUNK = "trunk";
    private static final int DEFAULT_TRUNK_BLOCK_AMOUNT = 5;
    private static final int DEFAULT_LEAFS_ROWS= 8;
    private static final int DEFAULT_LEAFS_COLS= 8;
    private static final float LEAF_CYCLE_LENGTH = 5;
    private static final float INITIAL_DIMS_LEAF_FACTOR = 1.0f;
    private static final float FINAL_DIMS_LEAF_FACTOR = 1.4f;
    private static final Vector2 DEFAULT_LEAF_DIMS = new Vector2(Block.SIZE, Block.SIZE);
    List<Block>trunkBlocks;
    List<Block>leafBlocks;
    List<Block>fruitBlocks;

    /**
     * Constructor for Tree class
     * @param bottomLeftTrunkCorner - bottom left corner of bottom trunk block.
     *                                Basically, it is top left corner of ground block where a tree should be planted.
     */
    public Tree(Vector2 bottomLeftTrunkCorner){
        this.rand = new Random();
        this.trunkBlocks = new ArrayList<>();
        this.leafBlocks = new ArrayList<>();
        this.fruitBlocks = new ArrayList<>();
        createTree(bottomLeftTrunkCorner);
    }

    // this function creates a tree
    private void createTree(Vector2 bottomLeftTrunkCorner){
//        int trunkHeight = this.rand.nextInt(3) + DEFAULT_TRUNK_BLOCK_AMOUNT;
//        int rows = this.rand.nextInt(4) +DEFAULT_LEAFS_ROWS;
//        int cols = this.rand.nextInt(4) + DEFAULT_LEAFS_COLS;
        generateTrunkBlocks(bottomLeftTrunkCorner, DEFAULT_TRUNK_BLOCK_AMOUNT);
        generateLeafBlocks(bottomLeftTrunkCorner,DEFAULT_LEAFS_ROWS,DEFAULT_LEAFS_COLS,DEFAULT_TRUNK_BLOCK_AMOUNT);
        generateFruitBlocks(bottomLeftTrunkCorner,DEFAULT_LEAFS_ROWS,DEFAULT_LEAFS_COLS,DEFAULT_TRUNK_BLOCK_AMOUNT);

    }

    // this function creates tree trunk blocks
    private void generateTrunkBlocks(Vector2 bottomLeftTrunkCorner, int trunkHeight){
        float x = bottomLeftTrunkCorner.x();
        float y = bottomLeftTrunkCorner.y()-Block.SIZE;
        for(int i =0; i<trunkHeight;i++){
            Block trunkBlock =  new Block(new Vector2(x,y-i*Block.SIZE),
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR)));
            trunkBlock.setTag(TRUNK);
            this.trunkBlocks.add(trunkBlock);
        }
    }

    // this function creates leaf blocks
    private void generateLeafBlocks(Vector2 bottomLeftTrunkCorner,int rows,int cols,int trunkHeight){
        float x = bottomLeftTrunkCorner.x() - (rows/2.0f)*Block.SIZE;
        float y = bottomLeftTrunkCorner.y()-Block.SIZE*(trunkHeight+1)-Block.SIZE*(cols/2.0f);
        int foliageNumber = this.rand.nextInt((rows*cols)/2)+(rows*cols)/2;
        int[][] tempArray = new int[rows][cols];
        placeRandomNumberXTimes(tempArray, foliageNumber,1);
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                if(tempArray[i][j] == 1){
                    addLeafBlock(x, i, y, j);
                }
            }
        }
    }

    private void addLeafBlock(float x, int i, float y, int j) {
        Vector2 coordinates = new Vector2(x + Block.SIZE* i, y +Block.SIZE* j);
        Block leafBlock = new Block(new Vector2(coordinates),
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR)));
        float uniqueStartAngle = this.rand.nextInt( 10)-10;
        leafBlock.renderer().setRenderableAngle(uniqueStartAngle);
        leafBlock.setTag(LEAF);
        float finalAngle = this.rand.nextFloat(30)-30;
        float waiTime = this.rand.nextFloat(10)/10;
        new ScheduledTask(leafBlock,
                waiTime,
                true,
                ()->    new Transition<Float>(
                        leafBlock,
                        (Float angle)-> leafBlock.renderer().setRenderableAngle(angle),
                        uniqueStartAngle,
                        uniqueStartAngle+finalAngle,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        LEAF_CYCLE_LENGTH,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null)

        );
        new ScheduledTask(leafBlock,
                waiTime,
                true,
                ()->    new Transition<Float>(
                        leafBlock,
                        (Float factor)-> leafBlock.setDimensions(DEFAULT_LEAF_DIMS.mult(factor)),
                        INITIAL_DIMS_LEAF_FACTOR,
                        FINAL_DIMS_LEAF_FACTOR,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        LEAF_CYCLE_LENGTH,
                        Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                        null)
        );
        leafBlock.setTag(LEAF);
        this.leafBlocks.add(leafBlock);
    }

    private void generateFruitBlocks(Vector2 bottomLeftTrunkCorner,int rows,int cols,int trunkHeight){
        float x = bottomLeftTrunkCorner.x() - 4*Block.SIZE;
        float y = bottomLeftTrunkCorner.y()-Block.SIZE*(trunkHeight+1)-Block.SIZE*(cols/2.0f);
        int fruitNumber = this.rand.nextInt((rows*cols/4))+5;
        int[][] tempArray =  new int[rows][cols];
        placeRandomNumberXTimes(tempArray, fruitNumber,2);
        for(int i = 0; i<rows; i++){
            for(int j = 0; j<cols; j++){
                if(tempArray[i][j] == 2){
                    Vector2 coordinates = new Vector2(x + Block.SIZE*i,y+Block.SIZE*j);
                    addFruitBlock(coordinates);
                }
            }
        }
    }

    private void addFruitBlock(Vector2 coordinates) {
        Block fruitBlock;
        int redOrYellow = this.rand.nextInt(2);
        if(redOrYellow==0){
            fruitBlock =  new Block(new Vector2(coordinates),
                    new OvalRenderable(ColorSupplier.approximateColor(Color.RED)));
            }

        else{
            fruitBlock =  new Block(new Vector2(coordinates),
                    new OvalRenderable(ColorSupplier.approximateColor(Color.YELLOW)));
        }
        fruitBlock.setTag(FRUIT);
        this.fruitBlocks.add(fruitBlock);
    }


    private void placeRandomNumberXTimes(int[][] array, int x, int number) {
        int zeroCount = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 0) {
                    zeroCount++;
                }
            }
        }
        if (x > zeroCount) {
            throw new IllegalArgumentException("x cannot be greater than the number of zero cells in the array");
        }
        Random rand = new Random();
        while (x > 0) {
            int row = rand.nextInt(array.length);
            int col = rand.nextInt(array[0].length);

            if (array[row][col] == 0) {
                array[row][col] = number;
                x--;
            }
        }
    }


    public List<Block> getTrunkBlocks(){
        return this.trunkBlocks;
    }

    public List<Block> getLeafBlocks(){
        return this.leafBlocks;
    }

    public List<Block> getFruitBlocks(){
        return this.fruitBlocks;
    }

}
