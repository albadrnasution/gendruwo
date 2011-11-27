/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author User
 */
public class GA {
    ArrayList<Attribute> att = new ArrayList<Attribute>();
    final ArrayList<Individu> training = new ArrayList<Individu>();
    ArrayList<Individu> generasi = new ArrayList<Individu>();

    ArrayList<Individu> rules = new ArrayList<Individu>();

    public GA(){
        //ngisi atribut
        att.add(new Attribute("edibility",0,0,"ep"));
        att.add(new Attribute("cap-shape",1,3,"bcxfks"));
        att.add(new Attribute("cap-surface",4,6,"fgys"));
        att.add(new Attribute("cap-color",7,10,"nbcgrpuewy"));
        att.add(new Attribute("bruises",11,12,"tf"));
        att.add(new Attribute("odor",13,16,"alcyfmnps"));
        att.add(new Attribute("gill-attachment",17,19,"adfn"));
        att.add(new Attribute("gill-spacing",20,21,"cwd"));
        att.add(new Attribute("gill-size",22,23,"bn"));
        att.add(new Attribute("gill-color",24,27,"knbhgropuewy"));
        att.add(new Attribute("stalk-shape",28,30,"et"));
        att.add(new Attribute("stalk-root",31,33,"bcuezr?"));
        att.add(new Attribute("stalk-surface-above-ring",34,36,"fyks"));
        att.add(new Attribute("stalk-surface-below-ring",37,39,"fyks"));
        att.add(new Attribute("stalk-color-above-ring",40,43,"nbcgopewy"));
        att.add(new Attribute("stalk-color-below-ring",44,47,"nbcgopewy"));
        att.add(new Attribute("veil-type",48,49,"pu"));
        att.add(new Attribute("veil-color",50,52,"nowy"));
        att.add(new Attribute("ring-number",53,54,"not"));
        att.add(new Attribute("ring-type",55,58,"ceflnpsz"));
        att.add(new Attribute("spore-print-color",59,62,"knbhrouwy"));
        att.add(new Attribute("population",63,65,"acnsvy"));
        att.add(new Attribute("habitat",66,68,"glmpuwd"));
        
    }

    public void bacaTraining(){
        //mengisi ArrayList training dengan membaca dari file
    }

    public void doGA(){
        //melakukan serangkaian genetic algorithm hingga seleksi
        //definisi presentase --> breeding : mutasi = 15:1
        generasi = (ArrayList<Individu>) training.clone();
        Random rn15 = new Random(15);
        int todo;
        int loop = generasi.size()/10;
        for(int gen=0; gen<loop; ++gen){
            todo = rn15.nextInt();
            if(todo>0) beranak();
            else mutasi();
        }
        seleksi();
    }

    private void mutasi(){
        Random rn = new Random(generasi.size()-1);
        Random rnPos = new Random(68);
        int mutan, posisi;
        int loop = generasi.size()/20;
        for(int i=0; i<loop; ++i){
            mutan = rn.nextInt();
            posisi = rnPos.nextInt();
            generasi.get(mutan).flip(posisi);
        }
    }

    private void beranak(){
        Random rn = new Random(generasi.size()-1);
        Random rnPos = new Random(68);
        int bapak, ibu, posisi1, posisi2;
        int loop = generasi.size()/10;
        for(int i=0; i<loop; ++i){
            bapak = rn.nextInt();
            ibu = rn.nextInt();
            posisi1 = rnPos.nextInt();
            posisi2 = rnPos.nextInt();
            if(posisi2>posisi1){
                posisi1 += posisi2;
                posisi2 = posisi1-posisi2;
                posisi1 -= posisi2;
            }
            if(posisi1<posisi2){
                Individu anak1 = (Individu) generasi.get(ibu).clone();
                anak1.set(generasi.get(bapak), posisi1,posisi2); //anak1: pinggiran ibu, tengah bapak
                Individu anak2 = (Individu) generasi.get(bapak).clone();
                anak2.set(generasi.get(ibu), posisi1,posisi2); //anak2: pinggiran bapak, tengah ibu

                //update generasi
                generasi.set(ibu, anak1);
                generasi.set(bapak, anak2);
            }
        }
    }


    private void seleksi(){

    }

    int fitness(Individu individu){

        return 0;
    }
    void saveToCLP(){
        
    }
}
