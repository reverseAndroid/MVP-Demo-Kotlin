package com.xjx.mvp.base

abstract class BasePresenter<A, V> {

    var mActivity: A? = null
    var mView: V? = null

    fun attachView(activity: A, view: V) {
        mView = view
        mActivity = activity
        initTitle()
        initView()
        initData()
    }

    abstract fun initTitle()

    abstract fun initView()

    abstract fun initData()

    fun detachView() {
        mView = null
    }

    fun isViewAttached(): Boolean {
        return mView != null
    }
}