/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.BitSet;

/**
 *
 * @author Hendra
 */
public class Attribute {
    String nama;
    int iAwal;
    int iAkhir;
    String pilihan;

    public Attribute(String nm, int aw, int akh, String pil){
        nama = nm;
        iAwal = aw;
        iAkhir = akh;
        pilihan = pil;
    }

    public int getPartInt(Individu individu){
        BitSet part = individu.get(iAwal, iAkhir+1);
        int partInt=0;
        for(int i=0; i<(iAkhir-iAwal+1); ++i)
            if(part.get(i)) partInt |= (1<<i);

        return partInt;
    }
}
