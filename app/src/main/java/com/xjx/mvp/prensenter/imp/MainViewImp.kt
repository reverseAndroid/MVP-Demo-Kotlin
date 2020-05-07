package com.xjx.mvp.prensenter.imp

import android.widget.TextView
import com.xjx.mvp.view.MainActivity
import com.xjx.mvp.base.BaseViewImp

interface MainViewImp : BaseViewImp {

    fun getTextView(): TextView
}