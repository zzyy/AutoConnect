package com.example.yunzou.connection.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.TextView
import com.example.yunzou.connection.AppSetting
import com.example.yunzou.connection.R

import com.example.yunzou.connection.accessibility.WirelessDisplayAccessibilityService
import com.example.yunzou.connection.util.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<View>(R.id.device_info) as TextView

        val devices = BluetoothUtils.getConnectedDevices()

        if (!devices.isEmpty()) {
            textView.text = devices[0].name + " " + devices[0].address
        }

        btn_open_accessibility_setting.setOnClickListener { startAccessibilitySettingActivity() }

        sw_enable_auto_wireless_display.setOnCheckedChangeListener({ buttonView, isChecked ->
            if (isChecked) {
                if (!AccessibilityUtils.isServiceEnable(this,
                        WirelessDisplayAccessibilityService::class.java.simpleName)) {
                    showOpenAccessibilityDialog()
                }

            } else {

            }
        })

        btn_chose_bluetooth.setOnClickListener({ v: View ->
            val dialog = ChoiceBluetoothDialog(this, {device ->
                tv_bluetooth_mac.text = "${device.name}  ${device.address}"
                AppSetting.saveBluetoothInfo(device.name, device.address)
            })
            dialog.show()
        })

        tv_bluetooth_mac.text = "${AppSetting.getTriggerBluetoothName()}  ${AppSetting.getTriggerBluetoothAddress()}"

        btn_request_write_setting_permission.setOnClickListener({ v ->
            SystemSettingUtils.startRequestPremissionActivity()
        })

        btn_set_screen_horizontal.setOnClickListener({
            ProcessController.saveSystemOriginalSettings()
            ProcessController.setSystemScreenRotationHorizontal()
        })

        btn_restore_system_settings.setOnClickListener({
            ProcessController.restoreSystemOriginalSettings()
        })


        btn_connect_wifi_display.setOnClickListener({
            WifiDisplayUtils().startScanAndConnect();
        })
    }




    fun showOpenAccessibilityDialog() {
        AlertDialog.Builder(this)
                .setMessage("open Accessibility Setting")
                .setPositiveButton("Confirm",
                        { _, _ -> AccessibilityUtils.openAccessibilitySetting(this) })
                .setNegativeButton("Cancel") { _, _ -> sw_enable_auto_wireless_display.isChecked = false }
                .show()
    }

    override fun onResume() {
        super.onResume()

        if(SystemSettingUtils.isCanWriteSystemSetting()){
            tv_can_write_settings.text = "true"
            btn_request_write_setting_permission.isEnabled = false
        }else{
            tv_can_write_settings.text = "false"
            btn_request_write_setting_permission.isEnabled = true
        }


        if (!AccessibilityUtils.isServiceEnable(this,
                WirelessDisplayAccessibilityService::class.java.simpleName)) {
            sw_enable_auto_wireless_display.isChecked = false
        }

    }

    private fun startAccessibilitySettingActivity() {
        AccessibilityUtils.openAccessibilitySetting(this)
    }
}
