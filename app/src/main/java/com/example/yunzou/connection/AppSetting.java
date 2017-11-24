package com.example.yunzou.connection;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.Surface;

import com.example.yunzou.connection.util.BluetoothUtils;

import java.util.List;

/**
 * Created by Simon on 17/11/14.
 */

public class AppSetting {
    private static String APP_DATA_FILE = "config";
    private static SharedPreferences PREFERENCES;

    static {
        PREFERENCES = ContextUtil.INSTANCE.getContext().getSharedPreferences(APP_DATA_FILE, Context.MODE_PRIVATE);
    }

    public static boolean isSystemSettingsSaved(){
        return PREFERENCES.getBoolean(SettingKey.SYSTEM_CONFIG_SAVE_STATUS, false);
    }

    public static void setSystemSettingsSaved(boolean isSaved){
        SharedPreferences.Editor editor = PREFERENCES.edit();
        editor.putBoolean(SettingKey.SYSTEM_CONFIG_SAVE_STATUS, isSaved);
        editor.commit();
    }

    public static void saveScreenConfigInfo(boolean isEnableRotation, int screenRotation){
        SharedPreferences.Editor editor = PREFERENCES.edit();
        editor.putBoolean(SettingKey.SYSTEM_CONFIG_SCREEN_AUTO_RATATION, isEnableRotation);
        editor.putInt(SettingKey.SYSTEM_CONFIG_SCREEN_RATATION, screenRotation);
        editor.apply();
    }

    public static boolean getOriginalScreenEnableRatation(){
        return PREFERENCES.getBoolean(SettingKey.SYSTEM_CONFIG_SCREEN_AUTO_RATATION, true);
    }

    public static int getOriginalScreenRotation(){
        return PREFERENCES.getInt(SettingKey.SYSTEM_CONFIG_SCREEN_RATATION, Surface.ROTATION_0);
    }

    public static void saveBluetoothInfo(String name, String address) {
        SharedPreferences.Editor editor = PREFERENCES.edit();
        editor.putString(SettingKey.BLUETOOTH_NAME, name);
        editor.putString(SettingKey.BLUETOOTH_ADDRESS, address);
        editor.apply();
    }

    public static String getTriggerBluetoothName() {
        return PREFERENCES.getString(SettingKey.BLUETOOTH_NAME, "");
    }

    public static String getTriggerBluetoothAddress() {
        return PREFERENCES.getString(SettingKey.BLUETOOTH_ADDRESS, "");
    }

    public static boolean isTriggerBluetoothDevice(BluetoothDevice device) {
        String triggerBluetoothAddress = getTriggerBluetoothAddress();
        return triggerBluetoothAddress.equals(device.getAddress());
    }

    public static boolean isConnectTriggerBluetooth() {
        String triggerBluetoothAddress = getTriggerBluetoothAddress();
        if (TextUtils.isEmpty(triggerBluetoothAddress)) {
            return false;
        }

        List<BluetoothDevice> connectedDevices = BluetoothUtils.getConnectedDevices();
        for (BluetoothDevice device : connectedDevices) {
            if (isTriggerBluetoothDevice(device)) {
                return true;
            }
        }

        return false;
    }

    private interface SettingKey {
        String BLUETOOTH_NAME = "bluetooth_name";
        String BLUETOOTH_ADDRESS = "bluetooth_address";

        String SYSTEM_CONFIG_SCREEN_AUTO_RATATION = "SYSTEM_CONFIG_SCREEN_AUTO_RATATION";
        String SYSTEM_CONFIG_SCREEN_RATATION = "SYSTEM_CONFIG_SCREEN_RATATION";
        String SYSTEM_CONFIG_SAVE_STATUS = "SYSTEM_CONFIG_SAVE_STATUS";
    }
}

