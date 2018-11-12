import javax.sound.sampled.*;
import java.io.*;

public class MusicPlayer
{
    public static Clip clip;
    public static boolean muted = false;

    /**
     * Plays music from wav file
     *
     * @param fileName name of wav file
     */
    public void playSong(String fileName)
    {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(
                    new File("res/music/" + fileName + ".wav"));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stopSong(String fileName)
    {
        try
        {
            clip.stop();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
