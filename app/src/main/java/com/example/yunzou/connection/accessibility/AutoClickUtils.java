package com.example.yunzou.connection.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.example.yunzou.connection.util.ContextUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Simon on 2017/11/7.
 */

public class AutoClickUtils {
    private static final String TAG = "AutoClickUtils";
    private Context mContext;
    private AccessibilityEvent mAccessibilityEvent;
    private AccessibilityService mAccessibilityService;

    private void openWirelessDisplayActivity() {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings",
                "com.android.settings.ui.DevicesListActivity");
        mContext.startActivity(intent);
    }


    /**
     * 根据Text搜索所有符合条件的节点，模糊搜索方式
     *
     * @param text
     * @return
     */
    public boolean clickText(String text) {
        AccessibilityNodeInfo nodeInfo = getRootNodeInfo();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> nodeInfos =
                    nodeInfo.findAccessibilityNodeInfosByText(text);
            return performClick(nodeInfos);
        }
        return false;
    }

    /**
     * 获取根节点
     */
    private AccessibilityNodeInfo getRootNodeInfo() {
        Log.e(TAG, "getRootNodeInfo: ");
        AccessibilityEvent curEvent = mAccessibilityEvent;
        AccessibilityNodeInfo nodeInfo = null;
        if (Build.VERSION.SDK_INT >= 16) {
            if (mAccessibilityService != null) {
                // 获得窗体根节点
                nodeInfo = mAccessibilityService.getRootInActiveWindow();
            }
        } else {
            nodeInfo = curEvent.getSource();
        }
        return nodeInfo;
    }

    /**
     * 模拟点击
     *
     * @param nodeInfos
     * @return true 成功； false 失败。
     */
    private boolean performClick(List<AccessibilityNodeInfo> nodeInfos) {
        if (nodeInfos == null || nodeInfos.isEmpty()) {
            return false;
        }

        for (AccessibilityNodeInfo nodeInfo : nodeInfos ) {
            // 进行模拟点击
            if (nodeInfo.isEnabled()) {// 如果可以点击
                return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            }
        }

        return false;
    }

    public static String getSettingPackageName(){
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        PackageManager packageManager = ContextUtil.getContext().getPackageManager();
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);
        if (resolveInfo == null){
            return  "";
        }
        ActivityInfo activityInfo = resolveInfo.activityInfo;
        if (activityInfo == null){
            return  "";
        }

        return activityInfo.packageName;
    }

    public static void startSettingActivity(){
        Context ctx = ContextUtil.getContext();
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        ctx.startActivity(intent);
    }
}
