/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendruwo;

import java.util.Random;

/**
 *
 * @author Albadr
 */
public class RandomGendruwo extends Random {

    public static void main(String... aArgs) {
        int START = 6;
        int END = 10;
        RandomGendruwo random = new RandomGendruwo();
        for (int idx = 1; idx <= 10; ++idx) {
            random.nextInt(START, END);
        }
    }

    public int nextInt(int aStart, int aEnd) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * this.nextDouble());
        int randomNumber = (int) (fraction + aStart);
        return randomNumber;
    }
    
    public float nextFloat(float aStart, float aEnd) {
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long) aEnd - (long) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long) (range * this.nextDouble());
        float randomNumber = (float) (fraction + aStart);
        return randomNumber;
    }
}
