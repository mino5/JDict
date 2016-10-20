package utils.other;

import android.util.Pair;

import java.util.Comparator;

public class StringLengthComparator implements Comparator<Pair<String, String>> {
    @Override
    public int compare(Pair<String, String> x, Pair<String, String> y) {
        // Assume neither string is null. Real code should
        // probably be more robust
        // You could also just return x.length() - y.length(),
        // which would be more efficient.
        if (x.first.length() < y.first.length()) {
            return 1;
        }
        if (x.first.length() > y.first.length()) {
            return -1;
        }
        return 0;
    }
}