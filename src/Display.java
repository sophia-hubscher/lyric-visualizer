import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;
import java.io.*;

/**
 * Displays GUI for user to interact with
 *
 * @author Sophia Hübscher
 * @version 1.4
 */
public class Display extends JPanel implements MouseMotionListener, MouseListener
{
    Font titleFont, subtitleFont, italicLabelFont1, plainLabelFont,
         italicLabelFont2, italicLabelFont3;

    Color   currentColor;
    Color   highlightColor;
    Color   BACKGROUND = new Color(0,   43,  54);
    Color   PINK      = new Color(211, 54,  130);
    Color   RED       = new Color(220, 50,  47);
    Color   ORANGE    = new Color(203, 75,  22);
    Color   YELLOW    = new Color(181, 137, 0);
    Color   GREEN     = new Color(133, 153, 0);
    Color   CYAN      = new Color(42,  161, 152);
    Color   BLUE      = new Color(38,  139, 210);
    Color   MAGENTA   = new Color(108, 113, 196);
    Color[] allColors = {PINK, RED, ORANGE, YELLOW, GREEN, CYAN,
                         BLUE, MAGENTA};

    FileProcessor fp    = new FileProcessor();
    Stats         stats = new Stats();
    MusicPlayer   mp    = new MusicPlayer();

    ArrayList<String> wordsList   = new ArrayList<>();
    ArrayList<String> wordsList2  = new ArrayList<>();
    ArrayList<Color>  colorsList  = new ArrayList<>();
    ArrayList<Color>  colorsList2 = new ArrayList<>();

    String[] songArray = fp.processSong("&burn");
    String[] topWords  = new String[5];
    String[] allWords;

    Song song = new Song(songArray[0], songArray[1],
            songArray[2], songArray[3], songArray[4]);

    int     barXLoc         = 0;
    int     repetitionCount = 0;
    int     width, height;
    int     repetitionOutOf360;
    double  posPercent, lovePercent, violencePercent, swearPercent;
    double  relativeLength;
    String  title = song.title + " - " + song.artist;

    /**
     * Displays GUI for user to interact with
     *
     * @param width width of screen
     * @param height height of screen
     * @param fontName name of font used
     */
    public Display(int width, int height, String fontName)
    {
        this.width = width;
        this.height = height;

        addMouseMotionListener(this);
        addMouseListener(this);

        //sets fonts
        titleFont        = new Font(fontName, Font.BOLD,   40);
        subtitleFont     = new Font(fontName, Font.BOLD,   32);
        italicLabelFont1 = new Font(fontName, Font.ITALIC, 30);
        plainLabelFont   = new Font(fontName, Font.PLAIN,  30);
        italicLabelFont2 = new Font(fontName, Font.ITALIC, 26);
        italicLabelFont3 = new Font(fontName, Font.ITALIC, 19);

        allWords = song.getAllWords();

        setBarColors(); //sets color of repetition bar

        //set stats
        posPercent   = stats.getPositivePercentage(allWords);
        swearPercent = stats.getTypePercentage(allWords,
                    "res/statsText/swearWords", 2200.0);
        lovePercent = stats.getTypePercentage(allWords,
                "res/statsText/romanceWords", 900.0);
        relativeLength = stats.getRelativeLength(song.lyrics,
                6000.0);
        repetitionOutOf360 = stats.getNumOutOf360(repetitionCount,
                stats.countWords(song.lyrics));
        violencePercent = stats.getTypePercentage(allWords,
                "res/statsText/violenceWords", 3000.0);

        //sets top 10 most frequent words
        topWords = fp.getWordFrequencies(song.lyrics);

        //resets x location of bar
        barXLoc = - (allWords.length * 8) + width - 440;
    }

