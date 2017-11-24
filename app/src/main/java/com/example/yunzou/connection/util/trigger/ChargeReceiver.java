package com.example.yunzou.connection.util.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yunzou.connection.AppSetting;
import com.example.yunzou.connection.util.ProcessController;

public class ChargeReceiver extends BroadcastReceiver {
    private static String TAG = "zy.ChargeReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_POWER_CONNECTED.equals(action)) {
            Log.d(TAG, "ACTION_POWER_CONNECTED");
            if (AppSetting.isConnectTriggerBluetooth()) {
                // 触发开始流程
                ProcessController.getInstance().processConnect();
            }
        } else if (Intent.ACTION_POWER_DISCONNECTED.equals(action)) {
            Log.d(TAG, "ACTION_POWER_DISCONNECTED");
        }
    }
}
