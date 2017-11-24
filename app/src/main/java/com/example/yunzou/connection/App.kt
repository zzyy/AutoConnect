package com.example.yunzou.connection

import android.app.Application
import com.example.yunzou.connection.util.ContextUtil

/**
 * Created by Simon on 2017/10/20.
 */
class App : Application(){

    override fun onCreate() {
        super.onCreate()
        ContextUtil.setContext(this)
    }
}