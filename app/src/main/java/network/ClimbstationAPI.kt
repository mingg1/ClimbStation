package network

import retrofit2.http.Body
import retrofit2.http.POST

interface ClimbstationAPI {
    @POST("login")
    suspend fun logIn(@Body req: LogInRequest): LogInResponse

    @POST("climbstationinfo")
    suspend fun climbstationInfo(@Body req: InfoRequest): InfoResponse

    @POST("Operation")
    suspend fun operation(@Body req: OperationRequest): ClimbStationResponses

    @POST("setspeed")
    suspend fun setSpeed(@Body req: SpeedRequest): ClimbStationResponses

    @POST("setangle")
    suspend fun setAngle(@Body req: AngleRequest): ClimbStationResponses

    @POST("Break")
    suspend fun setBreak(@Body req: BreakRequest): ClimbStationResponses

    @POST("logout")
    suspend fun logOut(@Body req: LogOutRequest): ClimbStationResponses
}


