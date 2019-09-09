// import javax.swing.JButton;
// import javax.swing.JFrame;
// import javax.swing.JPanel;
// import javax.swing.JLabel;
// import java.awt.GridLayout;
// import java.awt.event.*;
// import javax.swing.ImageIcon;

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

import java.awt.Toolkit;

/**
 * Creates panel for destination cards
 *
 * @author Fez, Rajshree, Dan, Noah, Bradley, Daivd
 * @version 4/30/18
 */
public class DestHandPanel extends JFrame
{
    // instance variables - replace the example below with your own
    JPanel handPanel;
    static Container handContentPane;
    private Toolkit toolkit  = Toolkit.getDefaultToolkit();


    /**
     * Shows the hand of the current player on the board
     * @param currentPlayer the current player
     */
    protected void showDestHand(Player currentPlayer){

        handPanel = new JPanel(new GridLayout(0,5));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        JFrame frame = new JFrame(currentPlayer.getName() 
                + "'s Destination Cards");
        handContentPane = frame.getContentPane();

        frame.setSize(800,350);
        frame.setLocation(dim.width/2-frame.getSize().width/2,
            dim.height/2-frame.getSize().height/2);

        for(DestCard currentCard: currentPlayer.getDestHand()){
            handPanel.add(new JLabel( new ImageIcon( toolkit.
                        getImage(
                        currentCard.getImagePath()).
                        getScaledInstance
                        (108,180, Image.SCALE_SMOOTH ))));

        }
        handContentPane.add(handPanel, BorderLayout.CENTER);
        handContentPane = frame.getContentPane();

        frame.setVisible(true);

    }
}
