package com.android.gjprojection.roveedoll.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.BaseInterpolator;

public class AnimatorsUtils {
    public static void animateBackgroundTint(
            @NonNull final View view,
            final int colorFrom,
            final int colorTo,
            final int duration) {
        final ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundTintList(
                        ColorStateList.valueOf((int) animator.getAnimatedValue())
                );
            }

        });
        colorAnimation.start();
    }

    public static void animateX(
            @NonNull final View view,
            final float newX,
            final int duration,
            final BaseInterpolator interpolator) {
        final ObjectAnimator o = ObjectAnimator.ofFloat(
                view,
                "x",
                newX)
                .setDuration(duration);
        o.setInterpolator(interpolator);
        o.start();
    }
}
