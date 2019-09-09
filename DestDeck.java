import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JComponent;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.JLabel;
/**
 * Creates decks for destination tickets
 *
 * @author Dan van Dyk
 * @version 4/29/18
 */
public class DestDeck extends JFrame implements MouseListener
{
    ArrayList<DestCard> shortDestDeck = new ArrayList<DestCard>();
    ArrayList<DestCard> longDestDeck = new ArrayList<DestCard>();

    static JButton button = 
        new JButton("Confirm Desired Destination Cards");
    private Toolkit toolkit  = Toolkit.getDefaultToolkit();
    static JPanel panel;
    static Container contentPane;
    /**
     * Constructor for objects of class DestDeck
     */
    public DestDeck()
    {
        File file = new File("cityCords.txt");
        //name toolkit to use later as "toolkit"
        toolkit = Toolkit.getDefaultToolkit();
        try{
            Scanner sc = new Scanner(file);
            String line = sc.nextLine();

            while(!(line = sc.nextLine()).isEmpty()){

                String[] lineS = new String[5];
                lineS = line.split("\\s+"); 
                if(lineS.length > 0){
                    DestCard currentCard = 
                        new DestCard(lineS[0],lineS[1],
                            Integer.parseInt(lineS[2]),
                            "images/"+lineS[3], lineS[4]);
                    if(currentCard.getDistance().equals("short")){
                        shortDestDeck.add(currentCard);
                    } 
                    else{
                        longDestDeck.add(currentCard);
                    }
                }

            }
            //Collections.shuffle(shortDestDeck);
            //Collections.shuffle(longDestDeck);
            sc.close();
        }catch(FileNotFoundException e){
            System.err.print(e);
        }
    }

    /**
     * Returns an array list of short cards
     * 
     * @return array list of short dest cards
     */
    public ArrayList getTempShort(){
        ArrayList<DestCard> tempShortList = new ArrayList<>(shortDestDeck);

        return tempShortList;
    }

