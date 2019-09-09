package TicketToRideGerman;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.Stack;
import java.util.Scanner;
import java.util.ArrayList;
/**
 * Write a description of class Board here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Board extends JPanel implements MouseListener
{
    Player player1;
    Player player2;
    Player player3;
    Player player4;
    Player player5;
    Deck deck;
    MeepleGroup bMeeples;
    int curPlays;
    Stack discard;
    String cols = "RBGYPW";
    Image image;
    Track [] tracks;

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }

    protected static void createAndShowGUI() {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//for now

        String[] choices = {"2", "3", "4", "5"};
        JComboBox<String> cb = new JComboBox<String>(choices);
        // f.add(cb);

        int s = JOptionPane.showOptionDialog(new JFrame(), cb, 
                "Please Select a Number of Players", JOptionPane.YES_NO_OPTION,
                JOptionPane.DEFAULT_OPTION, null, new Object[] 
                { "OK", "Close" }, JOptionPane.CLOSED_OPTION);

        if (s != 0) {
            System.exit(s);
        }

        String level = (String) cb.getSelectedItem();

        f.setTitle("Ticket to Ride: Germany");
        Board b = new Board();
        f.getContentPane().add(b);
        f.setResizable(false);
        f.setSize(1000,950);

        f.setVisible(true);

    }

    private void loadImage() {

        try {
            URL url = getClass().getResource("board.jpg");
            image = ImageIO.read(url);
            repaint();
        } catch (MalformedURLException mue) {
            System.err.println(mue.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public Board()
    {
        player1 = new Player();
        player2 = new Player();
        if(curPlays > 2)
        {
            player3 = new Player();
            if(curPlays > 3)
            {
                player4 = new Player(); 
                if(curPlays == 5)
                {
                    player5 = new Player();
                }
            }
        }
        deck = new Deck();
        discard = new Stack();
        bMeeples = new MeepleGroup(false);
        loadImage();
        addMouseListener(this);
        setTrack();
    }

    private String pickPlayersName(String args[])
    {
        System.out.println("Please Enter a name: ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        return name;
    }

    private TAndMcolor pickPlayersColor()
    {
        System.out.println("Please pick a color :\n Must be one of the following");        
        for(int i = 0; i < 6; i++)
        {

        }
        return TAndMcolor.RED;
    }

    public void paintComponent(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        g2.drawImage(image,0,0,800,800,this);
    }

    /////////////////////////////////////////////////////////////////////////temp////////////

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        

    }

    public void mouseReleased(MouseEvent e) {

    }
    int counterr = 0;
    public void mouseClicked(MouseEvent e) {
        counterr++;
        System.out.println("The X:" + e.getX()+ ",Y: " + e.getY() + "counter :" + counterr); 
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {

    }
    
    private void setTrack()
    {
        //Green Track;
        
        
    }
}

