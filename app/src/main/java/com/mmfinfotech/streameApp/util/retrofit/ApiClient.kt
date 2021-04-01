package com.mmfinfotech.streameApp.util.retrofit

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mmfinfotech.streameApp.utils.AppConstants
import com.mmfinfotech.streameApp.utils.AppPreferences
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiClient(val ctx: Context?) {
    private val tag: String? = ApiClient::class.java.simpleName
    private var retrofit: Retrofit? = null
    private val gson: Gson? = GsonBuilder().setLenient().create()

    private var clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
//        .readTimeout(3600, TimeUnit.SECONDS) //1 Min
//        .connectTimeout(3600, TimeUnit.SECONDS) ////1 Min
    private val headerAuthorizationInterceptor : Interceptor = Interceptor { chain ->
        var request: Request = chain.request()
        val headers: Headers?
        headers = if (request.url().toString().contains(AppConstants.Endpoint.signin)
            || request.url().toString().contains(AppConstants.Endpoint.signup)
            || request.url().toString().contains(AppConstants.Endpoint.socialLogin)
            || request.url().toString().contains(AppConstants.Endpoint.verifyEmail)
            || request.url().toString().contains(AppConstants.Endpoint.anotherDevice)
            || request.url().toString().contains(AppConstants.Endpoint.checkUpdate)
        ) {
            request.headers()
                .newBuilder()
                .add("app_key", AppConstants.appKey)
                .add("app_secret", AppConstants.appSecret)
                .build()
        } else {
            request.headers()
                .newBuilder()
                .add("Authorization", AppPreferences().getAuthToken(ctx)!!)
                .build()

        }

    request = request.newBuilder().headers(headers).build()
    chain.proceed(request)
    }

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            clientBuilder.addInterceptor(headerAuthorizationInterceptor)
            retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.Baseurl.url.trim { it <= ' ' })
                .addConverterFactory(GsonConverterFactory.create(gson!!))
                .client(clientBuilder.build())
                .build()
        }
        return retrofit
    }

}