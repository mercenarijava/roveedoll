package com.android.gjprojection.roveedoll.features.manual.MarceDir;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

public class DiscreteSeekBar extends AppCompatSeekBar {

    private static String PROGRESS_PROPERTY = "progress";
    private static int MULTIPLIER = 100;
    private int tickMarkCount = 0;
    private float stepSize = 0.0f;
    private int superOldProgress = 0;
    private int fromUserCount = 0;
    private OnDiscreteSeekBarChangeListener onDiscreteSeekBarChangeListener;

    public interface OnDiscreteSeekBarChangeListener {
        void onPositionChanged(int position);
    }

    public DiscreteSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public DiscreteSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DiscreteSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                superOldProgress = seekBar.getProgress();
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    fromUserCount+=1;
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                int oldProgress = seekBar.getProgress();
                final int newProgress;
                if((oldProgress % stepSize) >= stepSize/2F){
                    newProgress = (int)(((oldProgress/(int)stepSize)+1)*stepSize);
                } else {
                    newProgress = (int)(((oldProgress/(int)stepSize))*stepSize);
                }

                if(fromUserCount>1){
                    ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, PROGRESS_PROPERTY, oldProgress, newProgress);
                    animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                } else {
                    ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, PROGRESS_PROPERTY, superOldProgress, newProgress);
                    animation.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                }

                fromUserCount = 0;
                if(onDiscreteSeekBarChangeListener != null){
                    onDiscreteSeekBarChangeListener.onPositionChanged(newProgress/MULTIPLIER);
                }
            }
        });
    }

    public void setTickMarkCount(int tickMarkCount) {
        this.tickMarkCount = tickMarkCount < 2 ? 2 : tickMarkCount;
        setMax((this.tickMarkCount-1) * MULTIPLIER);
        this.stepSize = getMax()/(this.tickMarkCount-1);
    }

    public void setOnDiscreteSeekBarChangeListener(OnDiscreteSeekBarChangeListener onDiscreteSeekBarChangeListener){
        this.onDiscreteSeekBarChangeListener = onDiscreteSeekBarChangeListener;
    }

    public void setPosition(int position){
        this.setProgress(position*(int)stepSize);
    }

}
