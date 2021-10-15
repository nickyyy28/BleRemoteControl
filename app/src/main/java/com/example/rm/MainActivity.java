package com.example.rm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rm.utils.BlueToothUtil;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MY_PERMISSION_REQUEST_CONSTANT = 101;

    private boolean isRunning = false;
    private boolean dialogIsShow = false;

    private Handler checkConnect;
    private Runnable runnable;

    ArrayAdapter<String> adapter;
//    private final String[] data = {"Apple", "Banana", "Cheery", "Orange", "WaterMelon", "Lemon", "Pineapple"};

    private void checkBTPermission() {
//        Toast.makeText(this, "checkBTPermission: Start", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
                permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
                if (permissionCheck != 0) {
                    this.requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 1001); //any number
                } else {
                    Toast.makeText(this, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.", Toast.LENGTH_SHORT).show();
                }
            }

        }
//        Toast.makeText(this, "checkBTPermission: Finish", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isRunning = true;

        BlueToothUtil.UtilInit(this);

        BlueToothUtil.openBlueTooth(this);

        BlueToothUtil.setBlueToothDiscoverable(this);

        Button btn_1 = (Button) findViewById(R.id.scan_device);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        Toast.makeText(this, "SDK Version " + Build.VERSION.SDK_INT, Toast.LENGTH_SHORT).show();

//        Button btn_pid = (Button) findViewById(R.id.btn_setPid);
//        btn_pid.setOnClickListener(view -> {
//            Intent intent = new Intent("android.intent.action.PID_ACTION");
//            startActivity(intent);
//        });
//
//        Button btn_dialog = (Button) findViewById(R.id.btn_dialog);
//        btn_dialog.setOnClickListener(view -> {
//            showPopupWindow(MainActivity.this.getApplicationContext(), findViewById(R.id.btn_dialog));
//        });

        btn_1.setOnClickListener(view -> {


//            Toast.makeText(this, "start scan device", Toast.LENGTH_SHORT).show();
//            BlueToothUtil.openBlueTooth(this);

            //scan device
//                if (!isDiscover) {
//                    BlueToothUtil.startDiscovery();
//                    isDiscover = true;
//                    TimerTask timerTask = new TimerTask() {
//                        @Override
//                        public void run() {
//                            isDiscover = false;
//                            Toast.makeText(MainActivity.this, "stop scan", Toast.LENGTH_SHORT).show();
//                        }
//                    };
//                    new Timer().schedule(timerTask, 12 * 1000);
//                }
            BlueToothUtil.clearDevices();

            checkBTPermission();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&

                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

                    checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&

                    checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,

                        Manifest.permission.CAMERA,}, 1);
            }

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    String[] strings =
                            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                    ActivityCompat.requestPermissions(this, strings, 1);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this,
                        "android.permission.ACCESS_BACKGROUND_LOCATION") != PackageManager.PERMISSION_GRANTED) {
                    String[] strings = {android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            "android.permission.ACCESS_BACKGROUND_LOCATION"};
                    ActivityCompat.requestPermissions(this, strings, 2);
                }
            }

            BlueToothUtil.bluetoothInit(this);

            adapter.clear();
            adapter.notifyDataSetChanged();

        });

        if (Build.VERSION.SDK_INT >= 6.0) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_CONSTANT);
        }

        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        BlueToothUtil.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapter.getItem(i);

                try {
                    BlueToothUtil.closeAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BlueToothUtil.clientConnect(BlueToothUtil.getDeviceByName(name.split("\n")[0]), MainActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

        Spinner s = (Spinner) findViewById(R.id.device_type);
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapterView.getSelectedItem().toString().equals(MainActivity.this.getString(R.string.device1))) {
                    BlueToothUtil.setType(BlueToothUtil.deviceType.ATK_Blue1);
                    System.out.println("set type ATK_BLUE01");
                } else if (adapterView.getSelectedItem().toString().equals(MainActivity.this.getString(R.string.device2))) {
                    System.out.println("set type DX2002");
                    BlueToothUtil.setType(BlueToothUtil.deviceType.DX2002);
                } else if (adapterView.getSelectedItem().toString().equals(MainActivity.this.getString(R.string.device3))) {
                    BlueToothUtil.setType(BlueToothUtil.deviceType.BT_24);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //BlueToothUtil.startAccept(this);

        Log.d(TAG, "onCreate: " + adapter);

        checkConnect = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                if (BlueToothUtil.getConnectStatus() && !dialogIsShow && isRunning){
                    showPopupWindow(getApplicationContext(), findViewById(R.id.scan_device));
                    dialogIsShow = true;
                    isRunning = false;
                }

                if (isRunning){
                    checkConnect.postDelayed(this, 100);
                }
            }
        };

//        checkConnect.postDelayed(runnable, 50);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            BlueToothUtil.closeAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        isRunning = false;

        BlueToothUtil.unregisterBoardCaster(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        isRunning = true;
        checkConnect.postDelayed(runnable, 2000);

    }

    private void showPopupWindow(Context context, View parent) {
        //LayoutInflater的作用是用来动态加载Layout文件的
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") final View popupView = inflater.inflate( R.layout.popup_window, null);//动态加载Layout文件
        final PopupWindow pWindow = new PopupWindow(popupView,600,350,true);//需要填写宽高，否则显示不了
        final Button button=(Button)popupView.findViewById(R.id.btn_goto_control);//加载之后可以找到其中的控件了
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isRunning = false;
                dialogIsShow = false;
                pWindow.dismiss();
                Intent intent = new Intent("com.example.rm.GAME_ACTION");
                startActivity(intent);
            }
        });
        //Cancel按钮及其处理事件
        Button btnCancel=(Button)popupView.findViewById(R.id.btn_goto_pid_configure);
        btnCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isRunning = false;
                dialogIsShow = false;
                pWindow.dismiss();//关闭
                Intent intent = new Intent("android.intent.action.PID_ACTION");
                startActivity(intent);
            }
        });
        //显示popupWindow对话框

        pWindow.setOutsideTouchable(false);

        if (parent == null){
            Toast.makeText(context, "the parent is null", Toast.LENGTH_SHORT).show();
        }

        pWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

}