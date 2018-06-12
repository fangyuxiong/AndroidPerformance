package com.xfy.androidperformance.util;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class DefaultShakeAdapter implements ShakeManager.ShakeAdapter {
    private static final long MAX_TIME = 200;
    private static final int MAX_SHAKE_COUNT = 3;

    private int minOffset;
    private long startTime;
    private int shakeCount;
    private boolean startFromLeft;

    public DefaultShakeAdapter(int minOffset) {
        this.minOffset = minOffset;
    }

    @Override
    public boolean onChanged(float x, float y, float z) {
        if (shakeCount == 0) {
            if (shakeLeft(x)) {
                startFromLeft = true;
                shakeCount ++;
                startTime = now();
                return false;
            }
            if (shakeRight(x)) {
                startFromLeft = false;
                shakeCount ++;
                startTime = now();
                return false;
            }
            return false;
        } else if (now() - startTime <= MAX_TIME){
            if (startFromLeft) {
                if (shakeCount % 2 == 1 && shakeRight(x)) {
                    shakeCount ++;
                    return check();
                } else if (shakeCount % 2 == 0 && shakeLeft(x)) {
                    shakeCount ++;
                    return check();
                }
            } else {
                if (shakeCount % 2 == 0 && shakeRight(x)) {
                    shakeCount ++;
                    return check();
                } else if (shakeCount % 2 == 1 && shakeLeft(x)) {
                    shakeCount ++;
                    return check();
                }
            }
        } else {
            shakeCount = 0;
        }
        return false;
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private boolean shakeLeft(float x) {
        return x < 0 && x <= -minOffset;
    }

    private boolean shakeRight(float x) {
        return x > 0 && x >= minOffset;
    }

    private boolean check() {
        return shakeCount >= MAX_SHAKE_COUNT;
    }
}
