package pepse;

import java.util.Random;

/**
 * The class is created for one public method
 * The method fill randomly the gotten array by a gotten number x times
 */
public class RandomDoArrayGenerator {
    /**
     * Fill randomly the gotten array
     * @param array - te array we want to fill
     * @param x - the number of random cells we want to fill with "number"
     * @param number - the number we want to add
     * @param valNumber - with it, we verify whether to fill the cell or not
     */
    public static void placeRandomNumberXTimes(int[][] array, int x,
                                               int number, int valNumber) {
        Random rand = new Random();
        while (x > 0) {
            int row = rand.nextInt(array.length);
            int col = rand.nextInt(array[0].length);

            if (array[row][col] == valNumber) {
                array[row][col] = number;
                x--;
            }
        }
    }
}
