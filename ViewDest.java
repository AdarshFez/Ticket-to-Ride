import java.awt.*;
import javax.swing.*;

/**
 * Usr views their own destination cards
 *
 * @author (your name)
 * @version Spring 2018
 */
public class ViewDest extends JPanel
{
   public ViewDest(){
    
    
    
    }
   private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Destination Cards");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ViewDest view = new ViewDest();
        frame.getContentPane().add(view);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }
}