    /**
     * Returns an array list of long cards
     * 
     * @return array list of short long cards
     */
    public ArrayList getTempLong(){
        ArrayList<DestCard> tempLongList = new ArrayList<>(longDestDeck);
        return tempLongList;
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Due to the implemented event handlers, this code needed
     * to be here, HOWEVER due to a lack of necessity the method
     * is blank
     */
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Creates an array List of cards for the player to chose from
     * @param c(1-4) the cards for the array list
     */
    public void gameStart(TrainCard c1,
    TrainCard c2, TrainCard c3, TrainCard c4){
        //store the chosen cards in a 
        //temp arraylist until knows what to keep 
        ArrayList<DestCard> chosen = new  ArrayList<DestCard>();

    }
    //paramaters used for real game(DestCard c1, DestCard c2,
    //DestCard c3, DestCard c4, Player currentPlayer,int turnNum)
    /**
     * Works on turns involving the destination tickets
     * @param c(1-4) the cards involved
     * @param currentPlayer the player whose turn it is
     * @param turnNum the numeric turn
     */
    public void destTurn(DestCard c1, DestCard c2, DestCard c3,
    DestCard c4, Player currentPlayer,int turnNum){ 

        //used to check

        // int turnNum = 1;
        // Player currentPlayer = new Player("daniel", Colors.WHITE);
        // DestCard c1 = shortDestDeck.get(0);
        // DestCard c2 = shortDestDeck.get(1);
        // DestCard c3 = shortDestDeck.get(2);
        // DestCard c4 = shortDestDeck.get(3);

        panel = new JPanel(new GridLayout(0,2));
        panel.removeAll();
        JFrame frame = new JFrame(currentPlayer.getName()
                + "'s Destination Cards");
        //JPanel panel = new JPanel(new GridLayout(0,2));
        //if is null due to cards not left in deck to 
        //choose from then dont make button?
        JCheckBox check1 = new JCheckBox();
        panel.add(check1);
        panel.add(new JLabel( new ImageIcon( toolkit.getImage(c1
                        .getImagePath()).getScaledInstance(108,180, Image.SCALE_SMOOTH ))));

        JCheckBox check2 = new JCheckBox();
        panel.add(check2);
        panel.add(new JLabel(new ImageIcon
                ( toolkit.getImage(c2.getImagePath()).getScaledInstance
                    (108,180, Image.SCALE_SMOOTH ))));

        JCheckBox check3 = new JCheckBox();
        panel.add(check3);
        panel.add(new JLabel( new ImageIcon
                ( toolkit.getImage(c3.getImagePath()).getScaledInstance
                    (108,180, Image.SCALE_SMOOTH ))));

        JCheckBox check4 = new JCheckBox();
        panel.add(check4);
        panel.add(new JLabel( new ImageIcon( toolkit.getImage
                    (c4.getImagePath()).getScaledInstance(108,180,
                        Image.SCALE_SMOOTH ))));

        contentPane = frame.getContentPane();
        //add pannel that contains the checkbutton and images
        //ad button to botton of content pane that holds 
        //both that can be controlled by my frame

        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(button, BorderLayout.SOUTH);
        frame.setSize(300,800);
        //make jframe centered on any screen
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(dim.width/2-frame.getSize().width/2,
            dim.height/2-frame.getSize().height/2);

        frame.setUndecorated(true);
        frame.getRootPane().setBorder(BorderFactory.
            createMatteBorder(4, 4, 4, 4, new Color(139,69,19)));
        frame.setVisible(true);

        //temp array of cards that were selected after pressing confirm
        ArrayList<DestCard> checkArr = new ArrayList<DestCard>();

        button.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    //if first turn then make sure there is at least 2 
                    //seleced by checking if "checked" selection has 
                    //more than 3 selected then call method 
                    //with same cards again until valid input is entered
                    int selectionCount = 0;

                    if(check1.isSelected()){
                        selectionCount += 1;
                    }
                    if(check2.isSelected()){
                        selectionCount += 1;
                    }
                    if(check3.isSelected()){
                        selectionCount += 1;
                    }
                    if(check4.isSelected()){
                        selectionCount += 1;
                    }
                    //if first turn and not 2 selected then kill 
                    //old frame and make new, and if not first turn
                    //and none are selected make new

                    //add to temp arr of selected cards "will not get to
                    //this step if invalid input"

                    if((selectionCount >= 2 && turnNum == 0) || 
                    (selectionCount >=1 && turnNum != 0)){
                        if(check1.isSelected()){
                            checkArr.add(c1); 
                        }
                        else{
                            //puts the cards back to bottom of 
                            //respective deck if they are not checked
                            if(shortDestDeck.contains(c1)){
                                shortDestDeck.remove(c1);
                                shortDestDeck.add(c1);
                            }
                            if(longDestDeck.contains(c1)){
                                longDestDeck.remove(c1);
                                longDestDeck.add(c1);
                            }
                        }

                        if(check2.isSelected()){
                            checkArr.add(c2);
                        }
                        else{
                            if(shortDestDeck.contains(c2)){
                                shortDestDeck.remove(c2);
                                shortDestDeck.add(c2);
                            }
                            if(longDestDeck.contains(c2)){
                                longDestDeck.remove(c2);
                                longDestDeck.add(c2);
                            }
                        }

                        if(check3.isSelected()){
                            checkArr.add(c3);
                        }
                        else{
                            if(shortDestDeck.contains(c3)){
                                shortDestDeck.remove(c3);
                                shortDestDeck.add(c3);
                            }
                            if(longDestDeck.contains(c3)){
                                longDestDeck.remove(c3);
                                longDestDeck.add(c3);
                            }
                        }

                        if(check4.isSelected()){
                            checkArr.add(c4);
                        }
                        else{
                            if(shortDestDeck.contains(c4)){
                                shortDestDeck.remove(c4);
                                shortDestDeck.add(c4);
                            }
                            if(longDestDeck.contains(c4)){
                                longDestDeck.remove(c4);
                                longDestDeck.add(c4);
                            }
                        }

                        //go through each card that was checked 
                        //and add to players hand and remove
                        //from available destDeck 
                        //(either short or long deck)
                        for(DestCard dcardObject: checkArr){
                            currentPlayer.destination.add(dcardObject);
                            if(shortDestDeck.contains(dcardObject)){
                                shortDestDeck.remove(dcardObject);
                            }
                            if(longDestDeck.contains(dcardObject)){
                                longDestDeck.remove(dcardObject);
                            }
                        }

                        frame.setVisible(false);
                    }
                    // ArrayList<DestCard> check = new ArrayList<DestCard>
                    //(currentPlayer.getDestHand());
                    // ArrayList<DestCard> checkshort = new 
                    //ArrayList<DestCard>(shortDestDeck);
                }
            });

    }  

    /**
     * Creates GUI for the Frame created in this function
     */
    public static void createAndShowGUI(){
        JFrame frame = new JFrame("DestCheckBox");

        //needs destCardPanel created in above method
        //to be jcomponent to be set as frame
        JComponent newContentPane = (JComponent)contentPane;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.add(button);
        //display window
        frame.pack();
        frame.setVisible(true);
        frame.dispose();
    }
    //  
}

