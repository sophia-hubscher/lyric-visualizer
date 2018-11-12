/**
 * Song object holds title, artist, genre & lyrics
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class Song
{
    String title, artist, genre, lyrics;
    int    year;

    /**
     * Holds all info related to specific song
     *
     * @param title song title
     * @param artist singer, band, etc of song
     * @param genre song genre
     * @param year year song was released
     * @param lyrics all lyrics of song - each line separated by \n
     */
    public Song (String title, String artist, String genre, String year,
                 String lyrics)
    {
        this.title  = title;
        this.artist = artist;
        this.genre  = genre;
        this.year   = Integer.parseInt(year);
        this.lyrics = lyrics;
    }

    /**
     * Returns all the words in an array
     *
     * @return array of all words
     */
    public String[] getAllWords()
    {
        String[] allWords = lyrics.split("\\s+");

        //gets rid of all non-letter characters and sets all
        //words to lowercase
        for (int i = 0; i < allWords.length - 1; i++)
        {
            allWords[i].replaceAll("[^a-zA-Z]", "");
            allWords[i].toLowerCase();
        }

        return allWords;
    }

    /**
     * Gets colors based on the genre of the song and returns an array of
     * the rgb values
     *
     * @return array of rgb values
     */
    public int[] getGenreColor()
    {
        int[] colorArray = {0, 0, 0};
                    //if color not set, highlight color = black

        //sets array of rgb values depending on genre
        if (genre.equals("Pop"))              colorArray = new int[] {211, 54,  130};
        else if (genre.equals("Metal"))       colorArray = new int[] {0,   0,   0};
        else if (genre.equals("Alternative")) colorArray = new int[] {42,  161, 152};
        else if (genre.equals("Rap"))         colorArray = new int[] {181, 137, 0};
        else if (genre.equals("Indie"))       colorArray = new int[] {133, 153, 0};
        else if (genre.equals("R&B"))         colorArray = new int[] {108, 113, 196};
        else if (genre.equals("Rock"))        colorArray = new int[] {220, 50,  47};
        else if (genre.equals("Electronic"))  colorArray = new int[] {203, 75,  22};

        //returns the array of rgb values
        return colorArray;
    }
}