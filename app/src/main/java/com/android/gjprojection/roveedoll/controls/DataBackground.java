package com.android.gjprojection.roveedoll.controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.gjprojection.roveedoll.R;

import java.util.ArrayList;

public class DataBackground extends RelativeLayout implements View.OnTouchListener {
    private static final int LINE_WIDTH = 10;
    private static final int GRID_LINE_WIDTH = 1;
    private static final int GRID_LINE_GAP = 200;

    ///////// PAINTERS /////////////
    private @NonNull
    Paint gridPaint;
    private @NonNull
    Paint linePaint;


    ///////// ATTRIBUTES ////////////
    private ArrayList<PointScaled> points = new ArrayList<>();

    public DataBackground(Context context) {
        super(context);
        init(context);
    }

    public DataBackground(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DataBackground(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(@NonNull final Context context) {
        setWillNotDraw(false);
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setColor(ContextCompat.getColor(context, R.color.gridLineColor));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(ContextCompat.getColor(context, R.color.lineColor));
        linePaint.setStrokeWidth(LINE_WIDTH);
        setOnTouchListener(this);
    }


    private void drawGrid(@NonNull Canvas canvas,
                          final boolean vertical) {

        float gap_sum = GRID_LINE_GAP;
        while (gap_sum < (vertical ? getWidth() : getHeight())) {
            canvas.drawLine(
                    vertical ? gap_sum : 0,
                    vertical ? 0 : gap_sum,
                    vertical ? gap_sum + GRID_LINE_WIDTH : getWidth(),
                    vertical ? getHeight() : gap_sum + GRID_LINE_WIDTH,
                    gridPaint
            );
            gap_sum += GRID_LINE_GAP;
        }
    }

    private void drawLine(@NonNull Canvas canvas) {
        for (int i = 1; i < points.size(); i++) {
            canvas.drawLine(
                    points.get(i - 1).x * points.get(i - 1).scale,
                    points.get(i - 1).y * points.get(i - 1).scale,
                    points.get(i).x * points.get(i).scale,
                    points.get(i).y * points.get(i).scale,
                    linePaint
            );
        }
    }

    private void addPoint(final float x,
                          final float y) {
        points.add(new PointScaled(x, y, 1));
        invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                addPoint(motionEvent.getX(), motionEvent.getY());
            }
            break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawGrid(canvas, true);
        drawGrid(canvas, false);
        drawLine(canvas);
    }

    private static class PointScaled extends Point {
        float scale;

        PointScaled(float x, float y, float scale) {
            super((int) x, (int) y);
            this.scale = scale;
        }
    }
}
