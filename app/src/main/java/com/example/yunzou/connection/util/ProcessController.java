package com.example.yunzou.connection.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import com.example.yunzou.connection.AppSetting;
import com.example.yunzou.connection.accessibility.WirelessDisplayAccessibilityService;

import static org.joor.Reflect.*;


/**
 * Created by Simon on 17/11/15.
 */

public class ProcessController {
    private static final String TAG = "zy.ProcessController";

    private boolean isStartedProcessConnect = false;

    private ProcessController() {
    }

    private static ProcessController sINSTANCE;

    public static ProcessController getInstance() {
        if (sINSTANCE == null) {
            synchronized (ProcessController.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new ProcessController();
                }
            }
        }
        return sINSTANCE;
    }


    public boolean isStartedProcessConnect() {
        return isStartedProcessConnect;
    }

    public void processConnect() {
        if (isStartedProcessConnect) {
            return;
        }
        this.isStartedProcessConnect = true;

        saveSystemOriginalSettings();

        setSystemScreenRotationHorizontal();

        connectWifiDisplay();
    }

    private void startAccessbilityProcess() {
        Context context = ContextUtil.getContext();
        boolean isEnable = AccessibilityUtils.Companion.isEnabled(WirelessDisplayAccessibilityService.class.getName());
        if (isEnable) {
            AccessibilityUtils.Companion.openAccessibilitySettingActivity(null);
        }
    }


    public static void openAccessibilitySettingActivity(Context context) {
        if (context == null) {
            context = ContextUtil.getContext();
        }
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        if (context instanceof Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }

    public static void connectWifiDisplay() {
        Context context = ContextUtil.getContext();
        DisplayManager displayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        for (Display display : displays) {
            Log.d(TAG, "" + display.getName() + "   " + display.getDisplayId());
        }

        Object wifiDisplayStatus = on(displayManager)
                .call("getWifiDisplayStatus")
                .get();

        Log.d(TAG, "getWifiDisplayStatus:  " + wifiDisplayStatus.toString());

//        Object activeWifiDisplay = on(displayManager)
//                .call("getWifiDisplayStatus")
//                .call("getActiveDisplay")
//                .get();
//
//        Log.d(TAG, "activeWifiDisplay:  "  + activeWifiDisplay.toString());
    }

    public void processDisconnect() {
        this.isStartedProcessConnect = false;
        restoreSystemOriginalSettings();
    }

    public static void setSystemScreenRotationHorizontal() {
        SystemSettingUtils.setEnableRotation(false);
        SystemSettingUtils.setUserRotation(Surface.ROTATION_90);
    }

    public static void saveSystemOriginalSettings() {
        if (AppSetting.isSystemSettingsSaved()) {
            return;
        }
        boolean isEnableRotation = SystemSettingUtils.isEnableAutoRotation();
        int userRotation = SystemSettingUtils.getUserRotation();
        AppSetting.saveScreenConfigInfo(isEnableRotation, userRotation);
        AppSetting.setSystemSettingsSaved(true);
    }

    public static void restoreSystemOriginalSettings() {
        if (!AppSetting.isSystemSettingsSaved()) {
            return;
        }
        boolean isEnableRotation = AppSetting.getOriginalScreenEnableRatation();
        int userRotation = AppSetting.getOriginalScreenRotation();
        SystemSettingUtils.setEnableRotation(isEnableRotation);
        SystemSettingUtils.setUserRotation(userRotation);

        AppSetting.setSystemSettingsSaved(false);
    }
}
