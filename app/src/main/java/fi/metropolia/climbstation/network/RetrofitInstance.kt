package fi.metropolia.climbstation.network

import fi.metropolia.climbstation.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Object for retrofit instance
 *
 * @author Minji Choi
 *
 */
object RetrofitInstance {

    private val interceptor = HttpLoggingInterceptor()

    private val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        addNetworkInterceptor(interceptor)
    }.build()

    fun retrofitInstance(baseUrl:String):ClimbstationAPI {
       val retrofit = Retrofit.Builder().client(client).baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ClimbstationAPI::class.java)
    }
}
                            