import java.awt.*;

/**
 * Controls functionality for train cards
 *
 * @author Noah, Bradley, Fez, Rajshree, Dan, David
 * @version 4/29/18
 */
public class TrainCard
{
    Colors color;
    Image cardImage;

    /**
     * Creates new card with established color and img
     * @param img the image the card uses
     * @param c the color card uses
     */    
    public TrainCard(Image img, Colors c)
    {
        color = c;

        cardImage = img;
    }

    /**
     * Returns the color of the card
     * @return color
     */
    public Colors getColor()
    {
        return color;
    }

    /**
     * Changes image to new image
     * @param img the new image
     */
    public void setImage(Image img){
        cardImage = img;
    }

    /**
     * Changes color to new color
     * @param c the new color
     */
    public void setColor(Colors c)
    {
        color = c;
    }

    /**
     * Returns the image of the card
     * @return cards image
     */
    public Image getImage()
    {
        return cardImage;
    }
}

