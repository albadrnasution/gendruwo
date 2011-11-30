/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gendruwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Hendra, Albadr, Sidik
 */
public class GA {
    ArrayList<Attribute> att = new ArrayList<Attribute>();
    final ArrayList<Individu> training = new ArrayList<Individu>();
    ArrayList<Individu> generasi = new ArrayList<Individu>();
    ArrayList<Individu> offspring = new ArrayList<Individu>();

    ArrayList<Individu> rules = new ArrayList<Individu>();

    public GA(){
       
    }

    public void bacaTraining(){
        //mengisi ArrayList training dengan membaca dari file
    }
    
    public void firstStage(){
        Random fsRand = new Random();
        int initialPopulation = generasi.size();
        boolean doFristStage = true;
        
        while (doFristStage){
            int currentPopulation = generasi.size();
            /*Selection and Crossover*/
            int marriagePercentation = fsRand.nextInt();
            for (int marr=0;marr<marriagePercentation;marr++){
                int bridge = fsRand.nextInt(currentPopulation);
                int broom = fsRand.nextInt(currentPopulation);
                int posisi1 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                int posisi2 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                if(posisi2>posisi1){
                    posisi1 += posisi2;
                    posisi2 = posisi1-posisi2;
                    posisi1 -= posisi2;
                }
                if(posisi1<posisi2){
                    Individu anak1 = (Individu) generasi.get(broom).clone();
                    anak1.set(generasi.get(bridge), posisi1, posisi2); //anak1: pinggiran ibu, tengah bapak
                    Individu anak2 = (Individu) generasi.get(bridge).clone();
                    anak2.set(generasi.get(broom), posisi1,posisi2); //anak2: pinggiran bapak, tengah ibu
                    /*update generasi*/
                    offspring.add(anak1);
                    offspring.add(anak2);
                }
            }
            
            /*Fitness Calculation*/
            int offspringSize = offspring.size();
            for (int idxOff = 0; idxOff < offspringSize; ++idxOff)
                offspring.get(idxOff).updateFitnessIndividu(training);
            
            /*Sort next generation by fitness value*/
            Collections.sort(offspring);
            
            /*Massacre parents (old generation)*/
            generasi.clear();
            /*and prepare the chosen one (the best 99,2%parent population) 
             *for next generation, kill the rest too.
             */
            int desiredPopulation = (int) ((1-CONSTANT.DECAY_POP_RATE)*currentPopulation);
            for (int des=0;des<desiredPopulation;++des)
                generasi.add(offspring.get(des));
            offspring.clear();
            currentPopulation = desiredPopulation;
            
            /*Mutation*/
            int numofMutant = (int) (CONSTANT.MUTATION_RATE * generasi.size());
            for (int m=0;m<numofMutant;++m){
                /*Pilih kromosom target, total target numofMutant target*/
                int target = fsRand.nextInt(currentPopulation);
                /*Iterasi tiap bit pada target, jika random < probabiliti, mutasi bit*/
                for (int posisi=0;posisi<CONSTANT.CHROMOSOME_LEN;++posisi){
                    if (Math.random()<=CONSTANT.MUTATION_PROB){
                        generasi.get(target).flip(posisi);
                    }
                }
            }
            
            if (currentPopulation <= initialPopulation*CONSTANT.TERMINAL_POP_FROM_INITIAL){
                doFristStage = false;
            }
        }
        
    }
    

    
    public void secondStage(){
        
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
    
    public static final void main(String... aArgs){
    //log("Generating 10 random integers in range 0..99.");
    
    //note a single Random object is reused here
    Random randomGenerator = new Random();
    for (int idx = 1; idx <= 10; ++idx){
      int randomInt = randomGenerator.nextInt(100);
        System.out.println("Generated : " + randomInt);
    }
    
    //log("Done.");
  }
}
