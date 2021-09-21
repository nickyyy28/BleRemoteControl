package com.example.rm.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class BlueToothBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "BlueToothBroadcastReceiver";


    public HashMap<String, BluetoothDevice> getDevices() {
        return devices;
    }

    public final HashMap<String, BluetoothDevice> devices = new HashMap<>();

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    ArrayAdapter<String> adapter;

    BluetoothDevice getDevice(String name)
    {
        return devices.getOrDefault(name, null);
    }

    public BlueToothBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (device == null || device.getName() == null || device.getAddress()==null){
                return;
            }

            if (! devices.containsKey(device.getName())){
                Toast.makeText(context, "find device " + device.getName(), Toast.LENGTH_SHORT).show();
                devices.put(device.getName(), device);

                Log.d(TAG, "onReceive: " + adapter);

//                device.

                adapter.add(device.getName());
                adapter.notifyDataSetChanged();
            }
        }
    }

}
