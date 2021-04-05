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


class ApiClient(private val ctx: Context?) {
    private var retrofit: Retrofit? = null
    private val gson: Gson get() = GsonBuilder().setLenient().create()

    private var clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
//        .readTimeout(3600, TimeUnit.SECONDS) //1 Min
//        .connectTimeout(3600, TimeUnit.SECONDS) ////1 Min

    private val headerAuthorizationInterceptor : Interceptor = Interceptor { chain ->
        var request: Request = chain.request()

        val headers: Headers = if (request.url().toString().contains(AppConstants.Endpoint.signin)
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
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clientBuilder.build())
                .build()
        }
        return retrofit
    }

    interface ApiCallbackListener<T> {
        fun onDataFetched(response: T?, isSuccess: Boolean, message: String)
    }

    interface ApiResponseListener<T> {
        fun onSuccess(response: T)
        fun onFailed(status: String, message: String)
    }
}