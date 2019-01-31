package com.android.gjprojection.roveedoll.features.manual.Widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;


public class ProssimityControl extends View {

    private Paint paint = new Paint();
    private float center_x, center_y;
    private final RectF oval = new RectF();
    private Path path = new Path();

    public ProssimityControl(Context context) {
        super(context);
        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setAlpha(0);
    }

    @Override
    synchronized protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        radius = height / 5;
        path.addCircle(width / 2, height / 2, radius, Path.Direction.CW);
        center_x = width / 2;
        center_y = height / 2;
        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);
        canvas.drawArc(oval, 180, 180, false, paint);
    }

    public void onIdentifiedObjectProssimity(int distance) {
        paint.setColor(Color.RED);
        ObjectAnimator anim = ObjectAnimator.ofInt(this,
                "visibility", 0, distance);
        anim.setDuration(3000); // duration 3 seconds
        anim.start();
    }

    public void setVisibility(final int f) {
        paint.setAlpha(f);
        invalidate();
    }


}
