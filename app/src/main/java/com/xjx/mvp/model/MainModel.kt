package com.xjx.mvp.model

import android.widget.Toast
import com.xjx.mvp.App
import com.xjx.mvp.base.BaseModel
import com.xjx.mvp.bean.request.UserRequest
import com.xjx.mvp.model.imp.MainCallBack
import com.xjx.mvp.utils.Retrofit.ApiRetrofit
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class MainModel : BaseModel<MainCallBack>() {

    fun setBaidu(user: UserRequest?) {
        ApiRetrofit.getApiRetrofit().login(user)
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { userResponse ->
                    if (userResponse?.getCode().equals("200")) {
                        mCallBack?.callBack(userResponse)
                    } else {
                        mCallBack?.callBack(userResponse)
                    }
                },
                {
                    Toast.makeText(App.getContext(), "程序发生异常", Toast.LENGTH_SHORT).show()
                })
    }
}