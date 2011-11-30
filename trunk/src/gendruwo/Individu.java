/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.BitSet;
import java.util.ArrayList;

/**
 *
 * @author Hendra
 */
public class Individu extends BitSet {
    
    
    /**
     * Membandingkan dua individu kromosom.
     * Sama jika ???????????
     * @param individu
     * @param att
     * @return 
     */
    public boolean compare(Individu individu, ArrayList<Attribute> att){
        for(int i=0; i<att.size(); ++i){
            int part1Int = att.get(i).getPartInt(this);
            int part2Int = att.get(i).getPartInt(individu);
            if(part2Int>att.get(i).pilihan.length())
                return true; //tidak termasuk pilihan yang ada --> don't care
            else return (part1Int == part2Int);
        }
        return false;
    }
    
    /**
     * Mengeset bit dari pasangan ke bit dalam individu, dari bit index awal ke indeex akhir.
     * @param pasangan
     * @param awal
     * @param akhir 
     */
    public void set(Individu pasangan, int awal, int akhir){
        for(int i=awal; i<=akhir; ++i)
            this.set(i,pasangan.get(i));
    }

}
