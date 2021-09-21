package com.example.rm.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;


public class VirtualKeyView extends View{

    private Paint mPaint;
    Context mContext;


    public VirtualKeyView(Context context) {
        super(context);
    }

    public VirtualKeyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VirtualKeyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext=context;
    }


    //测量

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    //绘制


    @Override
    protected void onDraw(Canvas canvas) {
        //画圆
        //初始化画笔 文字
        Paint pa = new Paint();
        //圆
        Paint pa1 = new Paint();
        float wi = canvas.getWidth()/2;
        float he = canvas.getHeight()/2;
        pa.setColor(Color.BLACK);
        pa.setARGB(255, 199, 33, 56);
        pa1.setColor(Color.BLUE);
        pa.setAntiAlias(true);                       //设置画笔为无锯齿
        pa1.setAntiAlias(true);
        canvas.drawColor(Color.YELLOW);
        canvas.drawCircle(30, 30,60, pa);
        canvas.drawCircle(wi, he, 60, pa1);
        canvas.drawText("1508A大神养成记", wi, he, pa);
        super.onDraw(canvas);

    }
    //定位


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //监听

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


}