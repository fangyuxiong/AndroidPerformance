package com.xfy.androidperformance;

import com.xfy.androidperformance.util.LongArray;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public interface OnFPSCallback {

    void callback(LongArray data, long max);
}
