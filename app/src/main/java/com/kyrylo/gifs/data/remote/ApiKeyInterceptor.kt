package com.kyrylo.gifs.data.remote

import okhttp3.Interceptor
import okhttp3.Response

private const val API_KEY = "FsXTJPNNHE9ONwVg5IOxeMPPt1h7GXcI"

class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("api_key", API_KEY)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}