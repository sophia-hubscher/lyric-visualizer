import java.awt.Desktop;
import java.io.*;
import java.util.*;

/**
 * Reads & processes text from txt files
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
public class FileProcessor
{
    BufferedReader br;
    StringBuilder  sb;
    String         line;

    /**
     * Reads all of the text in a txt file and returns it as a string
     *
     * @param fileName
     * @return all of text in file
     */
    public String readFile (String fileName)
    {
        String allText = "";

        //reads text
        try
        {
            //creates all needed variables
            BufferedReader br   = new BufferedReader(new FileReader(fileName));
            StringBuilder  sb   = new StringBuilder();
            String         line = br.readLine();

            //reads all lines
            while (line != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //adds text to string
            allText = sb.toString();

            br.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //returns string of all text in file
        return allText;
    }

    /**
     * Takes song title and returns array of song info
     *
     * @param songTitle title of song being processed
     * @return Array of title, artist, genre, year, and lyrics
     */
    public String[] processSong (String songTitle)
    {
        String[] songInfo = new String[5];
        String allText = readFile("res/allLyrics");

        //adds text of specific song to a string
        int first  = allText.indexOf("*****\n" + songTitle);
        int next = allText.indexOf("*****", first+1);

        String songText = allText.substring(first, next);

        //splits text into individual lines
        String[] lines = songText.split(System.getProperty("line.separator"));

        //creates string of all lyrics
        String lyrics = "";
        for (int i = 6; i < lines.length; i++)
        {
            lyrics += lines[i] + "\n";
        }

        //adds all info to array in order: title, artist, genre, year, lyrics
        for (int i = 0; i < 4; i++)
            songInfo[i] = lines[i + 1];
        songInfo[4] = lyrics; //lyrics

        return songInfo;
    }

    /**
     * Gets all song titles from a file and returns them in an array
     *
     * @return all song titles sorted alphabetically
     */
    public String[] getSongTitles ()
    {
        String allSongTitles = "";

        //reads text
        try
        {
            //creates all needed variables
            br = new BufferedReader(new FileReader("res/songTitles"));
            sb = new StringBuilder();
            line = br.readLine();

            //reads all lines
            while (line != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            //adds text to string
            allSongTitles = sb.toString();

            br.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //turns into array
        String[] titleArray = allSongTitles.split(System.getProperty
                ("line.separator"));
        Arrays.sort(titleArray);

        //returns array of all song titles in file
        return titleArray;
    }

    /**
     * Returns all the words in an array
     *
     * @param fileName name of file with words being read
     * @return array of all words
     */
    public String[] getAllWords(String fileName)
    {
        String allText = readFile(fileName);

        String[] allWords = allText.split("\\s+");

        //gets rid of all non-letter characters or number characters
        //and sets all words to lowercase
        for (int i = 0; i < allWords.length - 1; i++)
        {
            allWords[i].replaceAll("[^a-zA-Z0-9]", "");
            allWords[i].toLowerCase();
        }

        return allWords;
    }

    /**
     * Takes a string, splits it into individual words, and sorts
     * them into an array based on their frequencies
     *
     * @param text string with words sorted by their frequencies
     * @return most frequent strings
     */
    public String[] getWordFrequencies (String text)
    {
        String[] words         = text.split("[^a-zA-Z0-9']+");
        String[] mostFreqWords = new String[10];
        int      counter       = 0;

        //loop through all text and set capitalization and remove
        //special characters
        for (int i = 0; i < words.length; i++)
            words[i] = words[i].toLowerCase();

        Map<String, Integer> wordCounts = new HashMap<>();

        //loops through all words and adds them to hashMaps
        for (String word : words)
        {
            Integer count = wordCounts.get(word);

            //if hashMap empty, set count to 0
            if (count == null) count = 0;

            //add word to hashMap
            wordCounts.put(word, count + 1);
        }

        //sorts map
        Map sortedMap = sortByValue(wordCounts);

        //creates an iterator to sort through the values
        Iterator iterator = sortedMap.entrySet().iterator();

        //goes through all keys and compares them to one another
        while (iterator.hasNext())
        {
            Map.Entry mapEntry = (Map.Entry) iterator.next();

            //gets 5 most frequent words
            if (counter < 5)
                mostFreqWords[counter] = (String)mapEntry.getKey();

            counter++;
        }

        return mostFreqWords;
    }

    /**
     * Sorts map by its values using the MapComparator class
     *
     * @param unsortedMap hashMap to be sorted
     * @return map sorted by value
     */
    public static Map sortByValue (Map unsortedMap)
    {
        Map sortedMap = new TreeMap(new MapComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);

        return sortedMap;
    }

    /**
     * Saves String to new or previously created txt file and
     * opens the file
     *
     * @param fileName name of file being written to
     * @param txt String being written to the file
     */
    public void saveToFile (String fileName, String txt)
    {
        BufferedWriter out = null;

        try
        {
            //create / override file
            out = new BufferedWriter(new FileWriter(
                    fileName + ".txt"));

            //write text to new / overridden file
            out.write(txt);

            out.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //open file
        File file = new File(fileName + ".txt");

        //check if desktop is supported
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }

        //open file
        try
        {
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) desktop.open(file);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}