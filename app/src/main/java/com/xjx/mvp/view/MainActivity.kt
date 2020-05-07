package com.xjx.mvp.view

import android.widget.TextView
import com.xjx.mvp.R
import com.xjx.mvp.base.BaseActivity
import com.xjx.mvp.model.MainModel
import com.xjx.mvp.prensenter.MainPresenter
import com.xjx.mvp.prensenter.imp.MainViewImp

class MainActivity : BaseActivity<MainModel, MainActivity, MainViewImp, MainPresenter>(), MainViewImp {

    override fun initContentView(): Int {
        return R.layout.activity_main
    }

    override fun initModel(): MainModel {
        return MainModel()
    }

    override fun getCurrentActivity(): MainActivity {
        return this
    }

    override fun initPresenter(): MainPresenter {
        return MainPresenter()
    }

    override fun getTextView(): TextView {
        val textView: TextView = findViewById(R.id.text)
        return textView
    }
}
