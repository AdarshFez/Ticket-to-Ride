import java.util.ArrayList;
/**
 * This class represents the locations of cities on the board
 *
 * @author Bradley, Fez, Rajshree, Noah, David, Dan
 * @version 4/29/18
 */
public class Vertex
{
    ArrayList edges;
    ArrayList<Meeple> meeples;
    Cities cityName;
    int xLower;
    int xUpper;
    int yLower;
    int yUpper;
    String stringName;

    /**
     * This constructs a new vertex of default values
     */
    public Vertex(){

        cityName = null;
        meeples = new ArrayList<>();
        edges = new ArrayList<>();

        xLower = 0;
        xUpper = 0;
        yLower = 0;
        yUpper = 0;
        stringName = "";

    }

     /**
     * This constructs a new vertex with established vars
     * 
     * @param cityName the name of the city
     * @param stringName the name of the string
     * @param xLower,Xupper,yLower,yUpper the coords for the
     *  map
     */
    public Vertex(Cities cityName,String stringName
    ,int xLower,int xUpper, int yLower, int yUpper)
    {
        this.cityName = cityName;
        meeples = new ArrayList<>();
        edges = new ArrayList<>();

        this.xLower = xLower;
        this.xUpper = xUpper;
        this.yLower = yLower;
        this.yUpper = yUpper;
        this.stringName = stringName;
    }

    /**
     * Adds a meeple to a vertex
     * @param the meeple to add
     */
    public void addMeeple(Meeple m)
    {
        meeples.add(m);
    }

     /**
     * Returns number meeples on the vertex
     */
    public int getNumMeeples()
    {
        return meeples.size();
    }
    
    /**
     * Returns xUpper coord
     * @return the xUpper
     */
    public int getxUpper()
    {
        return xUpper;
    }

    /**
     * Returns xLower coord
     * @return the xLower
     */
    public int getxLower()
    {
        return xLower;
    }

    /**
     * Returns yUpper coord
     * @return the yUpper
     */
    public int getyUpper()
    {
        return yUpper;
    }

     /**
     * Returns yLower coord
     * @return the yLower
     */
    public int getyLower()
    {
        return yLower;
    }

    /**
     * Returns the name of the city
     * @return the name of the city
     */
    public Cities getCityName()
    {
        return cityName;
    }

    /**
     * Adds an edge to the vertex
     * 
     * @param e the edge to add
     */
    public void addEdge(Edge e)
    {
        edges.add(e);

    }

     /**
     * Removes an edge from the vertex
     * 
     * @return e the edge to remove
     */
    public void removeEdge(Edge e)
    {
        edges.remove(e);
    }

     /**
     * returns the edges a vertex has
     * @return the list of edges
     */
    protected ArrayList<Edge> getEdge(){
        return edges;   
    }


    /**
     * prints the meeples a vertex has (based on color)
     */
    public void printMeeples()
    {
        for(Meeple m: meeples)
        {
            System.out.print(m.getColor() + " ");
        }
    }

    /**
     * creates a string of all meepls a vertex has based
     * on color
     * 
     * @return the string of meeples
     */
    public String meepleToString()
    {
        String toReturn = "\n";
        for(Meeple m: meeples)
        {
            toReturn += m.getColor() + " \n";
        }
        return toReturn;
    }
}
