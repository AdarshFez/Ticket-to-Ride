package TicketToRideGerman;

import java.util.Stack;
import java.util.Random;
/**
 * Write a description of class deck here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Deck
{
    private final int totalCards = 110;
    private final int maxCards = 12;
    Stack deck = new Stack();
    Random r = new Random();
    public Deck()
    {
        int i = 0;
        deck.push(new Card(Ccolor.LOCO));
        while(i < 12)
        {
            deck.push(new Card(Ccolor.RED));
            deck.push(new Card(Ccolor.ORANGE));
            deck.push(new Card(Ccolor.YELLOW));
            deck.push(new Card(Ccolor.GREEN));
            deck.push(new Card(Ccolor.BLUE));
            deck.push(new Card(Ccolor.PURPLE));
            deck.push(new Card(Ccolor.BLACK));
            deck.push(new Card(Ccolor.WHITE));
            deck.push(new Card(Ccolor.LOCO));
            i++;
        }
        deck.push(new Card(Ccolor.LOCO));
        
        shuffle();shuffle();shuffle();
    }

    public void shuffle()
    {
        Stack tempA = new Stack();
        Stack tempB = new Stack();
        while(!deck.isEmpty())
        {
            int i = r.nextInt();
            if(i%2 == 0)
            {
                tempA.push(deck.pop());
            }
            else
            {
                tempB.push(deck.pop());
            }
        }
        while(!tempA.isEmpty() || !tempB.isEmpty())
        {
            if(!tempA.isEmpty())            
                deck.push(tempA.pop());            
            if(!tempB.isEmpty())
                deck.push(tempB.pop());
        }
    }

    public void overkillPrint()
    {
        Stack temp = new Stack();
        Card tempC;
        while(!deck.isEmpty())
        {
            tempC = (Card)deck.pop();
            System.out.println(tempC.getColor());
            temp.push(tempC);
        }
        while(!deck.isEmpty())
        {
            deck.push(temp.pop());
        }
    }
}
