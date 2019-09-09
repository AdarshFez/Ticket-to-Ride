import java.awt.Color;
import java.util.ArrayList;
/**
 * Represents the edges between vertices
 *
 * @author Rajshree, Noah, Fez, Bradley, Dan, David
 * @version 4/29/18
 */
public class Edge
{
    Colors color;
    int length;
    Player owner;
    int src;
    int dst;
    ArrayList<Integer> routeNumbers = new ArrayList<Integer>();
    /**
     * Creates the new edge by setting up all vars correctly
     * 
     * @param color the color of edge
     * @param length how many trains are needed for edge
     * @param src, dst the vertices it connects
     */
    public Edge(Colors color, int length, int src, int dst)
    {
        this.color = color;
        this.length = length;
        this.src = src;
        this.dst = dst;
        owner = null;
    }

    /**
     * Creates the new edge by setting up all vars correctly
     * also establishes an array list of route numbers
     * 
     * @param color the color of edge
     * @param length how many trains are needed for edge
     * @param src, dst the vertices it connects
     * @param routes the routes invloved for this edge
     */
    public Edge(Colors color, int length,
    int src, int dst, ArrayList<Integer> routes)
    {
        this.color = color;
        this.length = length;
        this.src = src;
        this.dst = dst;
        owner = null;
        routeNumbers = new ArrayList<Integer>();
        for(Integer ints: routes){
            routeNumbers.add(ints);
        }
    }

    /**
     * Returns source vertex
     * @return src var
     */
    protected int getSource(){
        return src;
    }

    /**
     * Returns destination vertex
     * @return dst var
     */
    protected int getDest(){
        return dst;
    }

    /**
     * Returns number of trains needed for edge
     * @return length var
     */
    protected int getLength(){
        return length;
    }

    /**
     * Returns color of trainCards needed for edge
     * @return color var
     */
    protected Colors getColor(){
        return color;
    }

    /**
     * Changes owner of edge to new owner
     * @param owner the player who claimed route
     */
    public void setOwner(Player owner)
    {
        this.owner = owner;
    }

    /**
     * Returns owner of edge
     * @return the player who claimed route
     */
    public Player getOwner()
    {
        return owner;
    }

    /**
     * Returns route numbers for edges
     * @return an arraylist of route numbers
     */
    public ArrayList<Integer> getRouteNumbers(){return routeNumbers;}
}
