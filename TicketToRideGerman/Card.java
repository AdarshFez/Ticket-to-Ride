package TicketToRideGerman;


/**
 * Write a description of class Card here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Card
{
    Ccolor color;
    int quant; 
    public Card()
    {
       color = Ccolor.RED;
       quant = 12;//not sure if this is the right place for this
                   //but itd prob help to track the amount the deck or
                   //each player has somewhere
    }
    public Card(Ccolor c)
    {
        color = c;
        if(c == Ccolor.LOCO)
        {
            quant = 14;
        }
        else
        {
            quant = 12;
        }
    }
    public Card(Ccolor c, int n)
    {
        color = c;
        quant = n;
    }
    
    public Ccolor getColor()
    {
        return color;
    }
}
