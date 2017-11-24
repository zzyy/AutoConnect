package com.example.yunzou.connection.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by Simon on 2017/10/16.
 */

public class BluetoothUtils {

    public static boolean isConnect() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        int a2dp = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        int headset = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
        int health = bluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);

        return a2dp == BluetoothProfile.STATE_CONNECTED || headset == BluetoothProfile.STATE_CONNECTED
                || health == BluetoothProfile.STATE_CONNECTED;
    }

    public static List<BluetoothDevice> getConnectedDevices() {
        if (!isConnect()) {
            return Collections.emptyList();
        }

        List<BluetoothDevice> deviceList = new ArrayList<BluetoothDevice>();

        try {
            List<BluetoothDevice> devices = getBoundedDevices();
            Log.i("BLUETOOTH", "devices:" + devices.size());

            for (BluetoothDevice device : devices) {
                Method isConnectedMethod = BluetoothDevice.class.getDeclaredMethod("isConnected", (Class[]) null);
                boolean isConnected = (boolean) isConnectedMethod.invoke(device, (Object[]) null);
                if (isConnected) {
                    Log.i("BLUETOOTH", "connected:" + device.getName());
                    deviceList.add(device);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceList;
    }

    public static List<BluetoothDevice> getBoundedDevices() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        return new ArrayList<>(devices);
    }


}
