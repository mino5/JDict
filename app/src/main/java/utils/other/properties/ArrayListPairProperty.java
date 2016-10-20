package utils.other.properties;

import android.util.Pair;


/**
 * Created by Dominik on 10/15/2016.
 */

public class ArrayListPairProperty<L, R> extends ArrayListProperty<Pair<L, R>> {

    public void add(L elem1, R elem2) {
        val.add(new Pair<L, R>(elem1, elem2));
    }
}
