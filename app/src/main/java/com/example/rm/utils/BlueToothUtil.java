package com.example.rm.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rm.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;

public class BlueToothUtil {

    private static final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private static final BlueToothBroadcastReceiver receiver = new BlueToothBroadcastReceiver();

    private static BluetoothGattService writeGattService = null;
    private static BluetoothGattCharacteristic writeGattCharacteristic = null;
    private static BluetoothGattCharacteristic notifyGattCharacteristic = null;

    public static BluetoothGattCharacteristic getNotifyGattCharacteristic() {
        return notifyGattCharacteristic;
    }

    public static void setNotifyGattCharacteristic(BluetoothGattCharacteristic notifyGattCharacteristic) {
        BlueToothUtil.notifyGattCharacteristic = notifyGattCharacteristic;
    }

    public static BluetoothGattCharacteristic getConfigGattCharacteristic() {
        return configGattCharacteristic;
    }

    public static void setConfigGattCharacteristic(BluetoothGattCharacteristic configGattCharacteristic) {
        BlueToothUtil.configGattCharacteristic = configGattCharacteristic;
    }

    private static BluetoothGattCharacteristic configGattCharacteristic = null;

    public static BluetoothGattService getWriteGattService() {
        return writeGattService;
    }

    public static void setWriteGattService(BluetoothGattService writeGattService) {
        BlueToothUtil.writeGattService = writeGattService;
    }

    public static BluetoothGattCharacteristic getWriteGattCharacteristic() {
        return writeGattCharacteristic;
    }

    public static void setWriteGattCharacteristic(BluetoothGattCharacteristic writeGattCharacteristic) {
        BlueToothUtil.writeGattCharacteristic = writeGattCharacteristic;
    }

    public static boolean isConnectStatus() {
        return connectStatus;
    }

    public static void setConnectStatus(boolean connectStatus) {
        BlueToothUtil.connectStatus = connectStatus;
    }

    private static boolean connectStatus = false;

    public static BluetoothManager getBluetoothManager() {
        return bluetoothManager;
    }

    public static void setBluetoothManager(BluetoothManager bluetoothManager) {
        BlueToothUtil.bluetoothManager = bluetoothManager;
    }

    public static void disConnectGatt() {
        if (bluetoothGatt != null){
            bluetoothGatt.disconnect();
        }
    }

    public static void clearDevices() {
        receiver.clearDevices();
    }

    private static BluetoothManager bluetoothManager;

    private static TimerTask timerTask;
    private final static int REQUEST_ENABLE_BT = 1;

    private static BluetoothSocket serverSocket;
    private static BluetoothSocket clientSocket;

    public static BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public static void setBluetoothGatt(BluetoothGatt bluetoothGatt) {
        BlueToothUtil.bluetoothGatt = bluetoothGatt;
    }

    private static BluetoothGatt bluetoothGatt;

    public static void setServerSocket(BluetoothSocket socket) {
        serverSocket = socket;
    }

    public static void setClientSocket(BluetoothSocket socket) {
        clientSocket = socket;
    }

    private enum socketMode {
        SERVER,
        CLIENT
    }

    private static socketMode mode = socketMode.CLIENT;

    private static long startStamp;

    public static long getStartStamp() {
        return startStamp;
    }

    public static void setStartStamp(long startStamp) {
        BlueToothUtil.startStamp = startStamp;
        BlueToothUtil.endStamp = startStamp;
    }

    public static long getEndStamp() {
        return endStamp;
    }

    public static void setEndStamp(long endStamp) {
        BlueToothUtil.endStamp = endStamp;
    }

    private static long endStamp;

    public static void setMode(int code) {
        if (code > 0) {
            mode = socketMode.SERVER;
        } else {
            mode = socketMode.CLIENT;
        }
    }

    public static InputStream getInputStream() throws IOException {
        if (mode == socketMode.SERVER) {
            return serverSocket.getInputStream();
        } else {
            return clientSocket.getInputStream();
        }
    }

    public static OutputStream getOutputStream() throws IOException {
        if (mode == socketMode.SERVER) {
            return serverSocket.getOutputStream();
        } else {
            boolean a = clientSocket.isConnected();
            return clientSocket.getOutputStream();
        }
    }


