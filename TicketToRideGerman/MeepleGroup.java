package TicketToRideGerman;

import java.util.Stack;
/**
 * Write a description of class MeepleGroup here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class MeepleGroup
{
    private final int maxMeeps = 60;
    Stack red;
    Stack black;
    Stack green;
    Stack yellow;
    Stack purple;
    Stack white;
    public MeepleGroup(boolean isPlayer)
    {
        red    = new Stack();
        black  = new Stack();
        green  = new Stack();
        yellow = new Stack();
        purple = new Stack();
        white  = new Stack();
        
        if(!isPlayer)
        {
            for(int i = 0; i < 10; i++)
            {
                red.push(new Meeple(TAndMcolor.RED));
                black.push(new Meeple(TAndMcolor.BLACK));
                green.push(new Meeple(TAndMcolor.GREEN));
                yellow.push(new Meeple(TAndMcolor.YELLOW));
                purple.push(new Meeple(TAndMcolor.PURPLE));
                white.push(new Meeple(TAndMcolor.WHITE));
            }
        }   
    }
    
    
}
