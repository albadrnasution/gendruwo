/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gendruwo;

/**
 *
 * @author Albadr
 */
public class CONSTANT {
    /*Pembatasan maksimum generasi yang mungkin*/
    public static final int MAX_GENERATION = 888;
    
    /*Persentasi populasi yang dimutasi*/
    public static final float MUTATION_RATE = 0.8f;
    
    /*Peluang sebuah bit itu dimutasi/diflip atau tidak*/
    public static final float MUTATION_PROB = 0.8f;
    
    /*Penurunan populasi dari jumlah data set sampai jumlah terminal*/
    public static final float DECAY_POP_RATE = 0.008f;
    
    /*Jumlah terminal populasi dibanding jumlah awal populasi*/
    public static final float TERMINAL_POP_FROM_INITIAL = 0.08f;
    
    /*Peluang minimum dan maksimum sebuah individu/kromosom terambil untuk cross-over*/
    public static final float COVER_RATE_MIN = 0.68f;
    public static final float COVER_RATE_MAX = 0.86f;
    
    /*Presentasi jumlah PASANGAN dari jumlah populasi yang mungkin saat menaik-turunkan populasi*/
    public static final float COVER_COUPLE_MIN = 0.48f;
    public static final float COVER_COUPLE_MAX = 0.84f;
    
    /*Besar persentasu akurasi yang diinginkan*/
    public static float DESIRED_ACCURATION = 0.8f;
}
