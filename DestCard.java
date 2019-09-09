import java.awt.*;
/**
 * Creats the functionality for destination tickets
 *
 * @author Dan van Dyk
 * @version 4/29/18
 */
public class DestCard
{
    String startCity;
    String endCity;
    int points;
    String imagePath;
    String distance;
    /**
     * Creates new card
     * @param startCity,endCity the citys required to
     *  satisfy ticket
     *  @param points the points card is worth
     *  @param imagePath the location of cards image in
     *   images subfodler
     *  @param distance the number of length of the path
     */ 
    public DestCard(String startCity, String endCity, 
    int points, String imagePath, String distance)
    {
        this.startCity = startCity;
        this.endCity = endCity;
        this.points = points;
        this.imagePath = imagePath;
        this.distance = distance;

    }

    /**
     * Returns the city you start from
     * @return startCity
     */
    public  String getStartCity(){
        return startCity;
    }

    /**
     * Returns the city you end at
     * @return endCity
     */
    public  String getEndCity(){
        return endCity;
    }

    /**
     * Returns points card is worth
     * @return points
     */
    public  int getPoints(){
        return points;
    }

    /**
     * Returns pathname of image
     * @return imagePath
     */
    public String getImagePath(){
        return imagePath;
    }

    /**
     * Returns number of tiles for card
     * @return distance
     */
    public String getDistance(){
        return distance;
    }

}
