package com.xjx.mvp

import android.app.Activity
import android.app.Application
import android.content.Context
import java.util.*

class App : Application() {

    companion object {
        private var mContext: Context? = null

        var mActivityList = ArrayList<Activity>()

        fun getContext(): Context? {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}