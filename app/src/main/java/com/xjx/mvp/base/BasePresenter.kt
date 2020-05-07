package com.xjx.mvp.base

abstract class BasePresenter<M, A, V> {

    var mModel: M? = null
    var mActivity: A? = null
    var mView: V? = null

    fun attachView(model: M, activity: A, view: V) {
        mModel = model
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