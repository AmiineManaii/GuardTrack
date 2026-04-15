package com.example.guardiantrack.data.remote.interceptor

import com.example.guardiantrack.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")

        // N'ajouter l'en-tête Authorization que si une clé est définie et n'est pas "none"
        if (BuildConfig.API_KEY.isNotEmpty() && BuildConfig.API_KEY != "none") {
            requestBuilder.addHeader("Authorization", "Bearer ${BuildConfig.API_KEY}")
        }

        return chain.proceed(requestBuilder.build())
    }
}