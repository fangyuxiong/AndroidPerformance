package com.xfy.androidperformance.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.xfy.androidperformance.FPSConfigs;
import com.xfy.androidperformance.R;
import com.xfy.androidperformance.util.LongArray;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class FPSMonitorView extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    private Callback callback;
    private FPSConfigs fpsConfigs;

    private View initView;
    private TextView frameTv;

    private View detailView;
    private FPSDataView dataView;
    private TextView detailMaxTv;
    private TextView cacheTv;
    private SeekBar cacheSeek;
    private TextView refreshTv;
    private SeekBar refreshSeek;

    private boolean inDetail = false;
    private boolean destroyed = false;

    public FPSMonitorView(@NonNull Context context, @NonNull FPSConfigs fpsConfigs) {
        super(context);
        this.fpsConfigs = fpsConfigs;
        init(context);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    private void init(Context context) {
        setBackgroundColor(0xa0000000);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.fpsap_monitor_init_layout, this);
        initView = findViewById(R.id.init_view);
        frameTv = (TextView) findViewById(R.id.fpsap_tv);
        findViewById(R.id.fpsap_detail).setOnClickListener(this);
        findViewById(R.id.fpsap_close).setOnClickListener(this);
    }

    private void initDetailView() {
        if (detailView == null) {
            Context context = getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.fpsap_monitor_detail_layout, this);
            detailView = findViewById(R.id.detail_view);
            dataView = (FPSDataView) findViewById(R.id.data_view);
            detailMaxTv = (TextView) findViewById(R.id.max_time_tv);
            cacheTv = (TextView) findViewById(R.id.cache_data_tv);
            cacheSeek = (SeekBar) findViewById(R.id.cache_data_seek);
            refreshTv = (TextView) findViewById(R.id.refresh_delay_tv);
            refreshSeek = (SeekBar) findViewById(R.id.refresh_delay_seek);
            dataView.setFPSConfigs(fpsConfigs);
            cacheSeek.setMax(200);
            cacheSeek.setOnSeekBarChangeListener(this);
            cacheSeek.setProgress(fpsConfigs.maxFPSDataSize - 10);
            refreshSeek.setMax(1000);
            refreshSeek.setOnSeekBarChangeListener(this);
            refreshSeek.setProgress(fpsConfigs.updateDelay <= 16 ? 0 : (int) (fpsConfigs.updateDelay - 16));
            findViewById(R.id.detail_close).setOnClickListener(this);
            findViewById(R.id.clear_btn).setOnClickListener(this);
        }
    }

    private void showDetailView() {
        initDetailView();
        removeView(initView);
        if (detailView.getParent() == null) {
            addView(detailView);
        }
//        initView.setVisibility(GONE);
//        detailView.setVisibility(VISIBLE);
        inDetail = true;
    }

    private void showInitView() {
        if (initView.getParent() == null) {
            addView(initView);
        }
        if (detailView != null) {
            removeView(detailView);
        }
        inDetail = false;
    }

    public void refreshData(LongArray data, long maxValue) {
        if (destroyed)
            return;
        final long last = data.get(data.size() - 1);
        if (last <= 0)
            return;

        if (!inDetail) {
            refreshInitView(last);
        } else {
            refreshDetail(data, maxValue);
        }
    }

    private void refreshInitView(long last) {
        int fps = (int) (1000 / last);
        fps = fps > 60 ? 60 : fps;
        frameTv.setText("" + fps);
    }

    private void refreshDetail(LongArray data, long maxValue) {
        dataView.refreshData(data, maxValue);
        detailMaxTv.setText("最大耗时:" + maxValue + "ms");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fpsap_detail) {
            showDetailView();
        } else if (id == R.id.fpsap_close) {
            onClose();
        } else if (id == R.id.detail_close) {
            showInitView();
        } else if (id == R.id.clear_btn) {
            if (callback != null) {
                callback.onClearData();
            }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == cacheSeek) {
            int d = 10 + progress;
            cacheTv.setText("缓存数据: " + d);
            fpsConfigs.maxFPSDataSize = d;
        } else if (seekBar == refreshSeek) {
            int d = 16 + progress;
            refreshTv.setText("刷新延迟：" + d);
            fpsConfigs.updateDelay = d;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == cacheSeek && callback != null) {
            callback.onSetMaxCache();
        }
    }

    private void onClose() {
        if (callback != null) {
            callback.onClose();
        }
        destroyed = true;
        callback = null;
        fpsConfigs = null;
    }

    public interface Callback {
        void onClose();
        void onClearData();
        void onSetMaxCache();
    }
}
