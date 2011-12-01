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
        float STARTF = 0.6f;
        float ENDF = 0.910f;
        RandomGendruwo random = new RandomGendruwo();
        for (int idx = 1; idx <= 10; ++idx) {
            float u = random.nextFloat(STARTF, ENDF);
            System.out.println("Generated: "+u);
        }
        
        int inete = 1280;
        int siz = 8124;
        float hasil = (float)inete/siz;
        System.out.println(hasil);
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
        double range = (double) aEnd - (double) aStart ;
        // compute a fraction of the range, 0 <= frac < range
        double fraction = range * this.nextDouble();
        float randomNumber = (float) (fraction + aStart);
        return randomNumber;
    }
}
