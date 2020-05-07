package com.xjx.mvp.base

open class BaseModel<C> {

    var mCallBack: C? = null

    fun setCallBack(callBack: C) {
        mCallBack = callBack
    }
}