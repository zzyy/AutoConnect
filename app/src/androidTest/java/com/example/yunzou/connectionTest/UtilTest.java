package com.example.yunzou.connectionTest;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.yunzou.connection.accessibility.AutoClickUtils;
import com.example.yunzou.connection.util.ContextUtil;
import com.example.yunzou.connection.util.ProcessController;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Simon on 17/11/10.
 */

@RunWith(AndroidJUnit4.class)
public class UtilTest {

    @Before
    public void setUp(){
        ContextUtil.setContext(getContext());
    }

    @Test
    public void getSettingPackageName() {
        String settingPackageName = AutoClickUtils.getSettingPackageName();
        Log.d("zy>> ", settingPackageName);
        System.out.println(">>>> " + settingPackageName);

        ProcessController.connectWifiDisplay();
    }

    public Context getContext() {
        return InstrumentationRegistry.getContext();
    }

}
