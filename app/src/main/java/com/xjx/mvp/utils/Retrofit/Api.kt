package com.xjx.mvp.utils.Retrofit

import com.xjx.mvp.bean.request.UserRequest
import com.xjx.mvp.bean.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface Api {

    //登录
    @POST("doAppLogin")
    fun login(@Body requestBody: UserRequest?): Observable<UserResponse?>?
}