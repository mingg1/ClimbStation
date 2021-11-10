package network

import retrofit2.http.Body
import retrofit2.http.POST

interface ClimbstationAPI {
    @POST("login")
    suspend fun logIn(@Body reqBody: LogInRequest): LogInResponse

    @POST("climbstationinfo")
    suspend fun climbstationInfo(@Body reqBody: InfoRequest): InfoResponse

    @POST("Operation")
    suspend fun operation(@Body reqBody: OperationRequest): ClimbStationResponse

    @POST("setspeed")
    suspend fun setSpeed(@Body reqBody: SpeedRequest): ClimbStationResponse

    @POST("setangle")
    suspend fun setAngle(@Body reqBody: AngleRequest): ClimbStationResponse

    @POST("Break")
    suspend fun setBreak(@Body reqBody: BreakRequest): ClimbStationResponse

    @POST("logout")
    suspend fun logOut(@Body reqBody: LogOutRequest): ClimbStationResponse
}


