package ru.itmo.java;

import java.util.Map;
import java.util.Objects;

import static java.lang.Math.abs;

public class HashTable {
    private Entry[] m_array;
    private int m_keysNum = 0;
    private int m_capacity;
    private double m_loadFactor = 0.5;

    public HashTable(int capacity, double loadFactor) {
        this.m_capacity = capacity;
        this.m_array = new Entry[capacity];
        this.m_loadFactor = loadFactor;
    }

    public HashTable(int capacity) {
        this.m_capacity = capacity;
        this.m_array = new Entry[capacity];
    }

    public Object put(Object key, Object value) {
        if (get(key) == null) {
            ++m_keysNum;
            if ((double) m_keysNum / (double) m_capacity - m_loadFactor > 1e-5) {
                resize();
            }
            Entry newEntry = new Entry(key, value);
            int index = getHash(key);
            for (; m_array[index] != null; index = (index + 1) % m_capacity) {
            }
            m_array[index] = newEntry;
            return null;
        }

        int index = getHash(key);
        for (; m_array[index] == null || !key.equals(m_array[index].getKey()); index = (index + 1) % m_capacity) {
        }
        Object lastValue = m_array[index].getValue();
        m_array[index].setValue(value);
        return lastValue;
    }

    public Object get(Object key) {
        int hash = getHash(key);
        int index = hash;
        if (m_array[index] != null && key.equals(m_array[index].getKey())) {
            return m_array[index].getValue();
        }
        do {
            index = (index + 1) % m_capacity;
        } while ((m_array[index] == null || !key.equals(m_array[index].getKey())) && index != hash);

        if (index == hash) {
            return null;
        }
        return m_array[index].getValue();
    }

    public Object remove(Object key) {
        int hash = getHash(key);
        int index = hash;
        if (m_array[index] != null && key.equals(m_array[index].getKey())) {
            Object value = m_array[index].getValue();
            m_array[index] = null;
            --m_keysNum;
            return value;
        }
        for (index = (index + 1) % m_capacity;
             (m_array[index] == null || !key.equals(m_array[index].getKey())) && index != hash;
             index = (index + 1) % m_capacity)
            ;

        if (index == hash) {
            return null;
        }
        Object value = m_array[index].getValue();
        m_array[index] = null;
        --m_keysNum;
        return value;
    }

    public int size() {
        return m_keysNum;
    }

    private int getHash(Object key) {
        return abs(key.hashCode() % m_capacity);
    }

    private void resize() {
        m_capacity *= 2;
        Entry[] newArray = new Entry[m_capacity];

        for (Entry entry : m_array) {
            if (entry == null) {
                continue;
            }
            int index = getHash(entry.getKey());
            for (; newArray[index] != null; index = (index + 1) % m_capacity) {
            }
            newArray[index] = entry;
        }

        m_array = newArray;
    }

    private static class Entry {
        private Object m_key;
        private Object m_value;

        public Entry(Object key, Object value) {
            this.m_key = key;
            this.m_value = value;
        }

        public void setValue(Object newValue) {
            this.m_value = newValue;
        }

        public Object getKey() {
            return this.m_key;
        }

        public Object getValue() {
            return this.m_value;
        }
    }
}
