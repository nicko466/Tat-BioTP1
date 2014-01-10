/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1tat.bio;

import java.io.*;
import java.awt.*;

public class ShortPixmap extends Pixmap {

    public final short[] data;

    public ShortPixmap(int w, int h, short[] pixels) throws IllegalArgumentException {
        super(w, h);
        if (pixels == null) {
            pixels = new short[w * h];
        }
        if (pixels.length != w * h) {
            throw new IllegalArgumentException("bad dimensions");
        }
        data = pixels;
    }

    public ShortPixmap(int w, int h) {
        this(w, h, null);
    }

    public ShortPixmap(Pixmap p) {
        this(p.width, p.height, p.getShorts());
    }

    public ShortPixmap(String fileName) throws IOException {
        super(fileName);
        data = getShorts(readBytes());
        close();
    }

    // conversions
    public byte[] getBytes() {
        return getBytes(data);
    }

    public short[] getShorts() {
        return getShorts(data);
    }

    public double[] getDoubles() {
        return getDoubles(data);
    }

    // strings
    public String pixelType() {
        return "short";
    }

}
