package com.example.yunzou.connection.accessibility

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.example.yunzou.connection.util.L
import com.example.yunzou.connection.util.ProcessController
import com.example.yunzou.connection.util.WifiDisplayUtils

class WirelessDisplayAccessibilityService : AccessibilityService() {
    private val TAG: String = "WirelessDisplayAccessibilityService"

    companion object {
        @SuppressLint("StaticFieldLeak")
        var service: AccessibilityService? = null
            private set(value) {
                field = value
            }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        L.d("$TAG >> onServiceConnected")

        service = this

        val serviceInfo: AccessibilityServiceInfo = this.serviceInfo
        serviceInfo.packageNames = arrayOf("com.xiaomi.miplay")
        serviceInfo.eventTypes = AccessibilityEvent.TYPE_WINDOWS_CHANGED or
                AccessibilityEvent.TYPE_VIEW_FOCUSED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        this.serviceInfo = serviceInfo
    }

    override fun onInterrupt() {
        L.d("$TAG >> onInterrupt")

    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        L.d("$TAG >> onAccessibilityEvent $event")

        if (!ProcessController.getInstance().isStartedProcessConnect) {
            return
        }

        if (WifiDisplayUtils.isConnectedWifiDisplay()) {
            return
        }

//        AccessibilityUtils.clickText()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        L.d("$TAG >> onUnbind")
        service = null
        return super.onUnbind(intent)
    }

}
