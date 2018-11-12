import java.util.*;

/**
 * Compares values in a map
 *
 * @author Sophia Hübscher
 * @version 1.0
 */
class MapComparator implements Comparator
{

    Map map;

    /**
     * Compares values in a map
     *
     * @param map
     */
    public MapComparator(Map map)
    {
        this.map = map;
    }

    /**
     * Compares two values from a map to one another
     *
     * @param key1 key being compared to key2
     * @param key2 key being compared to key1
     * @return if key1 > key2 --> 1, if key1 = key2 --> 0, if key1 < key2 --></> -1
     */
    public int compare (Object key1, Object key2)
    {
        //compares values from the map
        Comparable val1 = (Comparable) map.get(key1);
        Comparable val2 = (Comparable) map.get(key2);

        //returns 1, 0, or -1
        return val2.compareTo(val1);
    }
}