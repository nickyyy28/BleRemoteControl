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

import com.example.rm.other.RockerData;
import com.example.rm.utils.BlueToothUtil;
import com.example.rm.utils.TimeTool;
import com.example.rm.views.RockerView;

import com.example.rm.other.rmData;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {

    public rmData rmdata = new rmData();
    public RockerData data = new RockerData(rmdata);

    public float distance_left;
    public float angle_left;

    public float distance_right;
    public float angle_right;

    private RockerView rockerViewLeft;
    private RockerView rockerViewRight;

    TextView textView;

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
                    Toast.makeText(GameActivity.this, "蓝牙已断开", Toast.LENGTH_SHORT).show();
                    GameActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    private final Handler updateChannel = new Handler() {
        @SuppressLint("HandlerLeak")
        public void handleMessage(Message msg) {
            // 处理消息
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    DecimalFormat df = new DecimalFormat("000.000");
                    String buffer = "油门: " + (rmdata.getChannel2() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel2())) + "\n偏航: " + (rmdata.getChannel1() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel1())) + "\n" +
                            "翻滚: " + (rmdata.getChannel3() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel3())) + "\n俯仰: " + (rmdata.getChannel4() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel4()));
                    textView.setText(buffer);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ActionBar actionBar = getSupportActionBar();

        textView = (TextView) findViewById(R.id.msgs);

        if (actionBar != null) {
            actionBar.hide();
        }

        rockerViewLeft = (RockerView) findViewById(R.id.rockerView_left);
        if (rockerViewLeft != null) {
            rockerViewLeft.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
                @Override
                public void onStart() {

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

        assert rockerViewLeft != null;
        rockerViewLeft.setLeft(true);

        rockerViewRight = (RockerView) findViewById(R.id.rockerView_right);
        if (rockerViewRight != null) {
            rockerViewRight.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
                @Override
                public void onStart() {

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

                System.out.println("err = " + (BlueToothUtil.getEndStamp() - BlueToothUtil.getStartStamp()));

//                if (TimeTool.getTimestamp() - GameActivity.this.leftTimeStamp > 500) {
//                    GameActivity.this.angle_left = 0;
//                    GameActivity.this.distance_left = 0;
//                }
//
//                if (TimeTool.getTimestamp() - GameActivity.this.rightTimeStamp > 500) {
//                    GameActivity.this.angle_right = 0;
//                    GameActivity.this.distance_right = 0;
//                }

//                if (GameActivity.this.rockerViewLeft.getState() == RockerView.TOUCH_STATE.UNTOUCHED){
//                    GameActivity.this.angle_left = 0;
//                    GameActivity.this.distance_left = 0;
//                }

                if (GameActivity.this.rockerViewRight.getState() == RockerView.TOUCH_STATE.UNTOUCHED){
                    GameActivity.this.angle_right = 0;
                    GameActivity.this.distance_right = 0;
                }

                GameActivity.this.rmdata.updateChannels(GameActivity.this.distance_left, GameActivity.this.angle_left, GameActivity.this.distance_right, GameActivity.this.angle_right);

                byte[] arr = GameActivity.this.data.getPack();
                System.out.println("arr" + Arrays.toString(arr));

                if (BlueToothUtil.getWriteGattCharacteristic() != null) {
                    boolean b = BlueToothUtil.getWriteGattCharacteristic().setValue(arr);
                    BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());

                    System.out.println("send data success!");

                    Message message = GameActivity.this.updateChannel.obtainMessage();
                    message.what = 1;
                    GameActivity.this.updateChannel.sendMessage(message);

                    if (!b) {
                        System.out.println("send data failed");
                    }
                } else {
                    System.out.println("send data failed");
                }

//                Log.i(TAG, "angle_left = " + GameActivity.this.angle_left + "distance_left = " + GameActivity.this.distance_left);
//                Log.i(TAG, "angle_right = " + GameActivity.this.angle_right + "distance_right = " + GameActivity.this.distance_right);
                Log.i(TAG, "send " + arr.length + "byte");

                //每隔50ms循环执行run方法
                if (isContinue) {
                    mHandler.postDelayed(this, 20);
                }

            }
        };

        //主线程中调用：
        mHandler.postDelayed(r, 3000);//延时2s

        DecimalFormat df = new DecimalFormat("000.000");
        String buffer = "油门: " + (rmdata.getChannel2() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel2())) + "\n偏航: " + (rmdata.getChannel1() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel1())) + "\n" +
                "翻滚: " + (rmdata.getChannel3() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel3())) + "\n俯仰: " + (rmdata.getChannel4() >= 0 ? "+" : "-") + df.format(Math.abs(rmdata.getChannel4()));
        textView.setText(buffer);

        Handler checkHandler = new Handler();
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                boolean isDiscover = false;
                if (BlueToothUtil.isDiscoverServices()){
                    isDiscover = true;
                    if (!BlueToothUtil.enableNotification(true)){
                        Toast.makeText(GameActivity.this, "蓝牙已断开", Toast.LENGTH_SHORT).show();
                        GameActivity.this.finish();
                    }
                } else {
                    BlueToothUtil.startDiscoverServices();
                }

                if (!isDiscover){
                    checkHandler.postDelayed(this, 2000);
                }

            }
        };

//        checkHandler.postDelayed(runnable, 2000);



//        addContentView(new VirtualKeyView(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BlueToothUtil.disConnectGatt();

    }
}