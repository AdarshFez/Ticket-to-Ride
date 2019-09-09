import java.io.*;
import java.net.*;
import javax.sound.sampled.*;

/**
 * Repeated loops of songs
 *
 * @author Rajshree Marhatta
 * @version Spring 2018
 */

public class MusicLoopPlayer{
    Clip music;
    /**
     * Constructor of looping songs. Sets the clip to the main 
     * background song.
     */
    public MusicLoopPlayer(){
        URL url =  getClass().getResource("backg.wav");
        try{

            AudioInputStream audioInputStream =
            AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();

            music = clip;
        }
        catch(IOException e){}
        catch(Exception e){}

    }

    /**
     * Main part of the game song
     */
    public void music(){
        URL url =  getClass().getResource("backg.wav");
        try{

            AudioInputStream audioInputStream = 
            AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            music = clip;
        }
        catch(IOException e){}
        catch(Exception e){}
    }

    /**
     * The intro song 
     */
    public void intro(){
        URL url =  getClass().getResource("intro.wav");
        try{

            AudioInputStream audioInputStream = 
            AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            music = clip;
        }
        catch(IOException e){}
        catch(Exception e){}

    }

    /**
     * Stop the music playing
     */
    public void stop(){
        music.stop();
    }
}
