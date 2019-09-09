import java.util.ArrayList;
import java.awt.*;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.*;
/**
 * This code represents all aspects of what a player needs to play the
 * game. Gives them a name, color, points and collections for each
 * item in their hand
 *
 * @author Bradley, Noah, Rajshree, Fez, David, Dan
 * @version 4/29/18
 */
public class Player 
{
    String name;
    Colors color;
    ArrayList<Meeple> playerMeep;
    ArrayList<DestCard> destination;
    ArrayList<TrainCard> trainC;
    
 

    int trainCount;
    int totalPoints;
    int totalRoutes;

     /**
     * This constructor creates a new player, initializing all
     * variables to their starting state and establishes their
     * name and color
     * 
     * @param playerName the string to become the player's
     *      name
     * @param col the Color of the players trains
     */ 
    public Player(String playerName, Colors col)
    {
        name = playerName;
        color = col;
        playerMeep = new ArrayList<Meeple>();
        destination = new ArrayList<DestCard>();
        trainC = new ArrayList<TrainCard>();
        trainCount = 15;
        totalPoints = 0;
        totalRoutes = 0;
    }
    
    /**
     * This method adds a meeple to the players hand
     * @param col the color of the meeple
     */
    protected void addMeeple(Colors col){
        playerMeep.add(new Meeple(col));   
    }

     /**
     * This method gets the list of a players
     * destination tickets
     * 
     * @return the array list of dest cards
     */
    public ArrayList<DestCard> getDestHand(){
        return destination;
    }

    /**
     * Adds new destination card to players hand
     * @param d the dest card being added
     */
    protected void addDest(DestCard d){
        destination.add(d);
    }

    
    /**
     * This method returns the players name
     * 
     * @return the players name
     */
    protected String getName(){
        return name;   
    }

    public void addCard(TrainCard t)
    {
        trainC.add(t);
    }

    /**
     * Sets the total points for the player
     * 
     * @param total - the total points for the player
     */
    protected void setTotalPoints(int total){
        totalPoints = total;   
    }

        /**
     * This method gives the player a new Train Card. 
     * @param t the new traincard to add
     */
    protected int getTrainCount(){
        return trainCount;   
    }

    /**
     * Returns the trains the player has left
     * 45 by default
     * @return the number of trains
     */
    protected void setTrainCount(int sub){
        trainCount -= sub;   
    }

    /**
     * Returns the points a player has
     * @return the players points
     */
    protected int getPoints(){
        return totalPoints;   
    }

    protected void addPoints(int numP){
        if((totalPoints + numP) <= 100){
            totalPoints += numP;   
        }
        else{
            totalPoints = 100;
        }
    }

    /**
     * This method adds points to a players score
     * 
     * @param numP the number of points to increase
     *      by
     */
    protected void subPoints(int numP){
        totalPoints -= numP;   
    }

    /**
     * This method returns the train cards a player has
     * 
     * @return the list of train cards in hand
     */
    protected ArrayList<TrainCard> getTrainCards(){
        return trainC;
    }

     /**
     * Gets the last train card in the hand
     * 
     * @return the last train card in the players hand
     */
    protected TrainCard getTrainCardLast(){
        return trainC.get(trainC.size()-1);
    }

    /**
     * Train colors for the player 
     */
    protected Color getColor2(){
        switch(color){

            case RED: return Color.RED;
            case GREEN: return Color.GREEN;
            case BLACK: return Color.BLACK;
            case YELLOW: return Color.YELLOW;

            case PURPLE: return new Color(128,0,128);

            default:
            return Color.WHITE;
        }
    }

    /**
     * Returns how many cards of a color a player has
     * 
     * @param c the color to test
     * @return the number of color c cards
     */
    protected int getTotalColor(Colors c){
        int total = 0;
        for(TrainCard train: trainC){
            if(train.getColor() == c){
                total++;
            }
        }

        return total;
    }

