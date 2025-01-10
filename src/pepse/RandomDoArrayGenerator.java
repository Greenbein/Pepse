package pepse;

import java.util.Random;

public class RandomDoArrayGenerator {
    public static void placeRandomNumberXTimes(int[][] array, int x, int number, int valNumber) {
//        int zeroCount = 0;
//        for (int i = 0; i < array.length; i++) {
//            for (int j = 0; j < array[i].length; j++) {
//                if (array[i][j] == 0) {
//                    zeroCount++;
//                }
//            }
//        }
//        if (x > zeroCount) {
//            throw new IllegalArgumentException("x cannot be greater than the number of zero cells in the array");
//        }
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
