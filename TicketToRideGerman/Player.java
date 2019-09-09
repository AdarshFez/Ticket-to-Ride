package TicketToRideGerman;


/**
 * Write a description of class Player here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Player
{
    String name;
    TAndMcolor color;
    Hand hand;
    MeepleGroup pMeeples;
    int trainCount = 45;
    
    public Player()
    {
        name = "Jimmy-Jo";
        color = TAndMcolor.PURPLE;
        hand = new Hand();
        pMeeples = new MeepleGroup(true);        
    }
    public Player(String playerName, TAndMcolor col)
    {
        name = playerName;
        color = col;
        hand = new Hand();
        pMeeples = new MeepleGroup(true);
    }
}
