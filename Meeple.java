import java.awt.*;
/**
 * Covers all functionality of meeples on the board
 *
 * @author Noah, Bradley, Fez, Rajshree, Dan, David
 * @version 4/29/18
 */
public class Meeple
{
    Colors color;
    Image image;

     /**
     * Constructs the meeple by setting its image
     * @param img the image representing the meeple
     */
    public Meeple(Image img)
    {
        image = img;
    }

    /**
     * Constructs the meeple by setting its color
     * @param c the color of the meeple
     */
    public Meeple(Colors c)
    {
        color = c;
    }

    
    /**
     * Returns the color of the meeple
     * @return the meeple's color
     */
    protected Colors getColor(){
        return color;   
    }

    /**
     * Sets the color of the meeple
     * @param col the new color
     */
    protected void setColors(Colors col){
        color = col;
    }

}
