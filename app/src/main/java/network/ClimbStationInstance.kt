package network

import retrofit2.Retrofit

object ClimbStationAPI {
    private const val BASE_URL = "http://192.168.0.5:8800"

    private val rewardsRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val rewardsApi: RewardsApi by lazy {
        rewardsRetrofit.create(RewardsApi::class.java)
    }
}
