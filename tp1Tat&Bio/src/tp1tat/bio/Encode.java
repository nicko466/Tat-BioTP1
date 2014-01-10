package tp1tat.bio;

/**
 *
 * @author thomas
 */
public class Encode {

    private String pathBaseImage;
    private String pathData2mask;
    private String pathOut;
    private short SizeHeader = 0;

    public Encode(String pathBaseImage, String pathData2mask, String pathOut) {
        this.pathBaseImage = pathBaseImage;
        this.pathData2mask = pathData2mask;
        this.pathOut = pathOut;
    }

    public void encodeHeader(short size2mask, String name) {

    }
    public void encodeData (){
        
    }
}
