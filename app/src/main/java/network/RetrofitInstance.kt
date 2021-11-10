package network

import network.util.Constants.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val client = OkHttpClient.Builder().apply { addInterceptor(ClimbStationInterceptor()) }.build()

    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val climbStationApi: ClimbstationAPI by lazy {
        retrofit.create(ClimbstationAPI::class.java)
    }
}
