package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rm.other.PIDControl;
import com.example.rm.utils.BlueToothUtil;

import java.io.IOException;

public class PIDConfigureActivity extends AppCompatActivity {

    private boolean isRunning = false;

    private PIDControl yaw_av_pid = new PIDControl((byte) 0x05);
    private PIDControl yaw_a_pid = new PIDControl((byte) 0x06);
    private PIDControl pitch_av_pid = new PIDControl((byte) 0x07);
    private PIDControl pitch_a_pid = new PIDControl((byte) 0x08);
    private PIDControl roll_av_pid = new PIDControl((byte) 0x09);
    private PIDControl roll_a_pid = new PIDControl((byte) 0x0A);

    private EditText ipt_yaw_av_Kp_pid;
    private EditText ipt_yaw_av_Ki_pid;
    private EditText ipt_yaw_av_Kd_pid;
    private EditText ipt_yaw_av_iLimit_pid;
    private EditText ipt_yaw_av_iRange_pid;

    private EditText ipt_yaw_a_Kp_pid;
    private EditText ipt_yaw_a_Ki_pid;
    private EditText ipt_yaw_a_Kd_pid;
    private EditText ipt_yaw_a_iLimit_pid;
    private EditText ipt_yaw_a_iRange_pid;

    private EditText ipt_pitch_av_Kp_pid;
    private EditText ipt_pitch_av_Ki_pid;
    private EditText ipt_pitch_av_Kd_pid;
    private EditText ipt_pitch_av_iLimit_pid;
    private EditText ipt_pitch_av_iRange_pid;

    private EditText ipt_pitch_a_Kp_pid;
    private EditText ipt_pitch_a_Ki_pid;
    private EditText ipt_pitch_a_Kd_pid;
    private EditText ipt_pitch_a_iLimit_pid;
    private EditText ipt_pitch_a_iRange_pid;

    private EditText ipt_roll_av_Kp_pid;
    private EditText ipt_roll_av_Ki_pid;
    private EditText ipt_roll_av_Kd_pid;
    private EditText ipt_roll_av_iLimit_pid;
    private EditText ipt_roll_av_iRange_pid;

    private EditText ipt_roll_a_Kp_pid;
    private EditText ipt_roll_a_Ki_pid;
    private EditText ipt_roll_a_Kd_pid;
    private EditText ipt_roll_a_iLimit_pid;
    private EditText ipt_roll_a_iRange_pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRunning = true;
        setContentView(R.layout.activity_pidconfigure);

        argumentInit();

        //yaw轴角速度环PID
        Button btn_yaw_av_pid = (Button) findViewById(R.id.btn_yaw_av_pid);
        btn_yaw_av_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                short yaw_av_Kp = getEditShort(ipt_yaw_av_Kp_pid);
                short yaw_av_Ki = getEditShort(ipt_yaw_av_Ki_pid);
                short yaw_av_Kd = getEditShort(ipt_yaw_av_Kd_pid);
                short yaw_av_iLimit = getEditShort(ipt_yaw_av_iLimit_pid);
                short yaw_av_iRange = getEditShort(ipt_yaw_av_iRange_pid);

