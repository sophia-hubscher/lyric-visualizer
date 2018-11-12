import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

/**
 * Window holds all buttons, labels, etc
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class Window extends JFrame implements ActionListener
{
    JPanel      masterPanel, componentPanel, lyricPanel;
    JButton     quitButton, searchButton, randomButton, saveButton;
    JComboBox   titleComboBox;
    JTextField  searchField;
    JScrollPane lyricScroll;
    JTextArea   lyricTextArea;

    Timer   t;
    Display display;

    Font lyricFont;

    FileProcessor fp       = new FileProcessor();
    Searcher      searcher = new Searcher();
    Stats         stats    = new Stats();
    MusicPlayer   mp       = new MusicPlayer();

    Color DARK_BLUE        = new Color(0,  43,  54);
    Color highlightColor;

    String[] songArray     = fp.processSong("&burn");
    String[] allTitles     = fp.getSongTitles();
    Song     song          = new Song(songArray[0], songArray[1],
                             songArray[2], songArray[3], songArray[4]);
    String   equalsTxt     = "";
    String   containedTxt  = "";
    int      delayTime     = 50;
    int      width;
    int      height;

    /**
     * Creates master panel & smaller panels with all JComponents
     *
     * @param width width of screen
     * @param height height of screen
     */
    public Window (int width, int height)
    {
        this.width  = width - 440;
        this.height = height;

	//not allowed to resize the window
	this.setResizable(false);

        //can not change the size of the window
        this.setResizable(false);

        lyricFont = new Font("SansSerif", Font.PLAIN, 14);

        //master panel holds all other panels
        masterPanel = (JPanel) this.getContentPane();
        masterPanel.setLayout(new BorderLayout());

        //comboBox holds all titles
        titleComboBox = new JComboBox(allTitles);
        titleComboBox.addActionListener(this);
        titleComboBox.setToolTipText("Titles");
        titleComboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //leaves program
        quitButton = new JButton("Quit");
        quitButton.setFocusPainted(false);
        quitButton.addActionListener(this);
        quitButton.setToolTipText("Quit Lyric Visualizer");
        quitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //searches through titles - text field
        searchField = new JTextField(15);
        searchField.addActionListener(this);
        searchField.setToolTipText("Search Titles");

        //searches through titles - button
        searchButton = new JButton("Search Title");
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(this);
        searchButton.setToolTipText("Search Titles");
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //save text to file - button
        saveButton = new JButton("Save");
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(this);
        saveButton.setToolTipText("Save text as TXT File");
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //searches through titles - button
        randomButton = new JButton("Surprise Me");
        randomButton.setFocusPainted(false);
        randomButton.addActionListener(this);
        randomButton.setToolTipText("Random Song");
        randomButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //component panel holds all major buttons / comboBoxes
        componentPanel = new JPanel();
        componentPanel.setLayout(new GridLayout(1, 1));

        //sets component panel color
        highlightColor = new Color(song.getGenreColor()[0], song.getGenreColor()[1],
                song.getGenreColor()[2]);
        componentPanel.setBackground(highlightColor);

        //area where lyrics are displayed
        lyricTextArea = new JTextArea(song.lyrics, 33, 32);
        lyricTextArea.setBackground(DARK_BLUE);
        lyricTextArea.setFont(lyricFont);
        lyricTextArea.setForeground(Color.WHITE);
        lyricTextArea.setMargin(new Insets(2,2,2,2));
        lyricTextArea.setLineWrap(true);
        lyricTextArea.setWrapStyleWord(true);
        lyricTextArea.setEditable(false);

        //adds the report to scroll panes, so if they get big, they will scroll
        lyricScroll = new JScrollPane(lyricTextArea ,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //adds the scroll pane to the overall panel
        lyricPanel = new JPanel();
        lyricPanel.add(lyricScroll);
        lyricPanel.setBackground(Color.WHITE); //sets color

        //makes a display object that will show board and motion
        display = new Display(width, height, "SansSerif");
        display.setBarColors();
        display.setLayout(new BorderLayout());

        //creating timer
        t = new Timer(delayTime, this);
        t.addActionListener(this);
        t.start();

        //start background music
        if (!mp.muted)
        {
            if (new File("res/music/" + song.title + "-" + song.artist
                    + ".wav").exists())
            {
                mp.playSong(song.title + "-" + song.artist);
            } else
            {
                mp.playSong(song.genre + "BGMusic");
            }
        }

        //adds comboBox and buttons to componentPanel
        componentPanel.add(searchField);
        componentPanel.add(searchButton);
        componentPanel.add(titleComboBox);
        componentPanel.add(randomButton);
        componentPanel.add(saveButton);
        componentPanel.add(quitButton);

        //adds all panels to the master panel
        masterPanel.add(componentPanel, BorderLayout.NORTH);
        masterPanel.add(display,        BorderLayout.CENTER);
        masterPanel.add(lyricPanel,     BorderLayout.WEST);

        //sets title and size of window and set to be visible in the JFrame
        this.setTitle("Lyric Visualizer");
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    /**
     * When a button is pressed, this is automatically called
     *
     * @param e the specific event
     */
    public void actionPerformed (ActionEvent e)
    {
        if (e.getSource() == t)
        {
            //update repetition bar location
            if (display.barXLoc < 0) display.barXLoc++;
            else display.barXLoc = - (display.allWords.length * 8) + width;

            display.repaint();
        } else if (e.getSource() == quitButton)
        {
            System.exit(0);
        } else if (e.getSource() == titleComboBox)
        {
            //stops old background music
            if (!mp.muted)
            {
                if (new File("res/music/" + song.title + "-" +
                        song.artist + ".wav").exists())
                    mp.stopSong(song.title + "-" + song.artist);
                else
                    mp.stopSong(song.genre + "BGMusic");
            }

            //sets song variables
            String songTitle = titleComboBox.getSelectedItem().toString();

            //if ends in a music note, searches for a song without a music note
            //in the title
            if (!(songTitle.indexOf(" ♬") == -1))
                songTitle = songTitle.substring(0, songTitle.indexOf(" ♬"));

            songArray = fp.processSong(songTitle);
            song = new Song(songArray[0], songArray[1], songArray[2],
                    songArray[3], songArray[4]);

            //sets lyric text in panel
            lyricTextArea.setText(song.lyrics);

            //sets variables in display
            display.songArray = songArray;
            display.song      = song;
            display.allWords  = song.getAllWords();
            display.barXLoc   = - (display.allWords.length * 8) + width;

            //sets new grid colors
            display.setBarColors();

            //sets stats in display
            display.posPercent = display.stats.getPositivePercentage
                                                    (display.allWords);
            display.swearPercent = display.stats.getTypePercentage
                    (display.allWords, "res/statsText/swearWords",
                            2200.0);
            display.lovePercent = display.stats.getTypePercentage
                    (display.allWords, "res/statsText/romanceWords",
                            900.0);
            display.relativeLength = display.stats.getRelativeLength(song.lyrics,
                        6000.0);
            display.repetitionOutOf360 = display.stats.getNumOutOf360(
                    display.repetitionCount, display.stats.countWords(song.lyrics));
            display.violencePercent = display.stats.getTypePercentage(
                    display.allWords, "res/statsText/violenceWords",
                            2000.0);

            //sets top 10 most frequent words
            display.topWords = fp.getWordFrequencies(song.lyrics);

            //sets component panel color
            highlightColor = new Color(song.getGenreColor()[0],
                    song.getGenreColor()[1], song.getGenreColor()[2]);
            componentPanel.setBackground(highlightColor);

            //starts new background music
            if (!mp.muted)
            {
                if (new File("res/music/" + song.title + "-" +
                        song.artist + ".wav").exists())
                    mp.playSong(song.title + "-" + song.artist);
                else
                    mp.playSong(song.genre + "BGMusic");
            }
        } else if (e.getSource() == randomButton)
        {
            //random num from 0 to num of songs
            int ranNum = (int)(Math.random() * allTitles.length);

            //set comboBox to random song
            titleComboBox.setSelectedIndex(ranNum);
        } else if (e.getSource() == searchField)
        {
            //when enter is pressed
            search();
        } else if (e.getSource() == searchButton)
        {
            search();
        } else if (e.getSource() == saveButton)
        {
            //save to file
            fp.saveToFile(song.title + "Lyrics",
                /* BASIC INFO */
                song.title + " - " + song.artist + "\n" +
                 song.year + " - " + song.genre + "\n\n---\n\n" +

                /* LYRICS */
                song.lyrics + "\n" +

                /* STATS */
                "---\n\n" +

                /* MOOD */
                ((int) display.posPercent + "% Positive - " +
                (100 - (int) display.posPercent) + "% Negative\n") +

                /* LENGTH / REPETITION */
                song.lyrics.length() + " total words\n" + display.repetitionCount +
                " repeated words\n" +

                /* PERCENTAGE BARS */
                "Profanity: " + stats.percentTxt((int)display.swearPercent) + "\n" +
                "Romance:   " + stats.percentTxt((int)display.lovePercent) + "\n" +
                "Violence:  " + stats.percentTxt((int)display.violencePercent));
        }
    }

    /**
     * Called when user searches for a song title - loops through all
     * song titles using the Searcher class and sets correct song when
     * found.
     */
    public void search ()
    {
        //if empty do not check for equality
        if (searchField.getText().equals("")) return;

        //resets variables
        equalsTxt = "";
        containedTxt = "NOT FOUND";

        //searches for term
        equalsTxt = searcher.doesEqual(searchField.getText(), allTitles);

        //if term not found, searches for text containing the search term
        //otherwise, sets song
        if (equalsTxt.equals("NOT FOUND"))
        {
            searcher.doesContain(searchField.getText(), allTitles);

            //searches in contained text
            containedTxt = searcher.doesContain(searchField.getText(), allTitles);
        } else
        {
            //set index of item in comboBox
            for (int i = 0; i < titleComboBox.getItemCount(); i++)
                if (titleComboBox.getItemAt(i) == equalsTxt)
                    titleComboBox.setSelectedIndex(i);
        }

        //if term found in doesContainText method, set song
        if (!containedTxt.equals("NOT FOUND"))
        {
            //set index of item in comboBox
            for (int i = 0; i < titleComboBox.getItemCount(); i++)
                if (titleComboBox.getItemAt(i) == containedTxt)
                    titleComboBox.setSelectedIndex(i);
        }

        //clears search field
        searchField.setText("");
    }
}