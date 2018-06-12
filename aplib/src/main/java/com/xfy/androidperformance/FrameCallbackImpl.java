package com.xfy.androidperformance;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.Choreographer;

import com.xfy.androidperformance.util.LruLongArray;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
class FrameCallbackImpl implements Choreographer.FrameCallback {

    private OnFPSCallback onRefreshListener;
    private LruLongArray data;
    private FPSConfigs fpsConfigs;
    private boolean running;
    private long lastFrameTimeNano;
    private long dataOutputDelay;
    private long maxValue;
    private Random random;

    FrameCallbackImpl(@NonNull FPSConfigs configs, @NonNull OnFPSCallback callback) {
        this.onRefreshListener = callback;
        this.fpsConfigs = configs;
        data = new LruLongArray(configs.maxFPSDataSize);
        running = true;
        lastFrameTimeNano = 0;
        dataOutputDelay = -1;
        Choreographer.getInstance().postFrameCallback(this);
    }

    @Override
    public void doFrame(long frameTimeNanos) {
        if (!running || data == null) {
            destroy();
            return;
        }
//        final long frameTime = testFrameTIme();
        final long frameTime = TimeUnit.NANOSECONDS.toMillis(frameTimeNanos - lastFrameTimeNano);
        if (lastFrameTimeNano != 0 && frameTime >= 16) {
            data.append(frameTime);
            maxValue = frameTime > maxValue ? frameTime : maxValue;
        }
        lastFrameTimeNano = frameTimeNanos;
        if (frameTime >= 16) {
            if (fpsConfigs.updateDelay <= FPSConfigs.MIN_UPDATE_DELAY || dataOutputDelay == -1) {
                outputData();
            } else {
                dataOutputDelay += frameTime;
                if (dataOutputDelay >= fpsConfigs.updateDelay) {
                    outputData();
                }
            }
        }
        Choreographer.getInstance().postFrameCallback(this);
    }

    private long testFrameTIme() {
        if (random == null) {
            random = new Random();
        }
        return random.nextInt(44) + 16;
    }

    private void outputData() {
        dataOutputDelay = 0;
        if (onRefreshListener != null) {
            onRefreshListener.callback(data, maxValue);
        }
        if (fpsConfigs.onFPSCallback != null) {
            fpsConfigs.onFPSCallback.callback(data, maxValue);
        }
    }

    void clearData() {
        if (data != null)
            data.clear();
        lastFrameTimeNano = 0;
        dataOutputDelay = -1;
        maxValue = 0;
    }

    void setMaxCacheCount() {
        if (data != null && fpsConfigs != null)
            data.setMaxSize(fpsConfigs.maxFPSDataSize);
    }

    void stop() {
        running = false;
    }

    void destroy() {
        if (data != null)
            data.clear();
        data = null;
        fpsConfigs = null;
        onRefreshListener = null;
        Choreographer.getInstance().removeFrameCallback(this);
    }
}
