package com.markel.testtaskmoviecatalog.utils

import okhttp3.Interceptor
import okhttp3.Response

class ApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder().addQueryParameter("api_key", Api.TMDB_API_KEY).build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}