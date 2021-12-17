package fi.metropolia.climbstation.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ClimbstationAPI {
    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun logIn(@Body reqBody: LogInRequest): Response<LogInResponse>

    @Headers("Content-Type: application/json")
    @POST("climbstationinfo")
    suspend fun climbstationInfo(@Body reqBody: InfoRequest): InfoResponse

    @POST("Operation")
    suspend fun operation(@Body reqBody: OperationRequest): ClimbStationResponse

    @POST("setspeed")
    suspend fun setSpeed(@Body reqBody: SpeedRequest): ClimbStationResponse

    @POST("setangle")
    suspend fun setAngle(@Body reqBody: AngleRequest): ClimbStationResponse

}


