package com.example.yunzou.connection.util.trigger;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yunzou.connection.AppSetting;
import com.example.yunzou.connection.util.ProcessController;
import com.example.yunzou.connection.util.USBUtils;

public class BluetoothReceiver extends BroadcastReceiver {
    private static String TAG = "zy.BluetoothReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "BluetoothReceiver.onReceive");
        BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (!AppSetting.isTriggerBluetoothDevice(bluetoothDevice)) {
            return;
        }

        int bluetoothConnectionState = intent.getIntExtra(BluetoothAdapter.EXTRA_CONNECTION_STATE, BluetoothAdapter.STATE_DISCONNECTED);

        if (bluetoothConnectionState == BluetoothAdapter.STATE_CONNECTING) {
            return;
        }

        if (bluetoothConnectionState == BluetoothAdapter.STATE_CONNECTED) {
            Log.d(TAG, "BluetoothReceiver. trigger bluetooth devices >> STATE_CONNECTED");
            if (USBUtils.isCharging()) {
                // 触发开始流程
                ProcessController.getInstance().processConnect();
            }

        } else {
//        if (bluetoothConnectionState == BluetoothAdapter.STATE_DISCONNECTED) {
            Log.d(TAG, "BluetoothReceiver. trigger bluetooth devices >> STATE_DISCONNECTED");
            // 恢复系统设置
            ProcessController.getInstance().processDisconnect();
        }
    }
}
