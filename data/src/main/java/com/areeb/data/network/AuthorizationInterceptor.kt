package com.areeb.data.network

import com.areeb.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request().newBuilder()
        request = request.addHeader("Authorization", "Bearer ${BuildConfig.TOKEN}")
        return chain.proceed(request.build())
    }
}