    public static void startAccept(Activity activity) {
        AcceptThread thread = new AcceptThread(adapter, serverSocket, activity);

        thread.start();
    }

    public static void clientConnect(BluetoothDevice device, Activity activity) throws IOException {
        ConnectThread thread = new ConnectThread(adapter, clientSocket, device, activity);

        thread.start();
    }


    public static boolean openBlueTooth(Activity activity) {
        //open blue tooth
        if (!adapter.isEnabled()) {
            Intent blue_tooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(blue_tooth, REQUEST_ENABLE_BT);
        }

        return getBlueToothState();
    }

    public static boolean closeBlueTooth() {
        if (!getBlueToothState()) {
            return true;
        }
        return adapter.disable();
    }

    public static void setBlueToothDiscoverable(Activity activity) {
        //make my device discoverable
        Intent discoverable = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverable.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 5 * 60);
        activity.startActivity(discoverable);

    }

    public static BluetoothDevice getDeviceByName(String name) {
        return receiver.getDevice(name);
    }

    public static boolean getBlueToothState() {
        //get bluetooth state
        return adapter.isEnabled();

    }

    public static void cancelBlueScan() {
        adapter.cancelDiscovery();
    }

    public static void setAdapter(ArrayAdapter<String> adapter) {
        receiver.setAdapter(adapter);
    }

    public static void registerBluetoothReceiver(Context context) {

        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF");
//        intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON");
        context.registerReceiver(receiver, intentFilter);
    }

    public static Set<BluetoothDevice> getBondedDevices() {
        return adapter.getBondedDevices();
    }

    public static void startDiscovery(Activity activity) {
        if (!adapter.isDiscovering()) {
            if (!adapter.startDiscovery()) {
                Toast.makeText(activity, "SCAN FAILED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void startDiscoverServices(){
        if (!isDiscoverServices()){
            bluetoothGatt.discoverServices();
        }
    }

    public static boolean isDiscoverServices(){
        return writeGattCharacteristic != null && configGattCharacteristic != null && notifyGattCharacteristic != null;
    }

    public static void unregisterBoardCaster(Activity activity) {
        activity.unregisterReceiver(receiver);
    }

    public static BluetoothAdapter getAdapter() {
        return adapter;
    }

    public static void UtilInit(Activity activity) {
        if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(activity, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            activity.finish();
        }
        bluetoothManager = (BluetoothManager) activity.getSystemService(Context.BLUETOOTH_SERVICE);
    }

    public static void bluetoothInit(Activity activity) {
        if (null == BluetoothAdapter.getDefaultAdapter()) {
            Toast.makeText(activity, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }

        openBlueTooth(activity);

        setBlueToothDiscoverable(activity);

        startDiscovery(activity);

        registerBluetoothReceiver(activity);

    }

    public static boolean enableNotification(boolean isEnable){
        if (writeGattCharacteristic == null || notifyGattCharacteristic == null || configGattCharacteristic == null){
            return false;
        }

        if (!bluetoothGatt.setCharacteristicNotification(notifyGattCharacteristic, true)){
            return false;
        }

//        BluetoothGattDescriptor clientConfig = notifyGattCharacteristic.getDescriptor(notifyGattCharacteristic.getUuid());
//
//        if (clientConfig == null){
//            return false;
//        }
//
//        if (isEnable){
//            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//        } else {
//            clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
//        }
//
//        return bluetoothGatt.writeDescriptor(clientConfig);
        return true;
    }
}

class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;
    private BluetoothSocket socket;
    private BluetoothAdapter adapter;
    private final Activity activity;

    private final static UUID myUUID = UUID.fromString("cb3fc1d1-5cd0-48ed-a757-aa77cec9a437");

    public BluetoothSocket getSocket() {
        return socket;
    }

    public void setSocket(BluetoothSocket socket) {
        this.socket = socket;
    }

    public BluetoothAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BluetoothAdapter adapter) {
        this.adapter = adapter;
    }

    private final String TAG = "AcceptThread";

    public AcceptThread(BluetoothAdapter adapter, BluetoothSocket socket, Activity activity) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.

        this.adapter = adapter;
        this.socket = socket;
        this.activity = activity;

        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = this.adapter.listenUsingRfcommWithServiceRecord("testHost", myUUID);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = mmServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
//                manageMyConnectedSocket(socket);

                BlueToothUtil.setMode(1);
                BlueToothUtil.setServerSocket(socket);
                Intent intent = new Intent("com.example.rm.GAME_ACTION");
                activity.startActivity(intent);
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}

class ConnectThread extends Thread {
    private BluetoothSocket mmSocket;
    private final Activity activity;
    private final BluetoothAdapter adapter;
    BluetoothGatt bluetoothGatt;

    private final static String TAG = "ConnectThread";

//    private final static UUID NotifyUUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");
//    private final static UUID WriteUUID = UUID.fromString("0000ff02-0000-1000-8000-00805f9b34fb");
    private final static UUID WriteUUID = UUID.fromString("d973f2e2-b19e-11e2-9e96-0800200c9a66");
    private final static UUID NotifyUUID = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    private final static UUID ServiceUUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    private final static UUID BT_24_WriteUUID = UUID.fromString("0000ffe2-0000-1000-8000-00805f9b34fb");

    public enum deviceType{
        DX2002,
        ATK_Blue1,
        BT_24
    }

    deviceType type = ConnectThread.deviceType.DX2002;

    public ConnectThread(BluetoothAdapter adapter, BluetoothSocket socket, BluetoothDevice device, Activity activity) {
        this.activity = activity;
        this.adapter = adapter;
        this.mmSocket = socket;

        type = deviceType.BT_24;

        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.

        /*
        if (device.getUuids() == null) {
            return;
        }

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
//            Log.e(TAG, "msg: uuid: " + device.getUuids()[0].toString());
            socket = device.createRfcommSocketToServiceRecord(device.getUuids()[1].getUuid());
//            tmp = device.createRfcommSocketToServiceRecord(UUID.randomUUID());
        } catch (IOException e) {
            Log.e(TAG, "Socket's create() method failed", e);
        }
        mmSocket = socket;
        */

        BlueToothUtil.setStartStamp(TimeTool.getTimestamp());



        bluetoothGatt = device.connectGatt(activity, true, new BluetoothGattCallback() {

            @Override
            public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyUpdate(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                super.onPhyRead(gatt, txPhy, rxPhy, status);
            }

            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                super.onConnectionStateChange(gatt, status, newState);
                Log.e("BluetoothGatt中中中", "连接状态:" + newState);

                /**
                 * 连接状态：
                 *    * The profile is in disconnected state   *public static final int STATE_DISCONNECTED  = 0;
                 *    * The profile is in connecting state     *public static final int STATE_CONNECTING    = 1;
                 *    * The profile is in connected state      *public static final int STATE_CONNECTED    = 2;
                 *    * The profile is in disconnecting state  *public static final int STATE_DISCONNECTING = 3;
                 *
                 */

                if (BluetoothGatt.STATE_CONNECTED == newState) {
                    Log.e("Bluetooth", "连接成功:");
                    System.out.println("bluetooth connect");
                    BlueToothUtil.setConnectStatus(true);
                    gatt.discoverServices();//必须有，可以让onServicesDiscovered显示所有Services
                    //tx_display.append("连接成功");
//                    Toast.makeText(mContext, "连接成功", Toast.LENGTH_SHORT).show();
                } else if (BluetoothGatt.STATE_DISCONNECTED == newState) {
                    Log.e("Bluetooth", "断开连接:");
                    System.out.println("bluetooth disconnect");
                    BlueToothUtil.setConnectStatus(false);
//                    bluetoothGatt.connect();
//                    Toast.makeText(mContext, "断开连接", Toast.LENGTH_SHORT).show();
                } else if (BluetoothGatt.STATE_CONNECTING == newState) {
                    System.out.println("bluetooth connecting");
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                super.onServicesDiscovered(gatt, status);
                List<BluetoothGattService> list = bluetoothGatt.getServices();
                BlueToothUtil.setEndStamp(TimeTool.getTimestamp());

                if (type == deviceType.ATK_Blue1){
                    for (BluetoothGattService bluetoothGattService:list){
                        String str = bluetoothGattService.getUuid().toString();
//                    Log.e("onServicesDisc中中中", " ：" + str);
                        List<BluetoothGattCharacteristic> gattCharacteristics = bluetoothGattService
                                .getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            System.out.println(gattCharacteristic.getUuid().toString());
                            if(WriteUUID.toString().equals(gattCharacteristic.getUuid().toString())){
//                            linkLossService=bluetoothGattService;
                                System.out.println("get right service");
                                BlueToothUtil.setWriteGattService(bluetoothGattService);
//                            alertLevel=gattCharacteristic;
                                BlueToothUtil.setWriteGattCharacteristic(gattCharacteristic);

                                Intent intent = new Intent("com.example.rm.GAME_ACTION");
                                activity.startActivity(intent);
//                            Log.e("daole",alertLevel.getUuid().toString());
                            }
                        }
                    }
                } else if (type == deviceType.DX2002){
                    for (BluetoothGattService gattService : list)
                    {
                        Log.i(TAG, gattService.getUuid().toString());
                        Log.i(TAG, ServiceUUID.toString());
                        if(gattService.getUuid().toString().equalsIgnoreCase(ServiceUUID.toString()))
                        {
                            List<BluetoothGattCharacteristic> gattCharacteristics =
                                    gattService.getCharacteristics();
                            Log.i(TAG, "Count is:" + gattCharacteristics.size());
                            for (BluetoothGattCharacteristic gattCharacteristic :
                                    gattCharacteristics)
                            {
                                if(gattCharacteristic.getUuid().toString().equalsIgnoreCase(NotifyUUID.toString()))
                                {
                                    Log.i(TAG, gattCharacteristic.getUuid().toString());
                                    Log.i(TAG, NotifyUUID.toString());
//                                    mNotifyCharacteristic = gattCharacteristic;
//                                    setCharacteristicNotification(gattCharacteristic, true);
//                                    broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

                                    BlueToothUtil.setNotifyGattCharacteristic(gattCharacteristic);
                                    bluetoothGatt.setCharacteristicNotification(gattCharacteristic, true);
                                    Intent intent = new Intent("com.example.rm.GAME_ACTION");
                                    activity.startActivity(intent);

                                }
                            }
                        }
                    }
                } else if (type == deviceType.BT_24){
                    for (BluetoothGattService bluetoothGattService:list){
                        String str = bluetoothGattService.getUuid().toString();
//                    Log.e("onServicesDisc中中中", " ：" + str);
                        List<BluetoothGattCharacteristic> gattCharacteristics = bluetoothGattService
                                .getCharacteristics();
                        for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                            System.out.println(gattCharacteristic.getUuid().toString());
                            if(BT_24_WriteUUID.toString().equals(gattCharacteristic.getUuid().toString())){
//                            linkLossService=bluetoothGattService;
                                System.out.println("get right service");
                                BlueToothUtil.setWriteGattService(bluetoothGattService);
//                            alertLevel=gattCharacteristic;
                                BlueToothUtil.setWriteGattCharacteristic(gattCharacteristic);

                                Intent intent = new Intent("com.example.rm.GAME_ACTION");
                                activity.startActivity(intent);
//                            Log.e("daole",alertLevel.getUuid().toString());
                            }
                        }
                    }
                }

//                enableNotification(true,gatt,alertLevel);//必须要有，否则接收不到数据

            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicRead(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                super.onCharacteristicWrite(gatt, characteristic, status);
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                super.onCharacteristicChanged(gatt, characteristic);
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorRead(gatt, descriptor, status);
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                super.onDescriptorWrite(gatt, descriptor, status);
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                super.onReliableWriteCompleted(gatt, status);
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                super.onReadRemoteRssi(gatt, rssi, status);
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                super.onMtuChanged(gatt, mtu, status);
            }

            @Override
            public void onServiceChanged(@NonNull BluetoothGatt gatt) {
                super.onServiceChanged(gatt);
            }
        });

        BlueToothUtil.setBluetoothGatt(bluetoothGatt);
        BlueToothUtil.cancelBlueScan();

    }

    public void run() {
        adapter.cancelDiscovery();

        bluetoothGatt.connect();

    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}