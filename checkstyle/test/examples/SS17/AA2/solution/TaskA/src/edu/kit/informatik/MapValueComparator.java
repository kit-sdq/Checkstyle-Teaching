package edu.kit.informatik;

import java.util.Comparator;
import java.util.Map;

public class MapValueComparator<K, V extends Comparable<? super V>> implements Comparator<Map.Entry<K, V>> {
    @Override
    public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b) {
        return a.getValue().compareTo(b.getValue()) * -1;
    }
}
