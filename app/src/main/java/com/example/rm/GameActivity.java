package com.example.rm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rm.utils.BlueToothUtil;
import com.example.rm.utils.TimeTool;
import com.example.rm.views.RockerView;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class GameActivity extends AppCompatActivity {

    private TextView mLogLeft;
    private TextView mLogRight;
    private TextView mDistance;

    public rmData data = new rmData();

    public float distance_left;
    public float angle_left;

    public float distance_right;
    public float angle_right;

    long leftTimeStamp;
    long rightTimeStamp;

    private Handler mHandler;

    public final Handler handler = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            // 处理消息
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(GameActivity.this, "蓝牙断开", Toast.LENGTH_SHORT).show();
                    GameActivity.this.finish();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        mLogLeft = (TextView) findViewById(R.id.log_left);
        mLogRight = (TextView) findViewById(R.id.log_right);
        mDistance = (TextView) findViewById(R.id.distance);

        RockerView rockerViewLeft = (RockerView) findViewById(R.id.rockerView_left);
        if (rockerViewLeft != null) {
            rockerViewLeft.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
                @Override
                public void onStart() {
                    mLogLeft.setText(null);
                }

                @Override
                public void angle(double angle) {
                    GameActivity.this.leftTimeStamp = TimeTool.getTimestamp();
                    GameActivity.this.angle_left = (float) angle;
                }

                @Override
                public void distance(double distance) {
                    GameActivity.this.leftTimeStamp = TimeTool.getTimestamp();
                    GameActivity.this.distance_left = (float) distance;
                }

                @Override
                public void onFinish() {

                }
            });
        }

        RockerView rockerViewRight = (RockerView) findViewById(R.id.rockerView_right);
        if (rockerViewRight != null) {
            rockerViewRight.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
                @Override
                public void onStart() {
                    mLogRight.setText(null);
                    mDistance.setText(null);
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void angle(double angle) {
                    GameActivity.this.rightTimeStamp = TimeTool.getTimestamp();
//                    mLogRight.setText("摇动角度 : " + String.valueOf(angle));
                    GameActivity.this.angle_right = (float) angle;
                }

                @SuppressLint("SetTextI18n")
                @Override
                public void distance(double distance) {
                    GameActivity.this.rightTimeStamp = TimeTool.getTimestamp();
                    //mDistance.setText("distance : " + String.valueOf(distance));
                    GameActivity.this.distance_right = (float) distance;
                }

                @Override
                public void onFinish() {
                    mLogRight.setText(null);
                    mDistance.setText(null);
                }
            });
        }

        mHandler = new Handler();

        Runnable r = new Runnable() {
            final static String TAG = "Timer task";

            boolean lastState = false;
            boolean curState = false;

            @Override
            public void run() {
                lastState = curState;
                curState = BlueToothUtil.isConnectStatus();

                boolean isContinue = true;

                if (lastState && !curState) {
                    Message message = GameActivity.this.handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                    isContinue = false;
                }

                if (TimeTool.getTimestamp() - GameActivity.this.leftTimeStamp > 500) {
                    GameActivity.this.angle_left = 0;
                    GameActivity.this.distance_left = 0;
                }

                if (TimeTool.getTimestamp() - GameActivity.this.rightTimeStamp > 500) {
                    GameActivity.this.angle_right = 0;
                    GameActivity.this.distance_right = 0;
                }


                GameActivity.this.data.updateChannels(GameActivity.this.angle_left, GameActivity.this.distance_left, GameActivity.this.angle_right, GameActivity.this.distance_right);

                byte[] arr = GameActivity.this.data.getDataPack();

                if (BlueToothUtil.getWriteGattCharacteristic() != null) {
                    boolean b = BlueToothUtil.getWriteGattCharacteristic().setValue(arr);
                    BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
                    if (!b) {
                        System.out.println("send data failed");
                    }
                }

//                try {
//                    outputStream.write(arr);
//                } catch (IOException e) {
////                    Toast.makeText(GameActivity.this, "发送数据失败", Toast.LENGTH_SHORT).show();
//
//                    e.printStackTrace();
//                }
                Log.i(TAG, "angle_left = " + GameActivity.this.angle_left + "distance_left = " + GameActivity.this.distance_left);
                Log.i(TAG, "angle_right = " + GameActivity.this.angle_right + "distance_right = " + GameActivity.this.distance_right);
                Log.i(TAG, "send " + arr.length + "byte");

                //每隔50ms循环执行run方法
                if (isContinue) {
                    mHandler.postDelayed(this, 50);
                }
            }
        };

        //主线程中调用：
        mHandler.postDelayed(r, 2000);//延时2s


//        addContentView(new VirtualKeyView(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlueToothUtil.disConnectGatt();

    }
}

class rmData {
    float channel1 = 0;
    float channel2 = 0;
    float channel3 = 0;
    float channel4 = 0;

    public rmData() {
    }

    public void updateChannels(float distance_left, float angle_left, float distance_right, float angle_right) {
        channel1 = (float) (Math.cos((double) (360 - angle_left)) * distance_left);
        channel2 = (float) (Math.sin((double) (360 - angle_left)) * distance_left);
        channel3 = (float) (Math.cos((double) (360 - angle_right)) * distance_right);
        channel4 = (float) (Math.sin((double) (360 - angle_right)) * distance_right);
    }

    private byte[] float2bytes(float f) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(f);
        return buffer.array();
    }

    public byte[] getDataPack() {
        ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.put((byte) 0x5A);
        buffer.put(float2bytes(channel1));
        buffer.put(float2bytes(channel2));
        buffer.put(float2bytes(channel3));
        buffer.put(float2bytes(channel4));

        buffer.put((byte) 0xA5);

        return buffer.array();
    }

}