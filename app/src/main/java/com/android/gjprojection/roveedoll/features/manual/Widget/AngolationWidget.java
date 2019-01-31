package com.android.gjprojection.roveedoll.features.manual.Widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

public class AngolationWidget extends View {

    private float x_center;
    private int standard_size = 80;
    private float y_center;
    private Paint paint;
    private float angle;

    public AngolationWidget(Context context) {
        super(context);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        x_center = dm.widthPixels / 2;
        y_center = dm.heightPixels / 2;
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(standard_size);
    }

    public AngolationWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AngolationWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AngolationWidget(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = (int) angle + "";
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        canvas.drawText(text + "°", x_center - (textBounds.width() / 2), y_center - getHeight() / 4, paint);
        super.onDraw(canvas);
    }

    public void setVelocityTextSize(int marcia) {
        if (marcia != 0) {
            paint.setColor(Color.BLACK);
            paint.setTextSize(standard_size - (marcia * 5));
        } else {
            paint.setColor(Color.RED);
        }
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
        angle = rotation;
        invalidate();
    }
}
