package com.xfy.androidperformance;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.xfy.androidperformance.util.ShakeManager;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
class DefaultShakeListener implements ShakeManager.OnShakeListener {
    private Context context;
    private FPSConfigs configs;
    DefaultShakeListener(Context context, FPSConfigs fpsConfigs) {
        this.configs = fpsConfigs;
        this.context = context;
    }

    @Override
    public void onShake() {
        FPSMoniter.start(context, configs);
        ShakeManager.release();
    }

    @Override
    public long getNextShakeDelay() {
        return Long.MAX_VALUE;
    }
}
