/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendruwo;

import java.util.ArrayList;
import java.util.Collections;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
            String att = new String();
            while ((data = reader.readLine()) != null) {
                //parsing sebuah baris menjadi sebuah data training
                Individu baru = new Individu(69);
                pointer = 0;
                for (int i = 0; i < Individu.attributes.size(); ++i) {
                    att = data.charAt(pointer) + "";
                    //System.out.println(i+" -> "+att);
                    code = mapper.get(i).get(att);
                    for (int bit = Individu.attributes.get(i).iAwal; bit <= Individu.attributes.get(i).iAkhir; ++bit) {
                        //mengisi individu
                        if (code % 2 == 0) {
                            baru.set(bit, false);
                        } else {
                            baru.set(bit, true);
                        }
                        code = code >> 1;
                    }
                    pointer += 2;
                }
                training.add(baru);
                generasi.add(baru);
            }
            System.out.println("Pembacaan file selesai, diperoleh " + training.size() + "data training");
        } catch (IOException ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void firstStage() {
        RandomGendruwo fsRand = new RandomGendruwo();
        int initialPopulation = generasi.size();
        int currentPopulation = generasi.size();
        boolean doFristStage = true;
        int numgenerasi = 0;
        while (doFristStage) {
            /*Prepare by Fitness Calculation*/
            currentPopulation = generasi.size();
            for (int idxOff = 0; idxOff < currentPopulation; ++idxOff) {
                generasi.get(idxOff).updateFitnessIndividu(training);
            }
            Collections.sort(generasi);
            System.out.println("FV[0]="+generasi.get(0).fitnessValue);
            System.out.println("FV[N]="+generasi.get(generasi.size()-1).fitnessValue);
            int fitnessPop = fitnessPopulation() ;
            float accuracy = (float)fitnessPop / training.size();
            System.out.println("Generasi ke:" + numgenerasi +", popul="+generasi.size()+", acc = "+accuracy);
            
            /*Selection and Crossover*/
            float marriagePercentation = fsRand.nextFloat(CONSTANT.COVER_COUPLE_MIN, CONSTANT.COVER_COUPLE_MAX);
            int marriageCouple = (int) (marriagePercentation * currentPopulation);
            for (int marr = 0; marr < marriageCouple; marr++) {
                int bridge = fsRand.nextInt(generasi.size());
                int broom = fsRand.nextInt(generasi.size());
                int posisi1 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                int posisi2 = fsRand.nextInt(CONSTANT.CHROMOSOME_LEN);
                if (posisi2 < posisi1) {
                    posisi1 += posisi2;
                    posisi2 = posisi1 - posisi2;
                    posisi1 -= posisi2;
                }
                if (posisi1 < posisi2) {
//                    Individu groom = (Individu) generasi.get(broom).clone();
//                    Individu bride = (Individu) generasi.get(bridge).clone();
//                    offspring.add(groom);
//                    offspring.add(bride);
                    Individu anak1 = (Individu) generasi.get(broom).clone();
                    Individu anak2 = (Individu) generasi.get(bridge).clone();
                    anak1.set(generasi.get(bridge), posisi1, posisi2); //anak1: pinggiran ibu, tengah bapak
                    anak2.set(generasi.get(broom), posisi1, posisi2); //anak2: pinggiran bapak, tengah ibu
                    /*update generasi*/
                    generasi.add(anak1);
                    generasi.add(anak2);
                }
            }

            /*Fitness Calculation*/
            int offspringSize = generasi.size();
            for (int idxOff = 0; idxOff < offspringSize; ++idxOff) {
                generasi.get(idxOff).updateFitnessIndividu(training);
            }
            fitnessPop = fitnessPopulation() ;
            accuracy = (float)fitnessPop / training.size();
            System.out.println("Generasi ke:" + numgenerasi +", popul="+generasi.size()+", acc = "+accuracy);

            /*Sort next generation by fitness value*/
            Collections.sort(generasi);
            /*Massacre parents (old generation)*/
            offspring.addAll(generasi);
            generasi.clear();
            /*and prepare the chosen one (the best 99,2%parent population) 
             *for next generation, kill the rest too.
             */
            int desiredPopulation = (int) ((1 - CONSTANT.DECAY_POP_RATE) * currentPopulation);
            for (int des = 0; des < desiredPopulation; ++des) {
                generasi.add(offspring.get(des));
            }
            offspring.clear();  /*Kill them all, the temporary offspring*/
            currentPopulation = generasi.size();

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
            numgenerasi++;
        }
    }

    /**
     * Do second stage, no i meant second stage!!!
     */
    public void secondStage() {
        /* random engine */
        RandomGendruwo rasgele = new RandomGendruwo();
        boolean doSecondStage = true; /* order second stage */
        int population = generasi.size(); /* number of population */
        int individuCandidate = 0; /*candidate Couple */
        float rationCouple = 0; /* ratio Couple */
        int generationCounter = 0; /* number of generation */
        while (doSecondStage) {
            /* Sort population based on fitness function*/
            Collections.sort(generasi);
            /* get total being crossovered individu */
            rationCouple = rasgele.nextFloat(CONSTANT.COVER_RATE_MIN, CONSTANT.COVER_RATE_MAX);
            individuCandidate = (int) (rationCouple * population);
            /* Sum total fitness function from whole generation */
            int totalFitness = 0;
            for (int interRoulet = 0; interRoulet < population; ++interRoulet) {
                totalFitness = totalFitness + generasi.get(interRoulet).getFitnessValue();
            }
            /* Let's have with roulette */
            float[] roulette = new float[individuCandidate];

            if (totalFitness != 0) {
                for (int interRoulet = 0; interRoulet < individuCandidate; ++interRoulet) {
                    roulette[interRoulet] = interRoulet ==0 ? (float)generasi.get(interRoulet).getFitnessValue() / totalFitness:
                            roulette[interRoulet-1] + (float)generasi.get(interRoulet).getFitnessValue() / totalFitness;
                }
            }

            /* do crossover */
            for (int onCouple = 0; onCouple < individuCandidate / 2; ++onCouple) {
                /* Let's find the individu candidates */
                boolean foundCandidate = false;
                int FirstCouple = 0;
                int secondCouple = 0;

                if (totalFitness != 0) {
                    /* if have roulette */
                    /* selecting the proposal for being a couple */
                    for (int y = 0; y < roulette.length&&!foundCandidate; ++y) {
                    float candidate = rasgele.nextFloat();
                        if (candidate < roulette[y]) {
                            foundCandidate = true;
                            FirstCouple = y;
                            roulette[y] = 0;
                        }
                    }
                        
                    foundCandidate = false;
                    
                    /* selecting the proposal for being a couple */
                    for (int y = 0; y < roulette.length&&!foundCandidate; ++y) {
                        float candidate = rasgele.nextFloat();

                        if (candidate < roulette[y]) {
                            foundCandidate = true;
                            secondCouple = y;
                            roulette[y] = 0;
                        }
                    }
                } else {
                    foundCandidate = false;
                    while (!foundCandidate) {
                        int candidate = rasgele.nextInt(population);
                        /* selecting the proposal for being a couple */
                        if (!generasi.get(candidate).isMarriage) {
                            foundCandidate = true;
                            FirstCouple = candidate;
                            generasi.get(candidate).isMarriage = true;
                        }

                    }

                    foundCandidate = false;
                    while (!foundCandidate) {
                        int candidate = rasgele.nextInt(population);
                        /* selecting the proposal for being a couple */
                        if (!generasi.get(candidate).isMarriage) {

                            foundCandidate = true;
                            secondCouple = candidate;
                            generasi.get(candidate).isMarriage = true;
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
                if (posisi2 < posisi1) {
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
            }

            /* Getting the old individu */
            for (int currOld = offspring.size(); currOld < population; ++currOld) {
                offspring.add(generasi.get(currOld));
            }
            /* Kill old generation */
            generasi.clear();
            /* let the young rules the world */
            for (int young = 0; young < population; ++young) {
                offspring.get(young).isMarriage = false;
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
            /* Other termination state */
            int fitnessPop = fitnessPopulation() ;
            float accuracy = (float)fitnessPop / training.size();
            System.out.println("Generasi ke:" + generationCounter+", popul="+generasi.size()+", acc = "+accuracy);
            if (accuracy >= CONSTANT.DESIRED_ACCURATION) {
                doSecondStage = false;
                System.out.println("Congratulation, you reach accuracy " + accuracy);
            }
            /* Temination state */
            if (generationCounter >= CONSTANT.MAX_GENERATION) {
                doSecondStage = false;
                System.out.println("MAX GENERATION REACHED, accuracy " + accuracy);
            }
        }

    }

    public void doGA() {
        /* don't have planning to do GA */
        firstStage();
        System.out.println("Finised First Stage");
        secondStage();
        System.out.println("Finised Second Stage");
        /* Clear container */
        rules.clear();
        /* Fill Container */
        for (int iterCopier = 0; iterCopier < generasi.size(); ++iterCopier) {
            rules.add(generasi.get(iterCopier));
        }
        Collections.sort(rules);
    }

    void saveToCLP(String clp_loc) {
        //Menulis CLP
        //rules = training;

        try {
            File f = new File(clp_loc);
            if (!f.exists()) {
                f.createNewFile(); //membuat file baru jika belum ada
                //Menulis CLP
            }
            if (f.canWrite()) {
                PrintWriter simpan = new PrintWriter(f);

                //Interview ...

                for (int i = 0; i < rules.size(); ++i) {
                    simpan.println("(defrule rule" + i);
                    simpan.println("   (not (solution found))");
                    //ambil nilai semua atribut yang tidak don't care sebagai LHS
                    int code;
                    for (int a = 1; a < Individu.attributes.size(); ++a) {
                        code = 0;
                        //ekstrak sebuah atribut
                        for (int bit = Individu.attributes.get(a).iAkhir; bit >= Individu.attributes.get(a).iAwal; --bit) {
                            code = code << 1;
                            if (rules.get(i).get(bit)) {
                                code += 1;
                            }
                        }
                        if(code<Individu.attributes.get(a).pilihan.length()) simpan.println("   ("+Individu.attributes.get(a).nama +" "+ code + ")");
                    }
                    simpan.println("   =>");
                    //atribut ke-0 adalah kelas, menjadi RHS
                    if (rules.get(i).get(0)) {
                        simpan.println("   (assert (" + Individu.attributes.get(0).nama + "  poisonous))");
                    } else {
                        simpan.println("   (assert (" + Individu.attributes.get(0).nama + "  edible))");
                    }

                    //memberi mark bahwa solusi sudah ditemukan
                    simpan.println("   (assert (solution found))\n)");
                    simpan.println();
                }

                //membuat rule default
                simpan.println("(defrule default");
                simpan.println("   (not (solution found))");
                simpan.println("   =>");
                simpan.println("   (assert (edibility poisonous))");
                simpan.println("   (assert (solution found))\n)");

                //fungsi untuk menampilkan hasil

                simpan.println("(defrule poisonous");
                simpan.println("   (solution found)");
                simpan.println("   (edibility poisonous)");
                simpan.println("   =>");
                simpan.println("   (printout t \"p\" crlf)");
                simpan.println(")");
                simpan.println();
                simpan.println("(defrule edible");
                simpan.println("   (solution found)");
                simpan.println("   (edibility edible)");
                simpan.println("   =>");
                simpan.println("   (printout t \"e\" crlf)");
                simpan.println(")");

                simpan.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(GA.class.getName()).log(Level.SEVERE, null, ex);
        }
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
//        Random randomGenerator = new Random();
//        for (int idx = 1; idx <= 10; ++idx) {
//            int randomInt = randomGenerator.nextInt(100);
//            System.out.println("Generated : " + randomInt);
//        }
        /* Trying to GA */
        GA putri = new GA();
        putri.bacaTraining("agaricus-lepiota-varis.data");
        putri.doGA();
        for (int i = 0; i < putri.rules.size(); ++i) {
            putri.rules.get(i).print();
        }
        putri.saveToCLP("rule_new_acc.clp");
        System.out.println("selesai......");
    }
}