    /**
     * Total number of red meeples for the player
     * 
     * @return the total number of red meeples the player has
     */
    protected int totalRedMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){
            if(playerMeep.get(i).getColor() == Colors.RED){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Total number of red meeples for the player
     * 
     * @return the total number of yellow meeples the player has
     */
    protected int totalYellowMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){
            if(playerMeep.get(i).getColor() == Colors.YELLOW){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Total number of red meeples for the player
     * 
     * @return the total number of black meeples the player has
     */
    protected int totalBlackMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){
            if(playerMeep.get(i).getColor() == Colors.BLACK){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Total number of green meeples for the player
     * 
     * @return int the total number of green meeples the player has
     */
    protected int totalGreenMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){

            if(playerMeep.get(i).getColor() == Colors.GREEN){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Total number of purple meeples for the player
     * 
     * @return int the total number of purple meeples the player has
     */
    protected int totalBlueMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){
            if(playerMeep.get(i).getColor() == Colors.BLUE){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Total number of white meeples for the player
     * 
     * @return int the total number of white meeples the player has
     */
    protected int totalWhiteMeep(){
        int counter = 0;
        for(int i =0; i < playerMeep.size(); i++){

            if(playerMeep.get(i).getColor() == Colors.WHITE){
                counter++;
            }
        }
        return counter;
    }

    /**
     * Tests which type of Train card you have the most of
     * @return the highest number of cards
     */
    protected int maxCardType()
    {
        int redCount = 0; int blueCount = 0; 
        int greenCount = 0; int orangeCount = 0; int purpleCount = 0;
        int blackCount = 0; int whiteCount = 0;
        int yellowCount = 0; int locoCount = 0;
        for(TrainCard t: trainC)
        {
            if(t.getColor() == Colors.RED)
            {
                redCount++;
            }
            if(t.getColor() == Colors.BLUE)
            {
                blueCount++;
            }
            if(t.getColor() == Colors.GREEN)
            {
                greenCount++;
            }
            if(t.getColor() == Colors.ORANGE)
            {
                orangeCount++;
            }
            if(t.getColor() == Colors.BLACK)
            {
                blackCount++;
            }
            if(t.getColor() == Colors.WHITE)
            {
                whiteCount++;
            }
            if(t.getColor() == Colors.YELLOW)
            {
                yellowCount++;
            }
            if(t.getColor() == Colors.LOCO)
            {
                locoCount++;
            }
            if(t.getColor() == Colors.PURPLE)
            {
                purpleCount++;
            }
        }
        redCount += locoCount; blueCount += locoCount; orangeCount
        += locoCount; greenCount += locoCount;
        purpleCount += locoCount; yellowCount += locoCount; 
        whiteCount += locoCount; blackCount += locoCount;
        return Math.max(locoCount,Math.max(Math.max(Math.max
        (redCount,blueCount),Math.max(greenCount,yellowCount)),
                Math.max(Math.max(orangeCount,purpleCount),
                Math.max(blackCount,whiteCount))));
    }

    /**
     * Removes the last trainCard in the arraylist
     */
    protected void remove(){
        trainC.remove(trainC.size()-1);
    }

    /**
     * Returns the total number of routes that the user completed.
     * //Just subtract off the total
     */
    public int completedRoutes(){
        ArrayList<DestCard> completed = new ArrayList<>();
        //ArrayList<DestCard> notCompleted = new ArrayList<>();
        int totalSub = 0;
        int totalGain=0;

        //Do a DepthFirst traversal to see what routes the user completed
        Stack s = new Stack();
        for(DestCard total: destination){
            String start = total.getStartCity();

            if(completed.contains(total)){
                addPoints(total.getPoints());
                totalGain+= total.getPoints();
                // totalGain += 
                //add the points to the users total points.
            }
            else{

                //get the points for each of the card 
                //and subtract the points;
                totalSub += total.getPoints();   
            }

        }
        //Print out the results in a JOptionPane
        return 0;
    }

    /**
     * returns the points you can get from a path
     * 
     * @param trainLength length of path
     * @return number of points
     */
    public int addTrainPoints(int trainLength){
        if(trainLength == 3){return 4;}
        else if(trainLength == 4){return 7;}
        else if(trainLength == 5){return 10;}
        else if(trainLength == 6){return 15;}
        else if(trainLength == 7){return 18;}
        return trainLength;   
    }
}
