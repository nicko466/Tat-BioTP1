/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1tat.bio;


/**
 *
 * @author nicko
 */
public class Tp1TatBio {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Encode encode = new Encode("Lena.pgm","../tatouminaOrig.jpg","Sortie.pgm");
        Decode decode = new Decode("Sortie.pgm");
    }
}
