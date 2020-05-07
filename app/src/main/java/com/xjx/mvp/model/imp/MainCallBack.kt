package com.xjx.mvp.model.imp

import com.xjx.mvp.bean.response.UserResponse

interface MainCallBack {

    fun callBack(response: UserResponse?)
}