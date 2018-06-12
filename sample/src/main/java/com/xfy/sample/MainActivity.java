package com.xfy.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.xfy.androidperformance.FPSMoniterBuilder;
import com.xfy.androidperformance.util.ShakeManager;

/**
 * Created by XiongFangyu on 2018/6/11.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        Button startBtn = new Button(this);
        layout.addView(startBtn);

        Button stop = new Button(this);
        layout.addView(stop);
        setContentView(layout);

        startBtn.setText("start");
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean result = new FPSMoniterBuilder(MainActivity.this, new WPAdapter())
                        .setAutoStartWhenShakePhone(true)
                        .setUpdateDelay(0)
                        .setMaxFPSDataSize(128)
                        .build(true);
                Log.d("MainActivity", "click result: " + result);
            }
        });

        stop.setText("stop");
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShakeManager.release();
            }
        });
    }
}
