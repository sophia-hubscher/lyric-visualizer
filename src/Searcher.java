/**
 * Handles all linear searching in program
 *
 * @author Sophia Hübscher
 * @version 1.1
 */
public class Searcher
{
    /**
     * Checks if a String in an array contains a search term
     *
     * @param searchTerm target word
     * @param allTerms all Strings that the target word is being
     *                 compared against
     * @return String in array that contains the search term
     */
    public String doesContain (String searchTerm, String[] allTerms)
    {
        //loops through all terms - returns if contains
        for (int i = 0; i < allTerms.length; i++)
            if(allTerms[i].toLowerCase().contains(searchTerm.toLowerCase()))
                                    return allTerms[i];

        return "NOT FOUND";
    }

    /**
     * Checks if a String in an array equals a search term
     *
     * @param searchTerm target word
     * @param allTerms all Strings that the target word is being
     *                 compared against
     * @return String in array that equals the search term
     */
    public String doesEqual (String searchTerm, String[] allTerms)
    {
        //loops through all terms - returns if equals
        for (int i = 0; i < allTerms.length; i++)
            if(allTerms[i].toLowerCase().equals(searchTerm.toLowerCase()))
                                    return allTerms[i];

        return "NOT FOUND";
    }
}
