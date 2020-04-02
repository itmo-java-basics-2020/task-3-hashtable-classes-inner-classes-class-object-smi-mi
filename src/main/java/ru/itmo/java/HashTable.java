package ru.itmo.java;

import static java.lang.Math.abs;

public class HashTable {
    private static final int STEP = 701;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;
    private static final int RESIZE_FACTOR = 2;
    private static final double EPSILON = 1e-5;
    private Entry[] table;
    private int keysNum = 0;
    private int removedKeysNum = 0;
    private int capacity;
    private double loadFactor;

    public HashTable(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.table = new Entry[capacity];
        this.loadFactor = loadFactor;
    }

    public HashTable(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    public Object put(Object key, Object value) {
        if ((double)(keysNum + removedKeysNum) / capacity - loadFactor > EPSILON) {
            resize();
        }

        int index = findIndex(key);
        if (table[index] == null) {
            table[index] = new Entry(key, value);
            ++keysNum;
            return null;
        }
        Object lastValue = table[index].value;
        table[index].value = value;
        return lastValue;
    }

    public Object get(Object key) {
        int index = findIndex(key);
        if (table[index] == null) {
            return null;
        }
        return table[index].value;
    }

    public Object remove(Object key) {
        int index = findIndex(key);
        if (table[index] == null) {
            return null;
        }
        Object lastValue = table[index].value;
        table[index] = new Entry();
        --keysNum;
        ++removedKeysNum;
        return lastValue;
    }

    public int size() {
        return this.keysNum;
    }

    private int findIndex(Object key) {
        int index = getInitIndex(key);
        while (table[index] != null) {
            if (key.equals(table[index].key)) {
                return index;
            }
            index = getNextIndex(index);
        }
        return index;
    }

    private int getInitIndex(Object key) {
        return abs(key.hashCode() % capacity);
    }

    private int getNextIndex(int index) {
        return (index + STEP) % capacity;
    }

    private void resize() {
        Entry[] oldTable = table;

        capacity *= RESIZE_FACTOR;
        table = new Entry[capacity];
        keysNum = 0;
        removedKeysNum = 0;

        for (Entry entry : oldTable) {
            if (entry != null && entry.key != null) {
                put(entry.key, entry.value);
            }
        }
    }

    private static class Entry {
        private Object key;
        private Object value;

        public Entry() {
            this.key = null;
            this.value = null;
        }

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
