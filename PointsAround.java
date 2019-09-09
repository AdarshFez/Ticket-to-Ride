
/**
 * This creates each players circle on the border point counter
 *
 * @author Bradley, Rajshree, Noah, Fez, Dan, David
 * @version 4/29/18
 */
public class PointsAround
{
    int width, height;
    int x = 0;
    int y= 0;

    /**
     * Constructs the points with an established x and y
     * coord
     * 
     * @param x,y the coordinates of the object
     */
    public PointsAround(int x, int y){
        width= 19;
        height = 19;
        this.x= x;
        this.y=y;

    }

    /**
     * Returns width of the object
     * 
     * @return the width
     */
    protected int getWidth(){
        return width;   
    }

    /**
     * Returns height of the object
     * 
     * @return the height
     */
    protected int getHeight(){
        return height;   
    }

    /**
     * Returns X coord of the object
     * 
     * @return the coord
     */
    protected int getX(){
        return x;   
    }

    /**
     * Returns Y coord of the object
     * 
     * @return the coord
     */
    protected int getY(){
        return y;   
    }

    /**
     * Sets the objects X coord
     * 
     * @param xS the new x coord
     */
    protected void setX(int xS){
        x = xS;   
    }   

    /**
     * Sets the objects Y coord
     * 
     * @param yS the new y coord
     */
    protected void setY(int yS){
        y = yS;   
    }

}
