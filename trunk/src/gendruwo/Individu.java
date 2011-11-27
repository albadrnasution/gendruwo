/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.BitSet;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class Individu extends BitSet {
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

    public void set(Individu pasangan, int awal, int akhir){
        for(int i=awal; i<=akhir; ++i)
            this.set(i,pasangan.get(i));
    }

}
