/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1tat.bio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thomas
 */
public class Decode {

    private short data[];
    private int sizeSecretImage;
    private String nameSecretImage;
    private ShortPixmap sp;
    private int endHeader = 16 + 16 + 13;

    public Decode(String src) {
        try {
            sp = new ShortPixmap(src);
            System.out.println("height " + sp.height);
            System.out.println("size " + sp.size);
            System.out.println("width " + sp.width);

            this.data = new short[sp.size];

            for (int i = 0; i < sp.width; i++) {
                data[i] = (short) sp.data[i];
            }
        } catch (IOException ex) {
            Logger.getLogger(Tp1TatBio.class.getName()).log(Level.SEVERE, null, "pixmax fail");
        }
        parseHeader();
        extractFlowData();
    }

    public void parseHeader() {
        int byte1 = getByte(data[0], data[1], data[2], data[3]);
        int byte2 = getByte(data[4], data[5], data[6], data[7]);
        int byte3 = getByte(data[8], data[9], data[10], data[11]);
        int byte4 = getByte(data[12], data[13], data[14], data[15]);
        sizeSecretImage = (byte1 << 24) + (byte2 << 16) + (byte3 << 8) + byte4;
        System.out.println("size= " + sizeSecretImage);
        nameSecretImage = parseName(16, 16 + 13 * 4);

    }

    public int byteToValueMasked(short val) {
        return val & 0x3;
    }

    public String parseName(int start, int end) {
        int i = start;
        String name = "";
        while (i <= end) {
            int byte1 = getByte(data[i], data[i + 1], data[i + 2], data[i + 3]);
            char c = (char) byte1;
            name = name.concat(String.valueOf(c));
            System.out.println("carac = " + c);
            i = i + 4;
        }
        System.out.println("name" + name);
        return name;
    }

    public int getByte(short v1, short v2, short v3, short v4) {
        int vm1 = byteToValueMasked(v1);
        int vm2 = byteToValueMasked(v2);
        int vm3 = byteToValueMasked(v3);
        int vm4 = byteToValueMasked(v4);
        return (vm1 << 6) + (vm2 << 4) + (vm3 << 2) + vm4;
    }

    public void extractFlowData() {
        try {
            FileOutputStream fos = new FileOutputStream(nameSecretImage);
            for (int i = endHeader; i < sizeSecretImage*4 + endHeader; i+=4) {
                int val= getByte(data[i],data[i+1],data[i+2],data[i+3]);
//                byte b = (byte) ((val) & 0xff);
                fos.write(val);
            }
            fos.close();

        } catch (IOException ex) {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
