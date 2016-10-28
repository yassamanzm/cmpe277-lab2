package com.sjsu.cmpe277.campusmap.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

// http://gamedev.stackexchange.com/questions/29750/fitting-android-game-to-different-screen-sizes
public class CampusView extends ImageView implements View.OnTouchListener {

    public CampusView(Context context) {
        super(context);
    }

    public CampusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CampusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        float leftX = 109;
        float topY = 750;
        float rightX = 264;
        float bottomY = 961;
        canvas.drawRect(leftX, topY, rightX, bottomY, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        float x= event.getX();
        float y= event.getY();
        String coordinates= "(" + x + ", " + y + ")";
        Toast.makeText(getContext(), coordinates, Toast.LENGTH_SHORT).show();

        return true;
    }
}
