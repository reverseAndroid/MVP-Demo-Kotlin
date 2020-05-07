package com.xjx.mvp.prensenter

import android.util.Log
import com.xjx.mvp.base.BasePresenter
import com.xjx.mvp.bean.request.UserRequest
import com.xjx.mvp.bean.response.UserResponse
import com.xjx.mvp.model.MainModel
import com.xjx.mvp.model.imp.MainCallBack
import com.xjx.mvp.prensenter.imp.MainViewImp
import com.xjx.mvp.view.MainActivity

class MainPresenter : BasePresenter<MainModel, MainActivity, MainViewImp>(), MainCallBack {

    override fun initTitle() {

    }

    override fun initView() {
        mModel!!.setCallBack(this)
        val user = UserRequest()
        val bean = UserRequest.UserBean()
        bean.username = "dnn"
        bean.password = "123456"
        user.setUser(bean)
        mModel!!.setBaidu(user)
    }

    override fun initData() {

    }

    override fun callBack(response: UserResponse?) {
        Log.e("MainPresenter", response!!.getMsg())
    }
}