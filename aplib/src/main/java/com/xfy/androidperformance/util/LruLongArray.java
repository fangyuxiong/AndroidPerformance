package com.xfy.androidperformance.util;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class LruLongArray extends LongArray {

    private int maxSize = -1;

    public LruLongArray(int maxSize) {
        this.maxSize = maxSize;
    }

    public LruLongArray(int maxSize, int initialCapacity) {
        super(initialCapacity);
        this.maxSize = maxSize;
    }

    public void setMaxSize(int m) {
        this.maxSize = m;
        trimToSize(m);
    }

    @Override
    public void put(int index, long value) {
        super.put(index, value);
        if (maxSize >= 0) {
            trimToSize(maxSize);
        }
    }

    @Override
    public void append(long value) {
        super.append(value);
        if (maxSize >= 0) {
            trimToSize(maxSize);
        }
    }

    public void trimToSize(int size) {
        if (size < 0)
            return;
        if (size == 0) {
            clear();
            return;
        }
        int nowSize = size();
        while (nowSize > size) {
            removeAt(0);
            nowSize = size();
        }
    }
}