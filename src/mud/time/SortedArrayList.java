package mud.time;

import java.util.ArrayList;
import java.util.Collections;

/**
 * http://stackoverflow.com/a/4031849 <-- I got this code from here 
 * @author Japhez
 */
class SortedArrayList<T> extends ArrayList<T> {

    @SuppressWarnings("unchecked")
    public void addSorted(T value) {
        add(value);
        Comparable<T> cmp = (Comparable<T>) value;
        for (int i = size() - 1; i > 0 && cmp.compareTo(get(i - 1)) < 0; i--) {
            Collections.swap(this, i, i - 1);
        }
    }
}