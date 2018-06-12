package com.xfy.androidperformance.weight;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class TouchMoveListener implements View.OnTouchListener {

    private int initX;
    private int initY;
    private float downX;
    private float downY;

    private WindowManager.LayoutParams layoutParams;
    private WindowManager windowManager;

    public TouchMoveListener(WindowManager windowManager, WindowManager.LayoutParams params) {
        layoutParams = params;
        this.windowManager = windowManager;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                initX = layoutParams.x;
                initY = layoutParams.y;
                downX = event.getRawX();
                downY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = event.getRawX() - downX;
                final float y = event.getRawY() - downY;
                layoutParams.x = (int) (initX + x);
                layoutParams.y = (int) (initY + y);
                windowManager.updateViewLayout(v, layoutParams);
                break;
        }
        return false;
    }
}
