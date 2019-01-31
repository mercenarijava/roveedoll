package com.android.gjprojection.roveedoll.features.manual.MarceDir;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class DiscreteSliderBackdrop extends FrameLayout {

    private String VALUES[] = {"R","P","1","2","3","4","5"};
    private Paint fillPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint strokePaint = new Paint();
    private int tickMarkCount = 0;
    private float tickMarkRadius = 0.0F;
    private float horizontalBarThickness = 0.0F;
    private int backdropFillColor = 0;
    private int backdropStrokeColor = 0;
    private float backdropStrokeWidth = 0.0F;
    // The x-radius of the oval used to round the corners
    private int xRadius = DisplayUtility.dp2px(getContext(), 8);
    // The y-radius of the oval used to round the corners
    private int yRadius = DisplayUtility.dp2px(getContext(), 8);
    private int discreteSliderBackdropLeftMargin = DisplayUtility.dp2px(getContext(), 32);
    private int discreteSliderBackdropRightMargin = DisplayUtility.dp2px(getContext(), 32);



    public DiscreteSliderBackdrop(Context context) {
        super(context);
        init(context, null);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscreteSliderBackdrop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        int interval = (width - (discreteSliderBackdropLeftMargin+discreteSliderBackdropRightMargin)) / (tickMarkCount-1);

        setUpFillPaint();
        setUpStrokePaint();
        setUpTextPaint();

        canvas.drawRoundRect(new RectF(discreteSliderBackdropLeftMargin,
                        (height/2) - (horizontalBarThickness/2),
                        width - discreteSliderBackdropRightMargin,
                        (height/2) + (horizontalBarThickness/2)),
                xRadius,
                yRadius,
                fillPaint);

        canvas.drawRoundRect(new RectF(discreteSliderBackdropLeftMargin,
                        (height/2) - (horizontalBarThickness/2),
                        width - discreteSliderBackdropRightMargin,
                        (height/2) + (horizontalBarThickness/2)),
                xRadius,
                yRadius,
                strokePaint);

        for(int i=0; i<tickMarkCount; i++){
            canvas.drawText(VALUES[i],discreteSliderBackdropLeftMargin + (i * interval) - 5,height/5,textPaint);
            canvas.drawCircle(discreteSliderBackdropLeftMargin + (i * interval), height/2, tickMarkRadius, fillPaint);
            canvas.drawCircle(discreteSliderBackdropLeftMargin + (i * interval), height/2, tickMarkRadius, strokePaint);
        }

        canvas.drawRoundRect(new RectF(discreteSliderBackdropLeftMargin,
                        (height/2) - ((horizontalBarThickness/2)-DisplayUtility.dp2px(getContext(), 1)),
                        width - discreteSliderBackdropRightMargin,
                        (height/2) + ((horizontalBarThickness/2)-DisplayUtility.dp2px(getContext(), 1))),
                xRadius,
                yRadius,
                fillPaint);
    }

    private void init(Context context, AttributeSet attrs){}

    private void setUpFillPaint(){
        fillPaint.setColor(backdropFillColor);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);
    }

    private void setUpTextPaint(){
        textPaint.setColor(Color.BLACK);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(25);
    }

    private void setUpStrokePaint(){
        strokePaint.setColor(backdropStrokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(backdropStrokeWidth);
    }

    public void setTickMarkCount(int tickMarkCount) {
        this.tickMarkCount = tickMarkCount < 2 ? 2 : tickMarkCount;
    }

    public void setTickMarkRadius(float tickMarkRadius) {
        this.tickMarkRadius = tickMarkRadius < 2.0F ? 2.0F : tickMarkRadius;
    }

    public void setHorizontalBarThickness(float horizontalBarThickness) {
        this.horizontalBarThickness = horizontalBarThickness < 2.0F ? 2.0F : horizontalBarThickness;
    }

    public void setBackdropFillColor(int backdropFillColor) {
        this.backdropFillColor = backdropFillColor;
    }

    public void setBackdropStrokeColor(int backdropStrokeColor) {
        this.backdropStrokeColor = backdropStrokeColor;
    }

    public void setBackdropStrokeWidth(float backdropStrokeWidth) {
        this.backdropStrokeWidth = backdropStrokeWidth < 1.0F ? 1.0F : backdropStrokeWidth;
    }

}