package com.github.JHXSMatthew;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Matthew on 5/03/2017.
 */
public class DataCache<K, V> {

    private boolean valid = true;
    private Map<K, V> map = new HashMap<>();

    public DataCache() {
        valid = true;
    }

    public void invalid() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public V get(K key) {
        return map.get(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public V put(K key, V value) {
        return map.put(key, value);
    }

    public Collection<V> values() {
        return map.values();
    }

    public Set<K> keySet() {
        return map.keySet();
    }
}
