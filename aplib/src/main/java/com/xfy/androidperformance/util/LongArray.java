package com.xfy.androidperformance.util;

import java.util.Arrays;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class LongArray implements Cloneable{
    private long[] mValues;
    private int mSize;

    /**
     * Creates a new LongArray containing no mappings.
     */
    public LongArray() {
        this(10);
    }

    /**
     * Creates a new LongArray containing no mappings that will not
     * require any additional memory allocation to store the specified
     * number of mappings.  If you supply an initial capacity of 0, the
     * sparse array will be initialized with a light-weight representation
     * not requiring any additional array allocations.
     */
    public LongArray(int initialCapacity) {
        if (initialCapacity == 0) {
            mValues = ContainerHelpers.EMPTY_LONGS;
        } else {
            mValues = new long[initialCapacity];
        }
        mSize = 0;
    }

    @Override
    public LongArray clone() {
        LongArray clone = null;
        try {
            clone = (LongArray) super.clone();
            clone.mValues = mValues.clone();
        } catch (CloneNotSupportedException cnse) {
            /* ignore */
        }
        return clone;
    }

    /**
     * Gets the long mapped from the specified key, or <code>0</code>
     * if no such mapping has been made.
     */
    public long get(int key) {
        return get(key, 0);
    }

    /**
     * Gets the long mapped from the specified key, or the specified value
     * if no such mapping has been made.
     */
    public long get(int key, long valueIfKeyNotFound) {
        if (key < 0 || key >= mSize) {
            return valueIfKeyNotFound;
        } else {
            return mValues[key];
        }
    }

    /**
     * Removes the mapping at the given index.
     */
    public void removeAt(int index) {
        System.arraycopy(mValues, index + 1, mValues, index, mSize - (index + 1));
        mSize--;
    }

    /**
     * Adds a mapping from the specified key to the specified value,
     * replacing the previous mapping from the specified key if there
     * was one.
     */
    public void put(int index, long value) {
        if (index >= 0) {
            mValues[index] = value;
        } else {
            mValues = GrowingArrayUtils.insert(mValues, mSize, index, value);
            mSize++;
        }
    }

    /**
     * Returns the number of key-value mappings that this SparseIntArray
     * currently stores.
     */
    public int size() {
        return mSize;
    }

    /**
     * Removes all key-value mappings from this SparseIntArray.
     */
    public void clear() {
        mSize = 0;
    }

    /**
     * Puts a key/value pair into the array, optimizing for the case where
     * the key is greater than all existing keys in the array.
     */
    public void append(long value) {
        mValues = GrowingArrayUtils.append(mValues, mSize, value);
        mSize++;
    }

    public long maxValue() {
        long max = Long.MIN_VALUE;
        for (int i = 0; i < mSize; i ++) {
            max = mValues[i] > max ? mValues[i] : max;
        }
        return max;
    }

    public long minValue() {
        long min = Long.MAX_VALUE;
        for (int i = 0; i < mSize; i ++) {
            min = mValues[i] < min ? mValues[i] : min;
        }
        return min;
    }

    public long average() {
        long r = 0;
        for (int i = 0; i < mSize; i ++) {
            r += mValues[i];
        }
        return r / mSize;
    }

    public long[] getValues() {
        if (mSize <= 0)
            return ContainerHelpers.EMPTY_LONGS;
        return Arrays.copyOf(mValues,mSize);
    }
}
