package com.young.myloading;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class MyView extends View {

    private Paint yesPaint;
    private Point curPoint;
    private Path path = new Path();
    private static boolean isFirst = true;
    private int width;
    private int height;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        yesPaint = new Paint();
        yesPaint.setStrokeCap(Paint.Cap.ROUND);
        yesPaint.setStyle(Paint.Style.FILL);
        yesPaint.setStrokeWidth(50f);
        yesPaint.setAntiAlias(true);
        yesPaint.setColor(Color.GREEN);
        Log.e("young", "init()");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (curPoint == null) {
            startAnimation();
        }
        canvas.drawPath(path, yesPaint);
    }

    private void startAnimation() {

        Point startPoint = new Point(40, 40);
        Point midPoint = new Point(68, 68);
        Point endPoint = new Point(112, 24);

        if (isFirst) {
            path.moveTo(startPoint.getX(), startPoint.getY());
            Log.e("young","path="+path.toString());
        }

        ValueAnimator valueAnimator = ValueAnimator.ofObject(new MyEvaluator(), startPoint, midPoint);
        valueAnimator.setDuration(40/102*1000);
        //valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curPoint = (Point) animation.getAnimatedValue();
                Log.e("young", "curPoint.x=" + curPoint.getX() + " curPoint.y=" + curPoint.getY());
                path.lineTo(curPoint.getX(), curPoint.getY());
                Log.e("young","path="+path.toString());
                invalidate();
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();

    }

    private class MyEvaluator implements TypeEvaluator<Point> {

        @Override
        public Point evaluate(float fraction, Point startValue, Point endValue) {
            Point startP = startValue;
            Point endP = endValue;
            float x = startP.getX() + fraction * (endP.getX() - startP.getX());
            float y = startP.getY() + fraction * (endP.getY() - startP.getY());
            Log.e("young", "x=" + x + " y=" + y);
            return new Point(x, y);
        }
    }
}
