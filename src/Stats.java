import java.util.*;

/**
 * Stats object hold methods that collect and display
 * stats about lyrics
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class Stats
{
    FileProcessor fp = new FileProcessor();

    /**
     * Finds the percent of positive words vs negative
     * words
     *
     * @param allWords all words in a song
     * @return percent of positive words
     */
    public double getPositivePercentage (String[] allWords)
    {
        double posInText = 0;
        double negInText = 0;

        String[] positiveWords;
        String[] negativeWords;

        //set arrays of positive and negative words
        positiveWords = fp.getAllWords("res/statsText/moodText/" +
                                    "positiveWords");
        negativeWords = fp.getAllWords("res/statsText/moodText/" +
                                    "negativeWords");

        //loop through arrays and check if in text
        for (int p = 0; p < allWords.length; p++) //positive
        {
            if (Arrays.toString(positiveWords).toLowerCase().contains
                    (" " + allWords[p].toLowerCase() + ","))
                                            posInText += 1.0;

            if (Arrays.toString(negativeWords).toLowerCase().contains
                    (" " + allWords[p].toLowerCase() + ","))
                negInText += 1.0;
        }

        //if no negative words, return 100%
        if (negInText == 0.0) return 100.0;

        //find percentage
        double percentPositive = (posInText / (negInText + posInText)) * 100;

        if (percentPositive > 100.0) return 100.0;

        return percentPositive;
    }

    /**
     * Gets the length of lyrics relative to the variable: divFactor
     *
     * @param lyrics all lyrics to the song
     * @param divFactor factor length is being divided by
     * @return length out of 100
     */
    public double getRelativeLength (String lyrics, double divFactor)
    {
        double length = (lyrics.length() / divFactor) * 100.0;

        //if too large, return 100
        if (length > 100.0) return 100.0;

        return length;
    }

    /**
     * Gets the percentage of a certain type of word in an array
     * of words
     *
     * @param allWords an array of the words being compared to
     * @param fileName name of the file being compared to the array
     * @param amplification how much it should be multiplied by
     * @return percent of works in array allWords, that also appear in
     *         the specified text file
     */
    public double getTypePercentage (String[] allWords, String fileName,
                                     double amplification)
    {
        double percentage;
        double wordCount = 0.0;
        String[] allOfType;

        //set array to all words of the tupe
        allOfType = fp.getAllWords(fileName);

        //loop through all words and check if they are in the string
        for (int i = 0; i < allWords.length; i++)
        {
            if (Arrays.toString(allOfType).toLowerCase().contains
                    (" " + allWords[i].toLowerCase() + ","))
                wordCount += 1.0;
        }

        //if no words of type, return 100%
        if (wordCount == 0.0) return 0.0;

        //calculate percentage
        percentage = (wordCount / (double)allWords.length) * amplification;

        if (percentage > 100.0) return 100.0;

        return percentage;
    }

    /**
     * Finds percent of a number relative to another out of 360 (used in
     * pie charts)
     *
     * @param smallNum smaller number
     * @param bigNum number that smallNum is being compared to
     * @return number out of 360
     */
    public int getNumOutOf360 (int smallNum, int bigNum)
    {
        int percentage = (int)(((double)smallNum / (double)bigNum) * 100);
        return (percentage * 360) / 100;
    }

    /**
     * Counts words in a string
     *
     * @param s string of all words
     * @return number of words in a string
     */
    public int countWords (String s)
    {
        int wordCount = 0;

        //split string into array of words
        try
        {
            String[] splitArray = s.split("\\s+");
            wordCount = splitArray.length;
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return wordCount;
    }

    /**
     * Creates a String made of ascii characters that
     * shows a graphical representation of a percentage
     *
     * @param percent percentage being represented
     * @return ascii bar
     */
    public String percentTxt (int percent)
    {
        String returnString = "";
        int count = 0;

        //add colored in blocks
        for (int i = 0; i < percent; i += 10)
        {
            returnString += "▓";
            count++;
        }

        //fill rest of the bar with empty blocks
        for (int i = count; i < 10; i++)
            returnString += "░";

        return returnString;
    }
}