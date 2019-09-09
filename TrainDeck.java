import java.util.Stack;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.awt.Toolkit;
/**
 * Estasblishes decks for destination cards
 *
 * @author Fez, Noah, Rajshree, Bradley, Dan, David
 * @version 4/29/18
 */
public class TrainDeck
{
    private final int totalCards = 110;
    private final int maxCards = 12;
    ArrayList<TrainCard> deck = new ArrayList<TrainCard>();
    private Toolkit toolkit;
    Random r = new Random();
    TrainDeck[] faceUp = new TrainDeck[5];
     /**
     * Constructs new deck with 12 of each color, 14 locomotives
     */
    public TrainDeck()
    {
         
        toolkit = Toolkit.getDefaultToolkit();
        int i = 0;
        
        while(i < 12)
        {
             deck.add(new TrainCard(toolkit.getImage
             ("images/RedCard.JPG"),Colors.RED));
             deck.add(new TrainCard(toolkit.getImage
             ("images/OrangeCard.JPG"),Colors.ORANGE));
             deck.add(new TrainCard(toolkit.getImage
             ("images/YellowCard.JPG"), Colors.YELLOW));
             deck.add(new TrainCard(toolkit.getImage
             ("images/GreenCard.JPG"),Colors.GREEN));
             deck.add(new TrainCard(toolkit.getImage
             ("images/BlueCard.JPG"),Colors.BLUE));
             deck.add(new TrainCard(toolkit.getImage
             ("images/PurpleCard.JPG"),Colors.PURPLE));
             deck.add(new TrainCard(toolkit.getImage
             ("images/BlackCard.JPG"),Colors.BLACK));
             deck.add(new TrainCard(toolkit.getImage
             ("images/WhiteCard.JPG"),Colors.WHITE));
            deck.add(new TrainCard(toolkit.getImage
            ("images/RainbowCard.JPG"),Colors.LOCO));
            i++;
        }
        //get to 14 nescessary loco cards 
         deck.add(new TrainCard(toolkit.getImage
         ("images/RainbowCard.JPG"),Colors.LOCO));
         deck.add(new TrainCard(toolkit.getImage
         ("images/RainbowCard.JPG"),Colors.LOCO));
        

        shuffle();
    }

    /**
     * Shuffles the cards in the deck
     */
    public void shuffle(){
        Collections.shuffle(deck);
    }
    
     /**
     * Takes card from deck to put onto table or into player 
     * hand
     * 
     * @return top card off deck
     */
    public TrainCard getCard()
    {
        return deck.remove(0);
    }

     /**
     * Adds new card to deck
     * @pram t the new card to put onto the deck
     */
    public void add(TrainCard t)
    {
        deck.add(t);
    }
}