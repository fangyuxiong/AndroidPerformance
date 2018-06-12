package com.xfy.sample;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.xfy.androidperformance.WindowPermissionAdapter;

import java.lang.reflect.Method;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class WPAdapter implements WindowPermissionAdapter {
    @Override
    public boolean checkPermission(Context context) {
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class clazz = Settings.class;
                Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
