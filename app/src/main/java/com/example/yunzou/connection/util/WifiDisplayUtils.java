package com.example.yunzou.connection.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.display.DisplayManager;
import android.widget.Toast;

import static org.joor.Reflect.*;


public class WifiDisplayUtils {
    Context context;
    private final DisplayManager mDisplayManager;

    public WifiDisplayUtils() {
        this.context = ContextUtil.getContext();
        mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
    }


    //    private String ACTION_WIFI_DISPLAY_STATUS_CHANGED = on(DisplayManager.class).field("ACTION_WIFI_DISPLAY_STATUS_CHANGED ").get();
    private String ACTION_WIFI_DISPLAY_STATUS_CHANGED = "android.hardware.display.action.WIFI_DISPLAY_STATUS_CHANGED";

    BroadcastReceiver mWifiDisplayStatusChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_WIFI_DISPLAY_STATUS_CHANGED)) {
//                (WifiDisplayStatus) intent.getParcelableExtra(DisplayManager.EXTRA_WIFI_DISPLAY_STATUS)
                /* WifiDisplayStatus */
                Object wifiDisplayStatus = intent.getParcelableExtra("android.hardware.display.extra.WIFI_DISPLAY_STATUS");
                checkAndConnectWifiDisplay(wifiDisplayStatus);
            }
        }
    };

    public void checkAndConnectWifiDisplay(/* WifiDisplayStatus */ Object status) {
        // if (wifiDisplayStatus.getFeatureState() == WifiDisplayStatus.FEATURE_STATE_ON) {
        if ((int) on(status).call("getFeatureState").get() == 3) {
            // WifiDisplay activeDisplay = status.getActiveDisplay();
            Object activeDisplay = on(status).call("getActiveDisplay").get();
            if (activeDisplay == null) {
                return;
            }

            int activeDisplayStatus = on(status).call("getActiveDisplayState").get();

            // if (activeDisplayStatus == WifiDisplayStatus.DISPLAY_STATE_CONNECTED){
            if (activeDisplayStatus == 2) {
                Toast.makeText(context, "wifi display 连接成功", Toast.LENGTH_SHORT).show();
                this.stopScan();
            }

            String name = on(activeDisplay).call("getDeviceName").get();
            String address = on(activeDisplay).call("getDeviceAddress").get();
            // if (activeDisplayStatus == WifiDisplayStatus.DISPLAY_STATE_NOT_CONNECTED){
            if (activeDisplayStatus == 0) {
                Toast.makeText(context, "wifi display 开始连接", Toast.LENGTH_SHORT).show();
                // String name = activeDisplay.getDeviceName();
                // String address = activeDisplay.getDeviceAddress();
                on(mDisplayManager).call("connectWifiDisplay", address);
            }
            // if (activeDisplayStatus == WifiDisplayStatus.DISPLAY_STATE_CONNECTING){
            if (activeDisplayStatus == 1) {
                Toast.makeText(context, "wifi display 连接中...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void startScanAndConnect() {
        context.registerReceiver(mWifiDisplayStatusChangedReceiver, new IntentFilter(ACTION_WIFI_DISPLAY_STATUS_CHANGED));
        on(mDisplayManager).call("startWifiDisplayScan");
        Toast.makeText(context, "开始扫描 wifi display", Toast.LENGTH_SHORT).show();
    }

    public void stopScan() {
        context.unregisterReceiver(mWifiDisplayStatusChangedReceiver);
        on(mDisplayManager).call("stopWifiDisplayScan");
        Toast.makeText(context, "停止扫描 wifi display", Toast.LENGTH_SHORT).show();
    }

    public static boolean isConnectedWifiDisplay() {
        Context context = ContextUtil.getContext();
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);

        Object wifiDisplayStatus = on(displayManager).call("getWifiDisplayStatus").get();

        Object activeDisplay = on(wifiDisplayStatus).call("getActiveDisplay").get();
        if (activeDisplay == null) {
            return false;
        }

        return true;
    }


}
