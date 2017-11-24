package com.example.yunzou.connection.util

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.AccessibilityNodeInfo
import com.example.yunzou.connection.accessibility.WirelessDisplayAccessibilityService

/**
 * Created by Simon on 2017/10/17.
 */
class AccessibilityUtils {

    companion object {
        private val TAG: String = "zy.AccessibilityUtils"


        fun isEnabled(serviceName: String): Boolean {
            val ctx = ContextUtil.getContext()
            val accessibilityManager: AccessibilityManager = ctx
                    .getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager

            val enableServicesInfoList = accessibilityManager
                    .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)

            return enableServicesInfoList.any { it.id.contains(serviceName) }
        }

        fun openAccessibilitySettingActivity(context: Context?) {
            var context = context
            if (context == null) {
                context = ContextUtil.getContext()!!
            }
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            if (context is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)
        }


        /**
         * 根据Text搜索所有符合条件的节点，模糊搜索方式
         *
         * @param text
         * @return
         */
        fun clickText(accessibilityService: AccessibilityService, text: String): Boolean {
            val rootNodeInfo = accessibilityService.getRootInActiveWindow()
            val nodeInfos = rootNodeInfo.findAccessibilityNodeInfosByText(text)
            return performClick(nodeInfos)
        }

        /**
         * 模拟点击
         *
         * @param nodeInfos
         * @return true 成功； false 失败。
         */
        private fun performClick(nodeInfos: List<AccessibilityNodeInfo>): Boolean {
            if (nodeInfos.isEmpty()) {
                return false
            }

            for (nodeInfo in nodeInfos) {
                // 进行模拟点击
                if (nodeInfo.isEnabled) {// 如果可以点击
                    return nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                }
            }
            return false
        }
    }


}