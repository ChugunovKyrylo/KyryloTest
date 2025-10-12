package com.kyrylo.gifs.data.remote

import com.kyrylo.gifs.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

private val YOUR_KEY: String? = null

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", YOUR_KEY ?: BuildConfig.API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}