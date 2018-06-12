package com.xfy.androidperformance;

import android.support.annotation.Nullable;

import com.xfy.androidperformance.util.ShakeManager;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class FPSConfigs {
    public static final long MIN_UPDATE_DELAY = 16;
    /**
     * 最大帧率数据
     */
    public int maxFPSDataSize = 128;
    /**
     * 帧率显示刷新延迟时间
     * 如果小于17ms，默认每帧都刷新
     */
    public long updateDelay = 0;
    /**
     * 外部监听回调
     */
    public @Nullable OnFPSCallback onFPSCallback;
    /**
     * 自动检测手机摇晃，并开始检测fps
     */
    public boolean autoStart;

    public @Nullable ShakeManager.ShakeAdapter shakeAdapter;
}
