package com.xjx.mvp.utils.Retrofit

import android.annotation.SuppressLint
import com.franmontiel.persistentcookiejar.ClearableCookieJar
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.xjx.mvp.App
import com.xjx.mvp.utils.LogUtils
import com.xjx.mvp.utils.NetUtils
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

open class BaseApiRetrofit {

    private var mClient: OkHttpClient? = null

    fun getClient(): OkHttpClient? {
        return mClient
    }

    fun BaseApiRetrofit() {
        /*================== common ==================*/

        // Log信息拦截器
        // HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);//这里可以选择拦截级别

        //cache
        val httpCacheDir = File(App.getContext()!!.cacheDir, "response")
        val cacheSize = 10 * 1024 * 1024 // 10 MiB
        val cache = Cache(httpCacheDir, cacheSize.toLong())

        //cookie
        val cookieJar: ClearableCookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.getContext()))

        //OkHttpClient
        mClient = OkHttpClient.Builder()
            .addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR)
            .addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(LoggingInterceptor())
            .cache(cache)
            .cookieJar(cookieJar)
            .build()
    }

    //header配置
    var REWRITE_HEADER_CONTROL_INTERCEPTOR =
        Interceptor { chain: Interceptor.Chain ->
            val request = chain.request()
                .newBuilder()
                .header("appid", "com.loans.lion")
                .header("Content-Type", "application/json")
                //                .header("access_token", SPUtils.getInstance().getString(Constant.TOKEN))
                //                .addHeader("Accept-Encoding", "gzip, deflate")
                //                .addHeader("Connection", "keep-alive")
                //                .addHeader("Accept", "*/*")
                //                .addHeader("Cookie", "add cookies here")
                .build()
            chain.proceed(request)
        }

    //cache配置
    var REWRITE_CACHE_CONTROL_INTERCEPTOR =
        Interceptor { chain: Interceptor.Chain ->
            //通过 CacheControl 控制缓存数据
            val cacheBuilder = CacheControl.Builder()
            //这个是控制缓存的最大生命时间
            cacheBuilder.maxAge(0, TimeUnit.SECONDS)
            //这个是控制缓存的过时时间
            cacheBuilder.maxStale(365, TimeUnit.DAYS)
            val cacheControl = cacheBuilder.build()

            //设置拦截器
            var request = chain.request()
            if (!NetUtils.isNetworkAvailable(App.getContext()!!)) {
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            val originalResponse = chain.proceed(request)
            if (NetUtils.isNetworkAvailable(App.getContext()!!)) {
                val maxAge = 0 //read from cache
                return@Interceptor originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 28 //tolerate 4-weeks stale
                return@Interceptor originalResponse.newBuilder()
                    .removeHeader("Prama")
                    .header("Cache-Control", "poublic, only-if-cached, max-stale=$maxStale")
                    .build()
            }
        }

    internal class LoggingInterceptor : Interceptor {
        @SuppressLint("DefaultLocale")
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var response: Response? = null
            try {
                //这个chain里面包含了request和response，所以你要什么都可以从这里拿
                val request = chain.request()
                val t1 = System.nanoTime() //请求发起的时间
                LogUtils.sf(
                    String.format(
                        "发送请求 %s on %s%n%s",
                        request.url(),
                        chain.connection(),
                        request.headers()
                    )
                )
                response = chain.proceed(request)
                //收到响应的时间
                val t2 = System.nanoTime()
                val responseBody = response.peekBody(1024 * 1024.toLong())
                LogUtils.sf(
                    String.format(
                        "接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                        response.request().url(),
                        responseBody.string(),
                        (t2 - t1) / 1e6,
                        response.headers()
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return response!!
        }
    }
}