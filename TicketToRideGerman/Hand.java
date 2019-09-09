package TicketToRideGerman;

import java.util.ArrayList;
/**
 * Write a description of class Hand here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Hand
{
    ArrayList hand;

    public Hand()
    {
        hand = new ArrayList<Card>();        
    }

    public Card discard(Card card)
    {
        for(Object Ccard: hand)
        {
            if(Ccard == card)
            {
                Card temp = (Card) Ccard;
                hand.remove(Ccard);
                return temp;
            }
        }
        return null;
    }
}
