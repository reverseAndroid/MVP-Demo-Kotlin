package com.xjx.mvp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xjx.mvp.App

abstract class BaseActivity<M, A, V, P : BasePresenter<M, A, V>> : AppCompatActivity(), BaseViewImp {

    var mModel: M? = null
    var mPresenter: P? = null
    var mActivity: A? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initContentView())
        mModel = initModel()
        mActivity = getCurrentActivity() as A
        App.mActivityList.add(getCurrentActivity())
        mPresenter = initPresenter()
        mPresenter?.attachView(mModel as M, mActivity as A, this as V)
    }

    protected abstract fun initModel(): M

    protected abstract fun initContentView(): Int

    protected abstract fun initPresenter(): P

    override fun onDestroy() {
        super.onDestroy()
        for (i in App.mActivityList.indices) {
            if (App.mActivityList[i].javaClass.name == mActivity!!.javaClass.name) {
                App.mActivityList.removeAt(i)
                break
            }
        }

        mPresenter?.detachView()
        mPresenter = null
    }
}