                yaw_av_pid.setKp(yaw_av_Kp);
                yaw_av_pid.setKi(yaw_av_Ki);
                yaw_av_pid.setKd(yaw_av_Kd);
                yaw_av_pid.setiLimit(yaw_av_iLimit);
                yaw_av_pid.setiRange(yaw_av_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(yaw_av_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

        //yaw轴角度环PID
        Button btn_yaw_a_pid = (Button) findViewById(R.id.btn_yaw_a_pid);
        btn_yaw_a_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short yaw_a_Kp = getEditShort(ipt_yaw_a_Kp_pid);
                short yaw_a_Ki = getEditShort(ipt_yaw_a_Ki_pid);
                short yaw_a_Kd = getEditShort(ipt_yaw_a_Kd_pid);
                short yaw_a_iLimit = getEditShort(ipt_yaw_a_iLimit_pid);
                short yaw_a_iRange = getEditShort(ipt_yaw_a_iRange_pid);

                yaw_a_pid.setKp(yaw_a_Kp);
                yaw_a_pid.setKi(yaw_a_Ki);
                yaw_a_pid.setKd(yaw_a_Kd);
                yaw_a_pid.setiLimit(yaw_a_iLimit);
                yaw_a_pid.setiRange(yaw_a_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(yaw_a_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

        //pitch轴角速度环PID
        Button btn_pitch_av_pid = (Button) findViewById(R.id.btn_pitch_av_pid);
        btn_pitch_av_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                short pitch_av_Kp = getEditShort(ipt_pitch_av_Kp_pid);
                short pitch_av_Ki = getEditShort(ipt_pitch_av_Ki_pid);
                short pitch_av_Kd = getEditShort(ipt_pitch_av_Kd_pid);
                short pitch_av_iLimit = getEditShort(ipt_pitch_av_iLimit_pid);
                short pitch_av_iRange = getEditShort(ipt_pitch_av_iRange_pid);

                pitch_av_pid.setKp(pitch_av_Kp);
                pitch_av_pid.setKi(pitch_av_Ki);
                pitch_av_pid.setKd(pitch_av_Kd);
                pitch_av_pid.setiLimit(pitch_av_iLimit);
                pitch_av_pid.setiRange(pitch_av_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(pitch_av_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

        //pitch轴角度环PID
        Button btn_pitch_a_pid = (Button) findViewById(R.id.btn_pitch_a_pid);
        btn_pitch_a_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short pitch_a_Kp = getEditShort(ipt_pitch_a_Kp_pid);
                short pitch_a_Ki = getEditShort(ipt_pitch_a_Ki_pid);
                short pitch_a_Kd = getEditShort(ipt_pitch_a_Kd_pid);
                short pitch_a_iLimit = getEditShort(ipt_pitch_a_iLimit_pid);
                short pitch_a_iRange = getEditShort(ipt_pitch_a_iRange_pid);

                pitch_a_pid.setKp(pitch_a_Kp);
                pitch_a_pid.setKi(pitch_a_Ki);
                pitch_a_pid.setKd(pitch_a_Kd);
                pitch_a_pid.setiLimit(pitch_a_iLimit);
                pitch_a_pid.setiRange(pitch_a_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(pitch_a_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

        //roll轴角速度环PID
        Button btn_roll_av_pid = (Button) findViewById(R.id.btn_roll_av_pid);
        btn_roll_av_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                short roll_av_Kp = getEditShort(ipt_roll_av_Kp_pid);
                short roll_av_Ki = getEditShort(ipt_roll_av_Ki_pid);
                short roll_av_Kd = getEditShort(ipt_roll_av_Kd_pid);
                short roll_av_iLimit = getEditShort(ipt_roll_av_iLimit_pid);
                short roll_av_iRange = getEditShort(ipt_roll_av_iRange_pid);

                roll_av_pid.setKp(roll_av_Kp);
                roll_av_pid.setKi(roll_av_Ki);
                roll_av_pid.setKd(roll_av_Kd);
                roll_av_pid.setiLimit(roll_av_iLimit);
                roll_av_pid.setiRange(roll_av_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(roll_av_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

        //roll轴角度环PID
        Button btn_roll_a_pid = (Button) findViewById(R.id.btn_roll_a_pid);
        btn_roll_a_pid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                short roll_a_Kp = getEditShort(ipt_roll_a_Kp_pid);
                short roll_a_Ki = getEditShort(ipt_roll_a_Ki_pid);
                short roll_a_Kd = getEditShort(ipt_roll_a_Kd_pid);
                short roll_a_iLimit = getEditShort(ipt_roll_a_iLimit_pid);
                short roll_a_iRange = getEditShort(ipt_roll_a_iRange_pid);

                roll_a_pid.setKp(roll_a_Kp);
                roll_a_pid.setKi(roll_a_Ki);
                roll_a_pid.setKd(roll_a_Kd);
                roll_a_pid.setiLimit(roll_a_iLimit);
                roll_a_pid.setiRange(roll_a_iRange);

                BlueToothUtil.getWriteGattCharacteristic().setValue(roll_a_pid.getPack());
                BlueToothUtil.getBluetoothGatt().writeCharacteristic(BlueToothUtil.getWriteGattCharacteristic());
            }
        });

    }

    private void argumentInit(){
        ipt_yaw_av_Kp_pid = findViewById(R.id.ipt_yaw_av_Kp);
        ipt_yaw_av_Ki_pid = findViewById(R.id.ipt_yaw_av_Ki);
        ipt_yaw_av_Kd_pid = findViewById(R.id.ipt_yaw_av_Kd);
        ipt_yaw_av_iLimit_pid = findViewById(R.id.ipt_yaw_av_iLimit);
        ipt_yaw_av_iRange_pid = findViewById(R.id.ipt_yaw_av_iRange);

        ipt_yaw_a_Kp_pid = findViewById(R.id.ipt_yaw_a_Kp);
        ipt_yaw_a_Ki_pid = findViewById(R.id.ipt_yaw_a_Ki);
        ipt_yaw_a_Kd_pid = findViewById(R.id.ipt_yaw_a_Kd);
        ipt_yaw_a_iLimit_pid = findViewById(R.id.ipt_yaw_a_iLimit);
        ipt_yaw_a_iRange_pid = findViewById(R.id.ipt_yaw_a_iRange);

        ipt_pitch_av_Kp_pid = findViewById(R.id.ipt_pitch_av_Kp);
        ipt_pitch_av_Ki_pid = findViewById(R.id.ipt_pitch_av_Ki);
        ipt_pitch_av_Kd_pid = findViewById(R.id.ipt_pitch_av_Kd);
        ipt_pitch_av_iLimit_pid = findViewById(R.id.ipt_pitch_av_iLimit);
        ipt_pitch_av_iRange_pid = findViewById(R.id.ipt_pitch_av_iRange);

        ipt_pitch_a_Kp_pid = findViewById(R.id.ipt_pitch_a_Kp);
        ipt_pitch_a_Ki_pid = findViewById(R.id.ipt_pitch_a_Ki);
        ipt_pitch_a_Kd_pid = findViewById(R.id.ipt_pitch_a_Kd);
        ipt_pitch_a_iLimit_pid = findViewById(R.id.ipt_pitch_a_iLimit);
        ipt_pitch_a_iRange_pid = findViewById(R.id.ipt_pitch_a_iRange);

        ipt_roll_av_Kp_pid = findViewById(R.id.ipt_roll_av_Kp);
        ipt_roll_av_Ki_pid = findViewById(R.id.ipt_roll_av_Ki);
        ipt_roll_av_Kd_pid = findViewById(R.id.ipt_roll_av_Kd);
        ipt_roll_av_iLimit_pid = findViewById(R.id.ipt_roll_av_iLimit);
        ipt_roll_av_iRange_pid = findViewById(R.id.ipt_roll_av_iRange);

        ipt_roll_a_Kp_pid = findViewById(R.id.ipt_roll_a_Kp);
        ipt_roll_a_Ki_pid = findViewById(R.id.ipt_roll_a_Ki);
        ipt_roll_a_Kd_pid = findViewById(R.id.ipt_roll_a_Kd);
        ipt_roll_a_iLimit_pid = findViewById(R.id.ipt_roll_a_iLimit);
        ipt_roll_a_iRange_pid = findViewById(R.id.ipt_roll_a_iRange);
    }

    float getEditFloat(EditText editText){
        String text = editText.getText().toString();

        if (text.equals("")){
            return 0;
        }

        return Float.parseFloat(text);

    }

    short getEditShort(EditText editText){
        String text = editText.getText().toString();

        if (text.equals("")){
            return 0;
        }

        return Short.parseShort(text);
    }

    @Override
    protected void onDestroy() {
        isRunning = false;

        super.onDestroy();

        try {
            BlueToothUtil.closeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}