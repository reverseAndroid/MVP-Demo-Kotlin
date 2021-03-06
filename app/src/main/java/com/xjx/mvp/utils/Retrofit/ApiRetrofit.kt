package com.xjx.mvp.utils.Retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.xjx.mvp.bean.request.UserRequest
import com.xjx.mvp.bean.response.UserResponse
import com.xjx.mvp.utils.Constant
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import rx.Observable


class ApiRetrofit : BaseApiRetrofit() {

    companion object {
        fun getApiRetrofit(): ApiRetrofit {
            return ApiRetrofit()
        }
    }

    private val gson = GsonBuilder().setLenient().create()

    private var retrofit = Retrofit.Builder()
        .baseUrl(Constant.BASE_URL)
        .client(getClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        //针对返回值是字符串不是json的工厂模式
//                .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build()

    private var mApi = retrofit.create(Api::class.java)

    @Multipart
    private fun getRequestBody(obj: Any): RequestBody? {
        val route = Gson().toJson(obj)
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route)
    }

    //登录
    fun login(user: UserRequest?): Observable<UserResponse?>? {
        return mApi?.login(user)
    }
}