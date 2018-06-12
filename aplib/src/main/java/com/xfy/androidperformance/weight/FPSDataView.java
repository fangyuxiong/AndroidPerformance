package com.xfy.androidperformance.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.xfy.androidperformance.FPSConfigs;
import com.xfy.androidperformance.util.LongArray;

/**
 * Created by XiongFangyu on 2018/6/11.
 */
public class FPSDataView extends View {
    private static final int TEXT_SIZE = 30;
    private final static int LEFT_P = TEXT_SIZE + 10;
    private static final int EACH_HEIGHT = 10;
    private static final int LINE_WIDTH = 5;

    private LongArray data;
    private Path path;
    private Paint textPaint;
    private Paint paint;
    private FPSConfigs fpsConfigs;

    private boolean destroyed = false;

    public FPSDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        path = new Path();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(LINE_WIDTH);
        paint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(TEXT_SIZE);
    }

    public void setFPSConfigs(FPSConfigs fpsConfigs) {
        this.fpsConfigs = fpsConfigs;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int pb = getPaddingBottom();
        final int pt = getPaddingTop();
        canvas.drawColor(Color.BLACK);
        if (data == null)
            return;
        path.reset();
        final int len = data.size();
        final long thisMax = data.maxValue();
        final long thisMin = data.minValue();
        final long offset = thisMax - thisMin;
        final int w = getWidth();
        final int h = getHeight() - pb;
        path.moveTo(w, h);

        int l = len - 1;
        if (fpsConfigs != null) {
            l = fpsConfigs.maxFPSDataSize - 1;
        }
        final float ew = l > 0 ? (w - LEFT_P) / (float)l : 0;
        final float eh = EACH_HEIGHT;

        canvas.drawText(thisMin + "", 0, h, textPaint);
        if (offset != 0) {
            if (thisMax > thisMin)
                canvas.drawText(thisMax + "", 0, pt + (TEXT_SIZE >> 1), textPaint);
            canvas.drawText(data.average() + "", 0, (h - pt) >> 1, textPaint);
        }
        canvas.save();
        canvas.clipRect(LEFT_P, 0, w, h);

        float[] result = new float[len * 2];
        for (int i = len - 1; i >= 0; i --) {
            final long o = data.get(i) - thisMin;

            result[i] = w - (len - i - 1) * ew;
            result[i + 1] = h - o * eh;
            if (result[i + 1] < pt) {
                result[i + 1] = pt;
            }

            path.lineTo(result[i], result[i + 1]);
            if (i == 0) {
                path.lineTo(result[i], h);
            }
//            drawPoint(canvas, result[i], result[i + 1]);
        }
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    void refreshData(LongArray data, long maxValue) {
        if (destroyed)
            return;
        this.data = data;
        invalidate();
    }

    void onClose() {
        destroyed = true;
        data = null;
        fpsConfigs = null;
    }

    private void drawPoint(Canvas canvas, float x, float y) {
        canvas.drawCircle(x, y, LINE_WIDTH, paint);
    }
}
