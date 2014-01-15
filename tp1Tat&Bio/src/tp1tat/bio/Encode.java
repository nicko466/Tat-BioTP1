package tp1tat.bio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private short sizeHeader = 0;
    private final short StartFlow = 0;
    private ShortPixmap sp;
    private File fileToHide;

    public Encode(String pathBaseImage, String pathData2mask, String pathOut) {
        try {
            this.pathBaseImage = pathBaseImage;
            this.pathData2mask = "tatoumina1.jpg";
            this.pathOut = pathOut;
            this.sp = new ShortPixmap(pathBaseImage);

            fileToHide = new File(pathData2mask);
            long size2mask = fileToHide.length();
            System.out.println(" taille ficher a masqué " + size2mask);
            sizeHeader = (short) (16 + pathOut.length() * Character.SIZE);
            encodeHeader(size2mask, this.pathData2mask);
            encodeData();
        } catch (IOException ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, "fail lecture base image", ex);
        }
    }

    public void encodeHeader(long size2mask, String name) {
        int size = (int) size2mask;

        System.out.println(" taille size2mask en long " + size2mask + " taille size2mask en int " + (short) ((int) size2mask));
        encodeByte(0, (int) size2mask);
        encodeByte(12, size);
        encodeByte(8, size >> 8);
        encodeByte(4, size >> 16);
        encodeByte(0, size >> 24);

        for (int i = 0; i < name.length(); i++) {
            encodeByte(16 + i * 4, (short) name.charAt(i));
        }

    }

    public void encodeData() {
        FileInputStream fileToHideInputStream = null;

        try {
            fileToHideInputStream = new FileInputStream(fileToHide);
            int content;
            while ((content = fileToHideInputStream.read()) != -1) {
                encodeByte(sizeHeader, (short)content);
            }
            //export data
            sp.write(pathOut);
            fileToHideInputStream.close();
        } catch (Exception ex) {
            Logger.getLogger(Encode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void encodeByte(int indexToWrite, short valueToEncode) {
        for (int i = indexToWrite; i < indexToWrite + 4; i++) {
            sp.data[i] = (short) (sp.data[i] >> 2);
            sp.data[i] = (short) (sp.data[i] << 2);
        }
        sp.data[indexToWrite + 3] += valueToEncode & 0b00000011;
        sp.data[indexToWrite + 2] += (valueToEncode & 0b00001100) >> 2;
        sp.data[indexToWrite + 1] += (valueToEncode & 0b00110000) >> 4;
        sp.data[indexToWrite] += (valueToEncode & 0b11000000) >> 6;
    }

    public void encodeByte(int indexToWrite, int valueToEncode) {
        for (int i = indexToWrite; i < indexToWrite + 4; i++) {
            sp.data[i] = (short) (sp.data[i] >> 2);
            sp.data[i] = (short) (sp.data[i] << 2);
        }
        sp.data[indexToWrite + 3] += valueToEncode & 0b00000011;
        sp.data[indexToWrite + 2] += (valueToEncode & 0b00001100) >> 2;
        sp.data[indexToWrite + 1] += (valueToEncode & 0b00110000) >> 4;
        sp.data[indexToWrite] += (valueToEncode & 0b11000000) >> 6;
    }
}
