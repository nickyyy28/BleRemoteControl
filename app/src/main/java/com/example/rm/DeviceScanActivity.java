package com.example.rm;

import android.app.Activity;
import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.os.Handler;
import android.util.Log;

import java.util.UUID;

public class DeviceScanActivity extends ListActivity {

    private BluetoothAdapter bluetoothAdapter;
    private boolean mScanning;
    private Handler handler;

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }

    public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
        this.mBluetoothGatt = mBluetoothGatt;
    }

    private BluetoothGatt mBluetoothGatt;

    private final String DeviceName = "FLY_DEVICE";
    private final String mUUID = UUID.randomUUID().toString();

    private boolean isScanByName = false;

    private Activity activity;

    public DeviceScanActivity(Activity activity) {
        this.activity = activity;
    }



    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private BluetoothAdapter.LeScanCallback mLeScanCallback = new  BluetoothAdapter.LeScanCallback() {


        private String TAG = "LeScanCallback";

        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "onscan");
            //  BluetoothDevice  name=eBody-Scale address=BC:6A:29:26:97:5E
            Log.d(TAG, "BluetoothDevice  name=" + device.getName() + " address=" + device.getAddress());
            //  BluetoothDevice  name=eBody-Scale address=BC:6A:29:26:97:5E
            //根据蓝牙名称或者mac地址找到对应的蓝牙设备
            if (isScanByName) {
                if (DeviceName.equals(device.getName())) {
                    Log.d(TAG, "find_device_by_name");
                    mBluetoothGatt = device.connectGatt(activity, false, null);
                    if (mScanning) {
                        scanLeDevice(false);
                    }

                }
            } else {
                if (mUUID.equals(device.getAddress())) {
                    Log.d(TAG, "find_device_by_mac");
                    mBluetoothGatt = device.connectGatt(activity, false, null);
                    if (mScanning) {
                        scanLeDevice(false);
                    }

                }
            }
        }
    };

    private void scanLeDevice(final boolean enable) {

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            handler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    mScanning = false;

                    bluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            bluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            bluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

}