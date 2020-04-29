package com.xjx.mvp

import android.app.Application
import android.content.Context

class App : Application() {

    companion object {
        private var mContext: Context? = null

        fun getContext(): Context? {
            return mContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }
}