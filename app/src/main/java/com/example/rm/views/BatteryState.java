package com.example.rm.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.rm.MainActivity;
import com.example.rm.R;

public class BatteryState extends View {
    private Context mContext;
    private float width;
    private float height;
    private Paint mPaint;
    private float powerQuantity=0.5f;//电量

    public BatteryState(Context context) {
        super(context);
        mContext=context;
        mPaint = new Paint();

    }

    public BatteryState(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
        mPaint = new Paint();
    }

    public BatteryState(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext=context;
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    计算控件尺寸
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//绘制界面
//        Bitmap batteryBitmap= Bitmap.createBitmap()//读取图片资源
//        width=batteryBitmap.getWidth();
//        height=batteryBitmap.getHeight();
        if (powerQuantity>0.3f&&powerQuantity<=1) {
//      电量少于30%显示红色
            mPaint.setColor(Color.GREEN);
        }
        else if (powerQuantity>=0&&powerQuantity<=0.3)
        {
            mPaint.setColor(Color.RED);
        }
//    计算绘制电量的区域
        float right=width*0.94f;
        float left=width*0.21f+(right-width*0.21f)*(1-powerQuantity);
        float tope=height*0.45f;
        float bottom=height*0.67f;

        canvas.drawRect(left,tope,right,bottom,mPaint);
//        canvas.drawBitmap(batteryBitmap, 0, 0, mPaint);
    }

}
