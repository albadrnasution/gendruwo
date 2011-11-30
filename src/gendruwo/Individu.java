/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendruwo;

import java.util.BitSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Hendra
 */
public class Individu extends BitSet implements Comparable {
    
    int fitnessValue = 0;
    Individu(){

    }
    //konstruktor, membuat individu 69 bit
    Individu(int n){
        super(n);
    }
    
    void print(){
        for(int i=0; i<this.length(); i++){
            if (this.get(i)) System.out.print(1);
            else System.out.print(0);
        }
    }

    //list atribut yang dimiliki oleh individu
    //ceritanya teh, individu punya atribut 
    //poke ini itu kalau ada yang rule training yang cocok akan diset true
    private boolean poke = false;
    
    static final ArrayList<Attribute> attributes = new ArrayList<Attribute>(Arrays.asList(
            new Attribute("edibility", 0, 0, "ep"),
            new Attribute("cap-shape", 1, 3, "bcxfks"),
            new Attribute("cap-surface", 4, 6, "fgys"),
            new Attribute("cap-color", 7, 10, "nbcgrpuewy"),
            new Attribute("bruises", 11, 12, "tf"),
            new Attribute("odor", 13, 16, "alcyfmnps"),
            new Attribute("gill-attachment", 17, 19, "adfn"),
            new Attribute("gill-spacing", 20, 21, "cwd"),
            new Attribute("gill-size", 22, 23, "bn"),
            new Attribute("gill-color", 24, 27, "knbhgropuewy"),
            new Attribute("stalk-shape", 28, 30, "et"),
            new Attribute("stalk-root", 31, 33, "bcuezr?"),
            new Attribute("stalk-surface-above-ring", 34, 36, "fyks"),
            new Attribute("stalk-surface-below-ring", 37, 39, "fyks"),
            new Attribute("stalk-color-above-ring", 40, 43, "nbcgopewy"),
            new Attribute("stalk-color-below-ring", 44, 47, "nbcgopewy"),
            new Attribute("veil-type", 48, 49, "pu"),
            new Attribute("veil-color", 50, 52, "nowy"),
            new Attribute("ring-number", 53, 54, "not"),
            new Attribute("ring-type", 55, 58, "ceflnpsz"),
            new Attribute("spore-print-color", 59, 62, "knbhrouwy"),
            new Attribute("population", 63, 65, "acnsvy"),
            new Attribute("habitat", 66, 68, "glmpuwd")));

    /**
     * Membandingkan dua individu kromosom.
     * Individu yang manggil adalah individu bakal rules aka yang masih abu-abu
     * Sama jika 
     * @param individu
     * @param att
     * @return 
     */
    public boolean compare(Individu individuTraining) {
        boolean same = true;
        for (int i = 0; i < attributes.size() && same; ++i) {
            int part1Int = attributes.get(i).getPartInt(this);
            int part2Int = attributes.get(i).getPartInt(individuTraining);
            if (part2Int > attributes.get(i).pilihan.length()) {
                same= true; //tidak termasuk pilihan yang ada --> don't care
            } else {
                same=(part1Int == part2Int);
            }
        }
        if (!individuTraining.isPoke())
            individuTraining.setPoke(same);
        return same;
    }
    
    /**
     * Berdasarkan list data training yang dimasukkan, menghitung berapa banyak
     * rules yang ada di individu ini cocok dengan data training.
     * Jumlah kecocokan disimpan dalam fitnessValue.
     * @param listTraining 
     */
    public void updateFitnessIndividu(List<Individu> listTraining){
        int nilai=0;
        
        for (Individu train : listTraining){
            if (this.compare(train))
                nilai++;
        }
        
        this.fitnessValue = nilai;
    }
    
    /**
     * Mengembalikan fitnessValue yang ada.
     * @return fitnessValue sekarang
     */
    public int getFitnessValue(){
        return fitnessValue;
    }

    /**
     * Mengeset bit dari pasangan ke bit dalam individu, dari bit index awal ke indeex akhir.
     * @param pasangan
     * @param awal
     * @param akhir 
     */
    public void set(Individu pasangan, int awal, int akhir) {
        for (int i = awal; i <= akhir; ++i) {
            this.set(i, pasangan.get(i));
        }
    }

    /**
     * @return the poke
     */
    public boolean isPoke() {
        return poke;
    }

    /**
     * @param poke the poke to set
     */
    public void setPoke(boolean poke) {
        this.poke = poke;
    }
    
    /**
     * Urutin pake Collections.sort()
     * Urutan hasil adalah descending berdasarkan fitnessValue.
     * Paling hebat ada di nomor NOL.
     * 
     * @param anotherIndividu
     * @return
     * @throws ClassCastException 
     */
    public int compareTo(Object anotherIndividu) throws ClassCastException {
        if (!(anotherIndividu instanceof Individu)) {
            throw new ClassCastException("A Individu object expected.");
        }
        int anotherFitnessValue = ((Individu) anotherIndividu).getFitnessValue();
        return anotherFitnessValue - this.getFitnessValue();
    }
    
    public static void main(String args[]){
        ArrayList<Individu> generasi = new ArrayList<Individu>();
        RandomGendruwo rand = new RandomGendruwo();
        
        for (int i=0;i<=10;i++){
            Individu iind = new Individu();
            iind.fitnessValue = rand.nextInt(0, 8000);
            generasi.add(iind);
        }
        
        System.out.println("============BEFORE SORT===============");
        for (Individu e : generasi){
            System.out.println(e.fitnessValue);
        }
        
        Collections.sort(generasi);
        System.out.println("=============AFTER SORT===============");
        for (Individu e : generasi){
            System.out.println(e.fitnessValue);
        }
    }
}
