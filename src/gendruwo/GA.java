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

    final ArrayList<Individu> training = new ArrayList<Individu>();
    ArrayList<Individu> generasi = new ArrayList<Individu>();
    ArrayList<Individu> offspring = new ArrayList<Individu>();
    ArrayList<Individu> rules = new ArrayList<Individu>();

    public GA() {
    }

    public void bacaTraining() {
        //mengisi ArrayList training dengan membaca dari file
    }

    public void firstStage() {
        RandomGendruwo fsRand = new RandomGendruwo();
        int initialPopulation = generasi.size();
        int currentPopulation = generasi.size();
        boolean doFristStage = true;

        while (doFristStage) {
            /*Selection and Crossover*/
            float marriagePercentation = fsRand.nextFloat(CONSTANT.COVER_COUPLE_MIN, CONSTANT.COVER_COUPLE_MAX);
            int marriageCouple = (int) (marriagePercentation * currentPopulation);
            for (int marr = 0; marr < marriageCouple; marr++) {
                int bridge = fsRand.nextInt(currentPopulation);
                int broom = fsRand.nextInt(currentPopulation);
                int posisi1 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                int posisi2 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                if (posisi2 > posisi1) {
                    posisi1 += posisi2;
                    posisi2 = posisi1 - posisi2;
                    posisi1 -= posisi2;
                }
                if (posisi1 < posisi2) {
                    Individu anak1 = (Individu) generasi.get(broom).clone();
                    anak1.set(generasi.get(bridge), posisi1, posisi2); //anak1: pinggiran ibu, tengah bapak
                    Individu anak2 = (Individu) generasi.get(bridge).clone();
                    anak2.set(generasi.get(broom), posisi1, posisi2); //anak2: pinggiran bapak, tengah ibu
                    /*update generasi*/
                    offspring.add(anak1);
                    offspring.add(anak2);
                }
            }

            /*Fitness Calculation*/
            int offspringSize = offspring.size();
            for (int idxOff = 0; idxOff < offspringSize; ++idxOff) {
                offspring.get(idxOff).updateFitnessIndividu(training);
            }

            /*Sort next generation by fitness value*/
            Collections.sort(offspring);

            /*Massacre parents (old generation)*/
            generasi.clear();
            /*and prepare the chosen one (the best 99,2%parent population) 
             *for next generation, kill the rest too.
             */
            int desiredPopulation = (int) ((1 - CONSTANT.DECAY_POP_RATE) * currentPopulation);
            for (int des = 0; des < desiredPopulation; ++des) {
                generasi.add(offspring.get(des));
            }
            offspring.clear();  /*Kill them all, the temporary offspring*/
            currentPopulation = desiredPopulation;

            /*Mutation*/
            int numofMutant = (int) (CONSTANT.MUTATION_RATE * generasi.size());
            for (int m = 0; m < numofMutant; ++m) {
                /*Pilih kromosom target, total target numofMutant target*/
                int target = fsRand.nextInt(currentPopulation);
                /*Iterasi tiap bit pada target, jika random < probabiliti, mutasi bit*/
                for (int posisi = 0; posisi < CONSTANT.CHROMOSOME_LEN; ++posisi) {
                    if (Math.random() <= CONSTANT.MUTATION_PROB) {
                        generasi.get(target).flip(posisi);
                    }
                }
            }

            if (currentPopulation <= initialPopulation * CONSTANT.TERMINAL_POP_FROM_INITIAL) {
                doFristStage = false;
            }
        }

    }

    /**
     * Do second stage, no i meant second stage!!!
     */
    public void secondStage() {
        //random engine
        Random rasgele = new Random();
        boolean doSecondStage = true;
        int population = generasi.size();
        int coupleCandidate = 0;
        float rationCouple = 0;
        int generationCounter = 0;
        while (doSecondStage) {
            /* Sort population based on fitness function*/
            Collections.sort(generasi);
            /* get total being crossovered individu */
            while (rationCouple < CONSTANT.COVER_RATE_MAX || rationCouple > CONSTANT.COVER_RATE_MIN) {
                rationCouple = rasgele.nextFloat();
            }
            coupleCandidate = (int) (rationCouple * population);
            /* do crossover */
            for (int onCouple = 0; onCouple < coupleCandidate / 2; ++onCouple) {
                Individu individuL = generasi.get(onCouple);
                Individu individuP = generasi.get((coupleCandidate - 1) - onCouple);
                int posisi1 = rasgele.nextInt(CONSTANT.CHROMOSOME_LEN);
                int posisi2 = rasgele.nextInt(CONSTANT.CHROMOSOME_LEN);
                if (posisi2 > posisi1) {
                    posisi1 += posisi2;
                    posisi2 = posisi1 - posisi2;
                    posisi1 -= posisi2;
                }
                if (posisi1 < posisi2) {
                    Individu anak1 = (Individu) individuL.clone();
                    anak1.set(individuP, posisi1, posisi2); //anak1: pinggiran ibu, tengah bapak
                    Individu anak2 = (Individu) individuP.clone();
                    anak2.set(individuL, posisi1, posisi2); //anak2: pinggiran bapak, tengah ibu
                    /*update generasi*/
                    offspring.add(anak1);
                    offspring.add(anak2);
                }
                /* Getting the old individu */
                for (int currOld = coupleCandidate; currOld < population; ++currOld) {
                    offspring.add(generasi.get(currOld));
                }
                /* Kill old generation */
                generasi.clear();
                /* let the young rules the world */
                for (int young = 0; young < population; ++young) {
                    generasi.add(offspring.get(young));
                }
                /* Kill the old babies */
                offspring.clear();

                /* Mutation */
                int numbMutant = (int) (CONSTANT.MUTATION_RATE * population);
                for (int iterMutant = 0; iterMutant < numbMutant; ++iterMutant) {
                    /*Select the Target*/
                    int target = rasgele.nextInt(population);
                    /*Iterasi tiap bit pada target, jika random < probabiliti, mutasi bit*/
                    for (int posisi = 0; posisi < CONSTANT.CHROMOSOME_LEN; ++posisi) {
                        if (Math.random() <= CONSTANT.MUTATION_PROB) {
                            generasi.get(target).flip(posisi);
                        }
                    }
                }
                generationCounter = generationCounter + 1;
                /* update fitness function*/
                for(int interIndividu=0 ;interIndividu<population;++interIndividu){
                    generasi.get(interIndividu).updateFitnessIndividu(training);
                }
                /* Temination state */
                if (generationCounter >= CONSTANT.MAX_GENERATION) {
                    doSecondStage = false;
                }
                /* Other termination state */
                if(fitnessPopulation()/training.size()>=CONSTANT.DESIRED_ACCURATION){
                    doSecondStage = false;
                }
            }
        }

    }

    public void doGA() {
        //melakukan serangkaian genetic algorithm hingga seleksi
        //definisi presentase --> breeding : mutasi = 15:1
        generasi = (ArrayList<Individu>) training.clone();
        Random rn15 = new Random(15);
        int todo;
        int loop = generasi.size() / 10;
        for (int gen = 0; gen < loop; ++gen) {
            todo = rn15.nextInt();
            if (todo > 0) {
                beranak();
            } else {
                mutasi();
            }
        }
        //seleksi();
    }

    private void mutasi() {
        Random rn = new Random(generasi.size() - 1);
        Random rnPos = new Random(68);
        int mutan, posisi;
        int loop = generasi.size() / 20;
        for (int i = 0; i < loop; ++i) {
            mutan = rn.nextInt();
            posisi = rnPos.nextInt();
            generasi.get(mutan).flip(posisi);
        }
    }

    private void beranak() {
        Random rn = new Random();
        int bapak, ibu, posisi1, posisi2;
        int loop = generasi.size() / 10;
        for (int i = 0; i < loop; ++i) {
            bapak = rn.nextInt(generasi.size() - 1);
            ibu = rn.nextInt(generasi.size() - 1);
            posisi1 = rn.nextInt(68);
            posisi2 = rn.nextInt(68);
            if (posisi2 > posisi1) {
                posisi1 += posisi2;
                posisi2 = posisi1 - posisi2;
                posisi1 -= posisi2;
            }
            if (posisi1 < posisi2) {
                Individu anak1 = (Individu) generasi.get(ibu).clone();
                anak1.set(generasi.get(bapak), posisi1, posisi2); //anak1: pinggiran ibu, tengah bapak
                Individu anak2 = (Individu) generasi.get(bapak).clone();
                anak2.set(generasi.get(ibu), posisi1, posisi2); //anak2: pinggiran bapak, tengah ibu

                //update generasi
                generasi.set(ibu, anak1);
                generasi.set(bapak, anak2);
            }
        }
    }

    private void seleksi() {
    }

    int fitness(Individu individu) {

        return 0;
    }

    void saveToCLP() {
    }

    /**
     * fitness ini buat menghitung kecocokan populasi dalam menyampuli data yang ada
     * @return 
     */
    public int fitnessPopulation() {
        int fit = 0;
        for (int i = 0; i < training.size(); ++i) {
            if (training.get(i).isPoke()) {
                fit = fit + 1;
                training.get(i).setPoke(false);
            }
        }
        return fit;
    }

    public static void main(String... aArgs) {
        //log("Generating 10 random integers in range 0..99.");

        //note a single Random object is reused here
        Random randomGenerator = new Random();
        for (int idx = 1; idx <= 10; ++idx) {
            int randomInt = randomGenerator.nextInt(100);
            System.out.println("Generated : " + randomInt);
        }

        Individu in = new Individu();

        //log("Done.");
    }
}
