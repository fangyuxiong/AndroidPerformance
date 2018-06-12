package com.xfy.androidperformance;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Choreographer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.xfy.androidperformance.util.LongArray;
import com.xfy.androidperformance.util.ShakeManager;
import com.xfy.androidperformance.weight.FPSMonitorView;
import com.xfy.androidperformance.weight.TouchMoveListener;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
class FPSMoniter implements OnFPSCallback, FPSMonitorView.Callback{

    private static FPSMoniter instance;

    private FrameCallbackImpl frameCallback;
    private FPSMonitorView monitorView;
    private WindowManager windowManager;

    private FPSConfigs configs;
    private Context context;

    static FPSMoniter start(Context context, FPSConfigs fpsConfigs) {
        if (instance == null) {
            synchronized (FPSMoniter.class) {
                if (instance == null) {
                    instance = new FPSMoniter(context, fpsConfigs);
                }
            }
        }
        return instance;
    }

    static boolean hasMoniter() {
        return instance != null;
    }

    private FPSMoniter(Context context, FPSConfigs fpsConfigs) {
        configs = fpsConfigs;
        this.context = context;
        monitorView = new FPSMonitorView(context, fpsConfigs);
        monitorView.setCallback(this);
        addViewToWindow(context, monitorView);
        frameCallback = new FrameCallbackImpl(fpsConfigs, this);
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }

    private void addViewToWindow(Context context, View view) {
        windowManager = (WindowManager) context.getSystemService(Service.WINDOW_SERVICE);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                100, 100,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.LEFT|Gravity.TOP;

        windowManager.addView(view, params);

        view.setOnTouchListener(new TouchMoveListener(windowManager, params));
        view.setVisibility(View.VISIBLE);
        ShakeManager.release();
    }

    @Override
    public void callback(LongArray data, long max) {
        if (monitorView != null) {
            monitorView.refreshData(data, max);
        }
    }

    @Override
    public void onClose() {
        frameCallback.stop();
        windowManager.removeView(monitorView);
        frameCallback = null;
        monitorView = null;
        windowManager = null;
        synchronized (FPSMoniter.class) {
            instance = null;
        }
        if (configs != null && configs.autoStart) {
            listenShake();
        }
        configs = null;
        context = null;
    }

    private void listenShake() {
        final ShakeManager shakeManager = ShakeManager.getInstance(context);
        if (configs.shakeAdapter != null) {
            shakeManager.setShakeAdapter(configs.shakeAdapter);
        }
        shakeManager.setOnShakeListener(new DefaultShakeListener(context, configs));
        shakeManager.startListening();
    }

    @Override
    public void onClearData() {
        if (frameCallback != null)
        frameCallback.clearData();
    }

    @Override
    public void onSetMaxCache() {
        if (frameCallback != null)
        frameCallback.setMaxCacheCount();
    }
}
