package ru.itmo.java;

import java.util.Map;
import java.util.Objects;

import static java.lang.Math.abs;

public class HashTable {
    private static final int STEP = 701;
    private Entry[] table;
    private int keysNum = 0;
    private int removedKeysNum = 0;
    private int capacity;
    private double loadFactor = 0.5;

    public HashTable(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.table = new Entry[capacity];
        this.loadFactor = loadFactor;
    }

    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new Entry[capacity];
    }

    public Object put(Object key, Object value) {
        int index = getHash(key);
        for (; table[index] != null && !key.equals(table[index].key); index = getNextIndex(index)) {
        }

        if (table[index] != null) {
            Object lastValue = table[index].value;
            table[index].value = value;
            return lastValue;
        }

        Entry newEntry = new Entry(key, value);
        table[index] = newEntry;

        ++keysNum;
        if ((double) (keysNum + removedKeysNum) / (double) capacity - loadFactor > 1e-5) {
            removedKeysNum = 0;
            resize(1);
        }
        if ((double) keysNum / (double) capacity - loadFactor > 1e-5) {
            removedKeysNum = 0;
            resize(2);
        }

        return null;
    }

    public Object get(Object key) {
        int hash = getHash(key);
        int index = hash;
        if (table[index] != null && key.equals(table[index].key)) {
            return table[index].value;
        }

        do {
            index = getNextIndex(index);
        } while ((table[index] == null || !key.equals(table[index].key)) && index != hash);

        if (index == hash) {
            return null;
        }
        return table[index].value;
    }

    public Object remove(Object key) {
        int hash = getHash(key);
        int index = hash;
        if (table[index] != null && key.equals(table[index].key)) {
            Object value = table[index].value;
            table[index] = new Entry();
            --keysNum;
            ++removedKeysNum;
            return value;
        }

        for (index = getNextIndex(index);
             table[index] != null && !key.equals(table[index].key) && index != hash;
             index = getNextIndex(index)) {}

        if (index == hash || table[index] == null) {
            return null;
        }
        Object value = table[index].value;
        table[index] = new Entry();
        --keysNum;
        ++removedKeysNum;
        return value;
    }

    public int size() {
        return this.keysNum;
    }

    private int getHash(Object key) {
        return abs(key.hashCode() % capacity);
    }

    private void resize(int resizeFactor) {
        capacity *= resizeFactor;
        Entry[] newArray = new Entry[capacity];

        for (Entry entry : table) {
            if (entry == null || entry.key == null) {
                continue;
            }
            int index = getHash(entry.key);
            for (; newArray[index] != null; index = getNextIndex(index)) {
            }
            newArray[index] = entry;
        }

        table = newArray;
    }

    private int getNextIndex(int index) {
        return (index + STEP) % capacity;
    }

    private static class Entry {
        private Object key;
        private Object value;

        public Entry() {
            this.key   = null;
            this.value = null;
        }
        public Entry(Object key, Object value) {
            this.key   = key;
            this.value = value;
        }
    }
}
