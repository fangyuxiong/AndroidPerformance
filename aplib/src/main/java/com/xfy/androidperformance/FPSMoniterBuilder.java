package com.xfy.androidperformance;

import android.annotation.SuppressLint;
import android.content.Context;

import com.xfy.androidperformance.util.ShakeManager;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class FPSMoniterBuilder {
    private Context context;
    private FPSConfigs configs;
    private WindowPermissionAdapter adapter;
    public FPSMoniterBuilder(Context context, WindowPermissionAdapter adapter) {
        this.context = context.getApplicationContext();
        configs = new FPSConfigs();
        this.adapter = adapter;
    }

    public FPSMoniterBuilder setMaxFPSDataSize(int max) {
        configs.maxFPSDataSize = max;
        return this;
    }

    public FPSMoniterBuilder setUpdateDelay(long delay) {
        configs.updateDelay = delay;
        return this;
    }

    public FPSMoniterBuilder setOnFPSCallback(OnFPSCallback callback) {
        configs.onFPSCallback = callback;
        return this;
    }

    public FPSMoniterBuilder setShakeAdapter(ShakeManager.ShakeAdapter shakeAdapter) {
        configs.shakeAdapter = shakeAdapter;
        return this;
    }

    public FPSMoniterBuilder setAutoStartWhenShakePhone(boolean autoStartWhenShakePhone) {
        configs.autoStart = autoStartWhenShakePhone;
        return this;
    }

    @SuppressLint("NewApi")
    public boolean build(boolean showNow) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            return false;
        }
        if (!adapter.checkPermission(context)) {
            return false;
        }
        if (FPSMoniter.hasMoniter())
            return true;
        if (showNow) {
            FPSMoniter.start(context, configs);
        } else {
            final ShakeManager shakeManager = ShakeManager.getInstance(context);
            if (configs.shakeAdapter != null) {
                shakeManager.setShakeAdapter(configs.shakeAdapter);
            }
            shakeManager.setOnShakeListener(new DefaultShakeListener(context, configs));
            shakeManager.startListening();
        }
        return true;
    }
}
