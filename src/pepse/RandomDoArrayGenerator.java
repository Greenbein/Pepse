package pepse;

import java.util.Random;

public class RandomDoArrayGenerator {
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
