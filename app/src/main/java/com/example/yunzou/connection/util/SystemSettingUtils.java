package com.example.yunzou.connection.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.Surface;
import android.widget.Toast;

/**
 * Created by Simon on 17/11/13.
 */

public class SystemSettingUtils {


    public static boolean isCanWriteSystemSetting(){
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return true;
        }
        Context context = ContextUtil.INSTANCE.getContext();
        boolean canWrite = Settings.System.canWrite(context);
        return canWrite;
    }

    public static void startRequestPremissionActivity() {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            return;
        }
        Context context = ContextUtil.INSTANCE.getContext();
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static boolean isEnableAutoRotation() {
        Context context = ContextUtil.INSTANCE.getContext();
        int systemRotation = 0;
        try {
            systemRotation = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return systemRotation == 1;
    }

    public static int getUserRotation() {
        Context context = ContextUtil.INSTANCE.getContext();
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.USER_ROTATION);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return Surface.ROTATION_0;
        }
    }

    public static void setEnableRotation(boolean enable) {
        Context context = ContextUtil.INSTANCE.getContext();
        int systemRotation = enable ? 1 : 0;
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, systemRotation);
        } catch (Exception e) {
            Toast.makeText(context, "无修改系统配置权限", Toast.LENGTH_SHORT).show();
        }
    }

    public static void setUserRotation(int userRotation) {
        Context context = ContextUtil.INSTANCE.getContext();
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.USER_ROTATION, userRotation);
        } catch (Exception e) {
            Toast.makeText(context, "无修改系统配置权限", Toast.LENGTH_SHORT).show();
        }
    }
}
