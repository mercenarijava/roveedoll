package com.android.gjprojection.roveedoll.features.free_line.views;

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
import android.widget.FrameLayout;

import com.android.gjprojection.roveedoll.R;

import java.util.ArrayList;

public class FreeLineView extends FrameLayout implements View.OnTouchListener {
    public static final int DEFAULT_SPEED = 50;

    ///////// PAINTERS /////////////
    private @NonNull
    Paint gridPaint;
    private @NonNull
    Paint linePaint;

    ///////// ATTRIBUTES ////////////
    private ArrayList<PointScaled> points = new ArrayList<>();
    private int gridLineWidth;
    private int gridLineGap;
    private int speed = DEFAULT_SPEED;

    public FreeLineView(
            Context context) {
        super(context);
        init(context);
    }

    public FreeLineView(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FreeLineView(
            Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(
            @NonNull final Context context) {
        this.setWillNotDraw(false);
        this.gridLineWidth = context.getResources()
                .getInteger(R.integer.free_line_view_grid_line_width);
        this.gridLineGap = context.getResources()
                .getInteger(R.integer.free_line_view_grid_square_width);
        this.gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.gridPaint.setColor(ContextCompat.getColor(context, R.color.gridLineColor));
        this.linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.linePaint.setColor(ContextCompat.getColor(context, R.color.lineColor));
        this.linePaint.setStrokeWidth(context.getResources()
                .getInteger(R.integer.free_line_view_line_width));
        setOnTouchListener(this);
    }

    public void clear() {
        this.points.clear();
        invalidate();
    }

    public void undo() {
        if (points.size() == 0) return;
        this.points.remove(points.size() - 1);
        invalidate();
    }

    public void setSpeed(final int speed) {
        this.speed = speed;
    }

    private void drawGrid(
            @NonNull Canvas canvas,
            final boolean vertical) {

        float gap_sum = gridLineGap;
        while (gap_sum < (vertical ? getWidth() : getHeight())) {
            canvas.drawLine(
                    vertical ? gap_sum : 0,
                    vertical ? 0 : gap_sum,
                    vertical ? gap_sum + gridLineWidth : getWidth(),
                    vertical ? getHeight() : gap_sum + gridLineWidth,
                    gridPaint
            );
            gap_sum += gridLineGap;
        }
    }

    private void drawLine(
            @NonNull Canvas canvas) {
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

    private void addPoint(
            final float x,
            final float y) {
        points.add(new PointScaled(x, y, 1));
        invalidate();
    }

    @Override
    public boolean onTouch(
            View view,
            MotionEvent motionEvent) {
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
    protected void onDraw(
            Canvas canvas) {
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
