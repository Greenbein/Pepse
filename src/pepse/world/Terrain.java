package pepse.world;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.util.ArrayList;
import java.util.List;

import static pepse.PepseConstants.GROUND_TAG;
import static pepse.PepseConstants.BASE_GROUND_COLOR;
import static pepse.PepseConstants.DEFAULT_BLOCKS_AMOUNT_IN_COLUMN;
import static pepse.PepseConstants.START_HEIGHT_FACTOR;

/**
 * We use this class for spawning different blocks
 * for creating ground level, and it allows other objects
 * to know what is the height for certain x
 */
public class Terrain {
    private final NoiseGenerator ng;
    private final Vector2 windowDimensions;
    private static final Renderable RENDERABLE =
            new RectangleRenderable
                    (ColorSupplier.approximateColor(BASE_GROUND_COLOR));

    /**
     * Creates a Terrain object to manage ground blocks with dynamic height using noise generation.
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed for the noise generation.
     */
    public Terrain(Vector2 windowDimensions, int seed) {
        this.windowDimensions = windowDimensions;
        float groundHeightAtX0 = this.windowDimensions.y() * START_HEIGHT_FACTOR;
        this.ng = new NoiseGenerator(seed, (int) groundHeightAtX0);
    }

    /**
     * Creates ground blocks within a given x-range. Blocks are placed from top to bottom according
     * to the ground height at each x position.
     * @param minX The minimum x-coordinate where blocks start to be created.
     * @param maxX The maximum x-coordinate where blocks should end.
     * @return A list of blocks representing the ground.
     */
    public List<Block> createInRange(int minX, int maxX) {
        List<Block> blocks = new ArrayList<>();
        for (int x = minX; x <= maxX; x += Block.SIZE) {
            int currentHeight = (int) (Math.floor(groundHeightAt(x) / Block.SIZE) * Block.SIZE);
            for (int y = (int) this.windowDimensions.y(); y >= currentHeight; y -= Block.SIZE) {
                Block myBlock = new Block(new Vector2(x, y), RENDERABLE);
                myBlock.setTag(GROUND_TAG);
                blocks.add(myBlock);
            }
        }
        return blocks;
    }

    /**
     * Returns the ground height at a given x position, using a noise generator to simulate terrain
     * variations. Ensures a minimum height based on the default block amount.
     * @param x The x-coordinate where the ground height is queried.
     * @return The ground height at the specified x-coordinate.
     */
    public float groundHeightAt(float x) {
        float defaultHeight = DEFAULT_BLOCKS_AMOUNT_IN_COLUMN * Block.SIZE;
        float newY = (float) (defaultHeight + this.ng.noise(x, (double) Block.SIZE * 7));
        return Math.max(defaultHeight, newY);
    }
}
