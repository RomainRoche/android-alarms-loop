package com.romainroche.alarms;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by rroche on 11/08/2017.
 */

public class ColorChangingView extends FrameLayout {

    protected int index = 0;
    private int[] colors = new int[] {R.color.background0, R.color.background1};
    private long[] durations = new long[] {(long)(1.0 * 60 * 1000.0)};

    protected long timestamp, remainingTime;
    protected long targetTime = (long)0;

    public boolean isOn = false;

    private View circle;

    public ColorChangingView(Context context, AttributeSet attrs) {

        super(context, attrs);

        this.colors = new int[] {R.color.background0, R.color.background1};
        this.setBackgroundResource(this.colors[0]);
        this.remainingTime = this.durations[0];

    }

    private int getCurrentColorResource() {
        return this.colors[this.index % this.colors.length];
    }

    private int getNextColorResource() {
        return this.colors[(this.index + 1) % this.colors.length];
    }

    private long getCurrentDuration() {
        return this.durations[this.index % this.durations.length];
    }

    public void setColors(int[] colors) {
        if (colors == null || colors.length == 0) {
            colors = new int[] {R.color.background0, R.color.background1};
        } else if (colors.length == 1) {
            colors = new int[] {colors[0], R.color.background1};
        }
        this.colors = colors;
        this.setBackgroundResource(this.colors[0]);
    }

    public void setDurations(long[] durations) {
        if (durations == null || durations.length == 0) {
            durations = new long[] {1 * 60 * 1000};
        }
        this.durations = durations;
        this.remainingTime = this.durations[0];
    }

    public void play() {
        this.timestamp = System.currentTimeMillis();
        this.targetTime = this.timestamp + this.remainingTime;
        this.isOn = true;
        this.invalidate();
    }

    public void pause() {
        this.isOn = false;
        this.invalidate();
    }

    public void reset() {
        this.isOn = false;
        this.setBackgroundResource(this.colors[0]);
        this.remainingTime = this.durations[0];
        this.invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0 ; i < this.getChildCount() ; i++){
            this.getChildAt(i).layout(l, t, r, b);
        }
    }

    private void doCircle() {

        if (this.circle == null) {
            this.circle = this.findViewById(R.id.circle);
        }

        // the side of the square containing the circle which perfectly contains the rectangle of
        // the activity itself is equal to the diagonal of the activity's frame
        double radius = Math.sqrt(Math.pow(this.getWidth(),2) + Math.pow(this.getHeight(),2));
        int width = (int)radius;
        int height = width;

        FrameLayout.LayoutParams layout = new FrameLayout.LayoutParams(width, height);
        layout.leftMargin = (this.getWidth() - width) / 2;
        layout.topMargin = (this.getHeight() - height) / 2;
        this.circle.setLayoutParams(layout);

        GradientDrawable bg = (GradientDrawable)this.circle.getBackground();
        bg.setColor(this.getResources().getInteger(this.getCurrentColorResource()));

        final ColorChangingView self = this;

        AnimatorSet scale = (AnimatorSet) AnimatorInflater.loadAnimator(this.getContext(),
                R.animator.scale_up);
        scale.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                self.setBackgroundResource(self.getCurrentColorResource());
                self.circle.setAlpha(0.f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        scale.setTarget(self.circle);
        this.circle.setAlpha(1.f);
        scale.start();

    }

    protected void targetReached() {
        this.index++;
        this.targetTime = this.targetTime + this.getCurrentDuration();
        this.doCircle();
    }

    protected void resetTimeData() {
        this.timestamp = System.currentTimeMillis();
        if (this.timestamp >= this.targetTime) {
            this.targetReached();
        }
        this.setRemainingTime();
    }

    protected void setRemainingTime() {
        this.remainingTime = this.targetTime - this.timestamp;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isOn) {
            this.resetTimeData();
        }
        super.draw(canvas);
    }

}
