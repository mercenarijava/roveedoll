package com.android.gjprojection.roveedoll.features.free_line.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.android.gjprojection.roveedoll.R;
import com.android.gjprojection.roveedoll.features.free_line.FreeLineViewModel;

import java.util.ArrayList;

public class FreeLineView extends FrameLayout implements View.OnTouchListener {
    public static final int DEFAULT_SPEED = 50;
    public static final int MAX_LINES_ALLOWED = 70;
    private static final int MIN_POINTS_DISTANCE = 50;
    private static long LINE_ID = 1;

    ///////// COMMUNICATION /////////
    private FreeLineViewModel communicationViewModel;

    ///////// PAINTERS /////////////
    private @NonNull
    Paint gridPaint;
    private @NonNull
    Paint linePaint;
    private @NonNull
    Paint legendPaint;
    private @NonNull
    Paint legendTextPaint;

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
                .getInteger(R.integer.free_line_view_line_width_max) / 2);
        this.legendPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.legendPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        this.legendPaint.setStrokeWidth(context.getResources()
                .getInteger(R.integer.free_line_view_line_width_max) / 2);
        this.legendPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.legendTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.legendTextPaint.setTextSize(context.getResources()
                .getInteger(R.integer.free_line_view_line_width_max) * 2);
        this.legendTextPaint.setColor(ContextCompat.getColor(context, R.color.colorPrimary));
        setOnTouchListener(this);
    }

    public ArrayList<PointScaled> getPoints() {
        return points;
    }

    public void setCommunicationViewModel(FreeLineViewModel communicationViewModel) {
        this.communicationViewModel = communicationViewModel;
    }

    private synchronized void updateLinesCount() {
        this.communicationViewModel
                .getLinesCount()
                .setValue(this.points.size() - 1);
    }

    public synchronized void clear() {
        this.points.clear();
        this.communicationViewModel
                .getLinesCount()
                .setValue(0);
        invalidate();
    }

    public synchronized void undo() {
        if (points.size() == 0) return;
        final PointScaled removed = this.points.remove(points.size() - 1);
        this.communicationViewModel
                .getPointDeleted()
                .setValue(removed);
        this.communicationViewModel
                .getLinesCount()
                .setValue(this.points.size() > 0 ? this.points.size() - 1 : 0);
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
            float newWidth = getContext().getResources()
                    .getInteger(R.integer.free_line_view_line_width_max);
            newWidth *= 1f - (points.get(i).speed / 100f);
            this.linePaint.setStrokeWidth(newWidth);
            canvas.drawLine(
                    points.get(i - 1).x * points.get(i - 1).scale,
                    points.get(i - 1).y * points.get(i - 1).scale,
                    points.get(i).x * points.get(i).scale,
                    points.get(i).y * points.get(i).scale,
                    linePaint
            );
        }
    }

    private void drawLegend(
            @NonNull Canvas canvas) {
        float offsetFromBottom = canvas.getHeight() % gridLineGap;
        canvas.drawLine(
                gridLineGap,
                canvas.getHeight() - offsetFromBottom,
                gridLineGap * 2,
                canvas.getHeight() - offsetFromBottom,
                legendPaint
        );
        drawTriangle(canvas, true);
        drawTriangle(canvas, false);
        drawLegendText(canvas);
    }

    private void drawTriangle(
            @NonNull Canvas canvas,
            boolean left) {
        final Path path = new Path();
        float offsetFromBottom = canvas.getHeight() % gridLineGap;
        float tirangleLineWidth = gridLineWidth * 10;
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(
                left ? gridLineGap : gridLineGap * 2,
                canvas.getHeight() - offsetFromBottom);
        path.lineTo(
                left ? gridLineGap + tirangleLineWidth : gridLineGap * 2 - tirangleLineWidth,
                canvas.getHeight() - offsetFromBottom - tirangleLineWidth
        );
        path.lineTo(
                left ? gridLineGap + tirangleLineWidth : gridLineGap * 2 - tirangleLineWidth,
                canvas.getHeight() - offsetFromBottom + tirangleLineWidth
        );
        path.lineTo(
                left ? gridLineGap : gridLineGap * 2,
                canvas.getHeight() - offsetFromBottom
        );
        path.close();

        canvas.drawPath(path, legendPaint);
    }

    private void drawLegendText(
            @NonNull Canvas canvas) {
        float offsetFromBottom = canvas.getHeight() % gridLineGap;
        canvas.drawText(
                "1m",
                gridLineGap + gridLineGap / 3f,
                canvas.getHeight() - offsetFromBottom - gridLineGap / 7f,
                legendTextPaint
        );
    }

    private synchronized void addPoint(
            final float x,
            final float y) {
        final PointScaled newPoint = new PointScaled(x, y, 1, speed, LINE_ID);
        if (points.size() == 0 || isValidPoint(points.get(points.size() - 1), newPoint)) {
            LINE_ID++;
            points.add(newPoint);
            this.communicationViewModel
                    .getPointAdd()
                    .setValue(newPoint);
        }
        updateLinesCount();
        invalidate();
    }

    private boolean isValidPoint(
            @NonNull PointScaled first,
            @NonNull PointScaled second) {
        final double distance =
                Math.sqrt(
                        Math.pow((second.x - first.x), 2) +
                                Math.pow((second.y - first.y), 2)
                );
        return distance > MIN_POINTS_DISTANCE && points.size() <= MAX_LINES_ALLOWED;
    }

    @Override
    public boolean onTouch(
            View view,
            MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP: {
                if (speed != 0)
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
        drawLegend(canvas);
    }

    public static class PointScaled extends Point {
        float scale;
        int speed;
        long id;

        PointScaled(float x, float y, float scale, int speed, long lineId) {
            super((int) x, (int) y);
            this.scale = scale;
            this.speed = speed;
            this.id = lineId;
        }

        public long getId() {
            return id;
        }
    }
}
