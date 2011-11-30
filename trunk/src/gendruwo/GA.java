/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendruwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public GA() {
    }

    public void bacaTraining(String file_addr) {
        //mengisi ArrayList training dengan membaca dari file
        File file = new File(file_addr);
        BufferedReader reader = null;
        String data = null;

        //generate char to bitset mapping
        ArrayList<Map<String, Integer>> mapper = new ArrayList<Map<String, Integer>>();
        for (int i = 0; i < Individu.attributes.size(); ++i) {
            //iterasi semua atribut
            Map<String, Integer> m = new HashMap<String, Integer>();

            String pil = Individu.attributes.get(i).pilihan;
            for (int en = 0; en < pil.length(); ++en) {
                //memetakan semua pilihan yang ada
                m.put(pil.substring(en, en + 1), en);
            }
            mapper.add(m);
        }

        try {
            // repeat until all lines is read
            reader = new BufferedReader(new FileReader(file));
            int pointer = 0, code;
            String att;
            while ((data = reader.readLine()) != null) {
                //parsing sebuah baris menjadi sebuah data training
                Individu baru = new Individu(69);
                pointer=0;
                for (int i = 0; i < Individu.attributes.size(); ++i) {
                    att = data.charAt(pointer) + "";
                    //System.out.println(i+" -> "+att);
                    code = mapper.get(i).get(att);
                    for (int bit = Individu.attributes.get(i).iAwal; bit <= Individu.attributes.get(i).iAkhir; ++bit) {
                        //mengisi individu
                        if (code%2 == 0) {
                            baru.set(bit, false);
                        }else baru.set(bit, true);
                        code = code >> 1;
                    }
                    pointer += 2;
                }
                training.add(baru);
                baru.print(); System.out.println();
            }
        } catch (IOException ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }
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

            
            /*Stopper*/
            if (currentPopulation <= initialPopulation * CONSTANT.TERMINAL_POP_FROM_INITIAL) {
                doFristStage = false;
            }
        }

    }

    /**
     * Do second stage, no i meant second stage!!!
     */
    public void secondStage() {
        /* random engine */
        Random rasgele = new Random();
        boolean doSecondStage = true; /* order second stage */
        int population = generasi.size(); /* number of population */
        int individuCandidate = 0; /*candidate Couple */
        float rationCouple = 0; /* ratio Couple */
        int generationCounter = 0; /* number of generation */
        while (doSecondStage) {
            /* Sort population based on fitness function*/
            Collections.sort(generasi);
            /* get total being crossovered individu */
            while (rationCouple < CONSTANT.COVER_RATE_MAX || rationCouple > CONSTANT.COVER_RATE_MIN) {
                rationCouple = rasgele.nextFloat();
            }
            individuCandidate = (int) (rationCouple * population);
            /* Sum total fitness function from whole generation */
            int totalFitness = 0;
            for (int interRoulet = 0; interRoulet < population; ++interRoulet) {
                totalFitness = totalFitness + generasi.get(interRoulet).getFitnessValue();
            }
            /* Let's have with roulette */
            float[] roulette = new float[individuCandidate];
            for (int interRoulet = 0; interRoulet < individuCandidate; ++interRoulet) {
                roulette[interRoulet] = generasi.get(interRoulet).getFitnessValue() / totalFitness;
            }
            /* do crossover */
            for (int onCouple = 0; onCouple < individuCandidate / 2; ++onCouple) {
                
                /* Let's find the individu candidates */
                boolean foundCandidate = false;
                int FirstCouple = 0;
                int secondCouple = 0;
                while (!foundCandidate) {
                    float candidate = rasgele.nextFloat();
                    /* selecting the proposal for being a couple */
                    for (int y = 0; y < roulette.length; ++y) {
                        if (candidate < roulette[y]) {
                            foundCandidate = true;
                            FirstCouple = y;
                            roulette[y] = 0;
                        }
                    }
                }
                foundCandidate = false;
                while (!foundCandidate) {
                    float candidate = rasgele.nextFloat();
                    /* selecting the proposal for being a couple */
                    for (int y = 0; y < roulette.length; ++y) {
                        if (candidate < roulette[y]) {
                            foundCandidate = true;
                            secondCouple = y;
                            roulette[y] = 0;
                        }
                    }
                }
                /* if you want to match the good one with the bad one */
//                Individu individuL = generasi.get(onCouple);
//                Individu individuP = generasi.get((individuCandidate - 1) - onCouple);
                
                Individu individuL = generasi.get(FirstCouple);
                Individu individuP = generasi.get(secondCouple);
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
                for (int currOld = individuCandidate; currOld < population; ++currOld) {
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
                for (int interIndividu = 0; interIndividu < population; ++interIndividu) {
                    generasi.get(interIndividu).updateFitnessIndividu(training);
                }
                /* Temination state */
                if (generationCounter >= CONSTANT.MAX_GENERATION) {
                    doSecondStage = false;
                }
                /* Other termination state */
                if (fitnessPopulation() / training.size() >= CONSTANT.DESIRED_ACCURATION) {
                    doSecondStage = false;
                }
            }
        }

    }

    public void doGA() {
        /* don't have planning to do GA */
        firstStage();
        secondStage();
        /* Clear container */
        rules.clear();
        /* Fill Container */
        for (int iterCopier = 0; iterCopier < generasi.size(); ++iterCopier) {
            rules.add(generasi.get(iterCopier));
        }
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
    }
}