    /**
     * Draws all graphics by a timer calling repaint()
     *
     * @param g the graphics object, automatically sent when repaint() is called
     */
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); //clear the old drawings

        //improves graphics
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //sets stroke weight
        g2.setStroke(new BasicStroke(5));

        //sets background
        g.setColor(BACKGROUND);
        g.fillRect(0, 0, width, height);

        //draws scrolling bar
        for (int x = allWords.length - 1; x > 0; x--)
        {
            g.setColor(colorsList.get(x));
            g.fillRect(x * 8 + barXLoc, 0, 8, 80);
        }

        //draws white dividing line / rectangle
        g.setColor(Color.WHITE);
        g.fillRect(0,80, width, 5);

        //sets title
        title = song.title + " - " + song.artist;

        //if wav for song exists, adds a "♬" to the end of the title
        if (new File ("res/music/" + song.title + "-" +
            song.artist + ".wav").exists())
            title += " (♬)";

        //draws title
        highlightColor = new Color(song.getGenreColor()[0], song.getGenreColor()[1],
                song.getGenreColor()[2]);
        g.setFont(titleFont);
        g.setColor(highlightColor);
        //title highlight offset and in background
        g.drawString(title, 10, 56);
        g.setColor(Color.WHITE);
        g.drawString(title, 8, 58);

        //draws all bars
        //mood bar - swear bar - romanticism bar - length bar
        drawBar(g, RED,         GREEN,   posPercent,     20,
                106, false);
        drawBar(g, Color.BLACK, MAGENTA, swearPercent,   160,
                106, true);
        drawBar(g, Color.BLACK, PINK,    lovePercent,    20,
                346, true);
        drawBar(g, Color.BLACK, YELLOW,  relativeLength, 160,
                346, true);

        //draws all circles
        //repetition circle
        drawCircle(g, Color.BLACK, BLUE, repetitionOutOf360, 310, 108);
        //other circle
        drawCircle(g, Color.BLACK, ORANGE, (int)((violencePercent * 360) / 100),
                310, 348);

        //draws bar and circle label backgrounds
        g2.setStroke(new BasicStroke(3));

        drawLabelRectangle(g, 30,  286);
        drawLabelRectangle(g, 170, 286);
        drawLabelRectangle(g, 358, 286);
        drawLabelRectangle(g, 30,  526);
        drawLabelRectangle(g, 170, 526);
        drawLabelRectangle(g, 358, 526);

        //draws bar and circle label text
        g.setColor(Color.WHITE);
        g.setFont(italicLabelFont1);
        g.drawString("Mood", 38,  317);
        g.drawString("#@$%", 177, 317);
        g.setFont(plainLabelFont);
        g.drawString("♡♥♡", 35,  559);      // Special unicode characters
        g.drawString("⚠⚠⚠", 363, 558);      // are not in italics
        g.setFont(italicLabelFont2);
        g.drawString("Length", 176, 556);
        g.setFont(italicLabelFont3);
        g.drawString("Repetition", 360, 314);

        //draws genre & year text
        g.setColor(Color.WHITE);
        g.setFont(subtitleFont);
        g.drawString("Genre",550, 140);
        g.drawString("Year Released", 550, 240);
        g.setFont(italicLabelFont1);
        g.drawString(song.genre, 550, 190);
        g.drawString("" + song.year, 550, 290);

        //draws 'top 5 words' title
        g.setFont(subtitleFont);
        g.drawString("Top 5 Words",550, 340);

        //draws list
        g.setFont(italicLabelFont1);

        for (int i = 0; i < 5; i++)
            g.drawString((i + 1) + ") " + topWords[i].substring(
                    0, 1).toUpperCase() + topWords[i].substring(1).toLowerCase(),
                    545, 390 + (i * 40));

        //draws top white dividing line / rectangle
        g.setColor(Color.WHITE);
        g.fillRect(0,0, width, 5);

        //draws sound icon background
        g.fillRoundRect(width - 500, height - 100, 40, 40,
                10, 10);

        //draws sound icon
        BufferedImage myPicture;

        try
        {
            //changes image depending if muted or not
            if (mp.muted)
                myPicture = ImageIO.read(new File(
                        "res/images/soundOff.png"));
            else
                myPicture = ImageIO.read(new File(
                        "res/images/soundOn.png"));

            //draw sound on/off icon
            g.drawImage(myPicture, width - 500, height - 100,
                    40, 40, this);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sets all colors for the bar
     */
    public void setBarColors()
    {
        //empties all ArrayLists
        wordsList   = new ArrayList<>();
        wordsList2  = new ArrayList<>();
        colorsList  = new ArrayList<>();
        colorsList2 = new ArrayList<>();

        allWords = song.getAllWords();

        //resets repetition count
        repetitionCount = 0;

        //loops through entire ArrayList
        for (int x = 0; x < allWords.length; x++)
        {
            //if word already been used, repeats color
            if (wordsList2.contains(allWords[x]))
            {
                int wordPos = Arrays.asList(allWords).indexOf(allWords[x]);

                //if word hasn't been assigned a color, sets color
                if (colorsList2.get(wordPos).equals(BACKGROUND))
                {
                    //sets previous color
                    int ranNum = (int) (Math.random() * allColors.length);
                    colorsList2.set(wordPos, allColors[ranNum]);
                }

                //sets current color
                currentColor = colorsList2.get(wordPos);
                repetitionCount++;
            } else //if word has not been used, sets to background color
            {
                currentColor = BACKGROUND;
            }

            //adds color
            colorsList2.add(x, currentColor);

            //adds words
            wordsList2.add(allWords[x]);
        }

        //loop through word & color lists and switch orders so that words
        //are drawn from last to first
        for (int i = wordsList2.size() - 1; i >= 0; i--)
        {
            wordsList.add(wordsList2.get(i));
            colorsList.add(colorsList2.get(i));
        }
    }

    /**
     * Draws a bar showing a stat
     *
     * @param g the graphics object
     * @param background background of bar
     * @param foreground foreground showing covered area of bar
     * @param percentage percentage of area covered
     * @param x x location of upper right corner of bar
     * @param y y location of upper right corner of bar
     * @param fromBottom if bar starts from bottom
     */
    public void drawBar (Graphics g, Color background, Color foreground,
                         double percentage, int x, int y, boolean fromBottom)
    {
        g.setColor(background); //background
        g.fillRoundRect(x, y, 120, 200, 12, 12);
        g.setColor(foreground); //area covered
        if (fromBottom)
            g.fillRect(x, y + 200 - (int)percentage * 2, 120,
                (int)percentage * 2);
        else
            g.fillRect(x, y, 120, (int)percentage * 2);
        g.setColor(Color.WHITE); //outline in white
        g.drawRoundRect(x, y, 120, 200, 12, 12);
    }

    /**
     * Draws a circle showing a stat
     *
     * @param g the graphics object
     * @param background background of circle
     * @param foreground foreground showing covered area of circle
     * @param numOutOf360 area covered
     * @param x x location of upper right corner of circle
     * @param y y location of upper right corner of circle
     */
    public void drawCircle (Graphics g, Color background, Color foreground,
                            int numOutOf360, int x, int y)
    {
        g.setColor(background); //background
        g.fillOval(x, y, 200, 200);
        g.setColor(foreground); //area covered
        g.fillArc(x, y, 200, 200, 90,  - (numOutOf360));
        g.setColor(BACKGROUND);
        g.fillOval(x + 40, y + 40, 120, 120); //inside circle
        g.setColor(Color.WHITE); //outlines
        g.drawOval(x, y, 200, 200);
        g.drawOval(x + 40, y + 40, 120, 120);
    }

    /**
     * Draws label rectangle
     *
     * @param g the graphics object
     * @param x x location of upper right corner of rectangle
     * @param y y location of upper right corner of rectangle
     */
    public void drawLabelRectangle (Graphics g, int x, int y)
    {
        g.setColor(BACKGROUND); //background
        g.fillRect(x,  y, 100, 40);
        g.setColor(Color.WHITE); //outline
        g.drawRect(x,  y, 100, 40);
    }

    /**
     * Checks mouse location and sets toolTipText accordingly
     *
     * @param e specific mouse event
     */
    public void mouseMoved (MouseEvent e)
    {
        int xLoc = e.getX();
        int yLoc = e.getY();

        //check x and y locations and set toolTipText based off
        //of them
        if (xLoc > 20 && xLoc < 140 && yLoc > 110 && yLoc < 310)
        {
            //mood bar
            this.setToolTipText((int) posPercent + "% Positive - " +
                    (100 - (int) posPercent) + "% Negative");
        } else if (xLoc > 160 && xLoc < 280 && yLoc > 110 && yLoc < 310)
        {
            //swear bar
            if (swearPercent == 0)      this.setToolTipText("No profanity");
            else if (swearPercent < 25) this.setToolTipText("Slight profanity");
            else if (swearPercent < 50) this.setToolTipText("Some profanity");
            else                        this.setToolTipText("Very profane");
        } else if (xLoc > 20 && xLoc < 140 && yLoc > 350 && yLoc < 550)
        {
            //romanticism bar
            if (lovePercent == 0)      this.setToolTipText("No romance");
            else if (lovePercent < 25) this.setToolTipText("Slight romance");
            else if (lovePercent < 50) this.setToolTipText("Some romance");
            else                       this.setToolTipText("Very romantic");
        } else if (xLoc > 160 && xLoc < 280 && yLoc > 350 && yLoc < 550)
        {
            //length bar
            this.setToolTipText(song.lyrics.length() + " words");
        } else if (xLoc > 310 && xLoc < 510 && yLoc > 110 && yLoc < 310)
        {
            //repetition circle
            this.setToolTipText(repetitionCount + " words repeated");
        } else if (xLoc > 310 && xLoc < 510 && yLoc > 350 && yLoc < 550)
        {
            //violence circle
            if (violencePercent == 0)      this.setToolTipText("No violence");
            else if (violencePercent < 25) this.setToolTipText("Slight violence");
            else if (violencePercent < 50) this.setToolTipText("Some violence");
            else                           this.setToolTipText("Very violent");
        } else if (yLoc < 85)
        {
            //repetition scroll bar
            this.setToolTipText("Repetition in lyrics");
        } else if (xLoc > width - 500 && xLoc < width - 460 && yLoc > height - 100
                && yLoc < width - 460)
        {
            if (mp.muted) this.setToolTipText("Sound off");
            else this.setToolTipText("Sound on");

            //sets cursor to hand
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else
        {
            //if not on specific area, clears toolTipText
            this.setToolTipText("");

            //sets cursor to default
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }


    /**
     * Reacts based on where the users mouse is clicked
     *
     * @param e specific action being acted upon
     */
    public void mouseClicked (MouseEvent e)
    {
        int xLoc = e.getX();
        int yLoc = e.getY();

        //check x and y locations and react based off of them

        if (xLoc > width - 500 && xLoc < width - 460 && yLoc > height - 100
                && yLoc < width - 460)
        {
            if (mp.muted)
            {
                mp.muted = false;

                //starts playing music
                if (new File("res/music/" + song.title + "-" +
                        song.artist + ".wav").exists())
                    mp.playSong(song.title + "-" + song.artist);
                else
                    mp.playSong(song.genre + "BGMusic");
            } else
            {
                mp.muted = true;

                //stops old background music
                if (new File("res/music/" + song.title + "-" +
                        song.artist + ".wav").exists())
                    mp.stopSong(song.title + "-" + song.artist);
                else
                    mp.stopSong(song.genre + "BGMusic");
            }
        }
    }

    public void mouseExited (MouseEvent e) {}
    public void mouseDragged (MouseEvent e) {}
    public void mousePressed (MouseEvent e) {}
    public void mouseEntered (MouseEvent e) {}
    public void mouseReleased (MouseEvent e) {}
}