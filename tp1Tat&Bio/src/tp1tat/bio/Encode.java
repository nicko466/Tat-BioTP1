package tp1tat.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thomas
 */
public class Encode {

    private String pathBaseImage;
    private String pathData2mask;
    private String pathOut;
    private ShortPixmap sp;
    private File fileToHide;
    private String nameFileToMask;

    /**
     * Constructeur de l'encodeur Encodage fait lors de cette construction
     *
     * @param pathBaseImage path de l'image dans laquelle on va cacher les
     * données
     * @param pathData2mask path de l'image qui va être cachée
     * @param pathOut path de l'image de sortie
     */
    public Encode(String pathBaseImage, String pathData2mask, String pathOut) {
        try {
            this.pathBaseImage = pathBaseImage;
            this.pathData2mask = pathData2mask;
            this.nameFileToMask = "tatoumina1.jpg";
            this.pathOut = pathOut;
            this.sp = new ShortPixmap(pathBaseImage);

            //permet de créer le fichier pathOut s'il n'existe pas auparavant
            new File(pathOut);

            fileToHide = new File(pathData2mask);
            long size2mask = fileToHide.length();
            System.out.println("Taille ficher à masquer " + size2mask);
            encodeHeader(size2mask, this.nameFileToMask);
            encodeData();
        } catch (IOException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, "fail lecture base image", ex);
        }
    }

    /**
     * Encode le header de l'image cachée : taille du contenu + nom
     *
     * @param size2mask taille de l'image qui est cachée
     * @param name nom de l'image qui est cachée
     */
    public void encodeHeader(long size2mask, String name) {
        int size = (int) size2mask;

//        System.out.println("Taille size2mask en long " + size2mask + " taille size2mask en int " + (short) ((int) size2mask));
        encodeByte(0, (int) size2mask);
        encodeByte(12, size);
        encodeByte(8, size >> 8);
        encodeByte(4, size >> 16);
        encodeByte(0, size >> 24);
        for (int i = 0; i < name.length(); i++) {
            encodeByte(16 + i * 4, (short) name.charAt(i));
        }

    }

    /**
     * Encode le contenu de l'image
     */
    public void encodeData() {
        FileInputStream fileToHideInputStream = null;
        try {
            fileToHideInputStream = new FileInputStream(fileToHide);
            int content;
            //On commence à mettre le contenu à partir de l'octet 144
            int i = 144;
            while ((content = fileToHideInputStream.read()) != -1) {
                encodeByte(i, (short) content);
                i = i + 4;
            }
            //export data
            sp.write(pathOut);
            fileToHideInputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Encode une valeur à un endroit donné
     *
     * @param indexToWrite index à partir duquel on va caché une valeur
     * @param valueToEncode valeur à cacher
     */
    public void encodeByte(int indexToWrite, short valueToEncode) {
        //On met les 2 bits de poids faibles de 4 octets à 0
        for (int i = indexToWrite; i < indexToWrite + 4; i++) {
            sp.data[i] = (short) (sp.data[i] >> 2);
            sp.data[i] = (short) (sp.data[i] << 2);
        }
        //On encode pour chaque octet deux bits de la valeur, en fonction de l'octet le masque à appliquer change
        sp.data[indexToWrite + 3] += valueToEncode & 0b00000011;
        sp.data[indexToWrite + 2] += (valueToEncode & 0b00001100) >> 2;
        sp.data[indexToWrite + 1] += (valueToEncode & 0b00110000) >> 4;
        sp.data[indexToWrite] += (valueToEncode & 0b11000000) >> 6;
    }

    /**
     * Encode une valeur à un endroit donné
     *
     * @param indexToWrite index à partir duquel on va caché une valeur
     * @param valueToEncode valeur à cacher
     */
    public void encodeByte(int indexToWrite, int valueToEncode) {
        //On met les 2 bits de poids faibles de 4 octets à 0
        for (int i = indexToWrite; i < indexToWrite + 4; i++) {
            sp.data[i] = (short) (sp.data[i] >> 2);
            sp.data[i] = (short) (sp.data[i] << 2);
        }
        //On encode pour chaque octet deux bits de la valeur, en fonction de l'octet le masque à appliquer change
        sp.data[indexToWrite + 3] += valueToEncode & 0b00000011;
        sp.data[indexToWrite + 2] += (valueToEncode & 0b00001100) >> 2;
        sp.data[indexToWrite + 1] += (valueToEncode & 0b00110000) >> 4;
        sp.data[indexToWrite] += (valueToEncode & 0b11000000) >> 6;
    }
}
