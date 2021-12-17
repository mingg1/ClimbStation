package fi.metropolia.climbstation.network

import fi.metropolia.climbstation.BuildConfig
import fi.metropolia.climbstation.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val interceptor = HttpLoggingInterceptor()

    val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        addNetworkInterceptor(interceptor)
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder().client(client).baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val climbStationApi: ClimbstationAPI by lazy {
        retrofit.create(ClimbstationAPI::class.java)
    }
}
                            