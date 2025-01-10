package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.*;

/**
 *
 */
public class Flora {
    private static final float TREE_PROBABILITY = 0.1f;
    private final List<Vector2> skyLineCoords;
    private final Random rand;
    public Flora(List<Vector2>skyLineCoords) {
        this.skyLineCoords = skyLineCoords;
        this.rand = new Random();
    }
    /**
     * The function creates an array of trees
     * Rounds minX and maxX
     * Creates an array of block that are upper ground block (extracts it from groundBlocks)
     * Passes through all x coords of columns in the area [minX,...,maxX]
     * For x it generates a random number from 1 to 10
     * If the number equals to 1, it creates a new tree with BOTTOM left corner,
     * that equals to TOP left corner of a block with the index x in upperGroundBlocks array
     * @param minX - left border of planting tree area
     * @param maxX - right border of planting tree area
     * @return
     */
    public List<Tree> createInRange(int minX, int maxX){
        minX = (int)(Math.floor((double) minX / Block.SIZE) * Block.SIZE);
        maxX = (int)(Math.floor((double) maxX / Block.SIZE) * Block.SIZE);
        List<Tree>trees = new ArrayList<>();
        int [] tempArr =  new int[maxX-minX];
        int numBlocksInRange = (maxX - minX) / Block.SIZE + 1;
        for (int x = 0; x<=numBlocksInRange; x++){
            float randomCoinResult = rand.nextFloat(1);
            if(randomCoinResult < TREE_PROBABILITY) {
                if (Math.abs((minX + x)%skyLineCoords.size()) < skyLineCoords.size() && checkArea(tempArr,x)){
                    tempArr[x] = 1;
                    Tree newTree = new Tree(skyLineCoords.get(Math.abs((minX + x)%skyLineCoords.size())));
                    trees.add(newTree);
                }
            }
        }
        return trees;
    }

    private boolean checkArea(int[]arr,int index){
        if(index>0 && index<arr.length-1){
            return arr[index-1]==0 && arr[index+1]==0;
        }
        if(index==arr.length-1){
            return arr[index-1]==0;
        }
        else{
            return arr[index+1]==0;
        }
    }

}
