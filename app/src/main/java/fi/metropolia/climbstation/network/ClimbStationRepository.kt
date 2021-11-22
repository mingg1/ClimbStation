package fi.metropolia.climbstation.network

import android.util.Log
import retrofit2.Response

class ClimbStationRepository {
    private val api = RetrofitInstance.climbStationApi

    suspend fun logIn(serialNumber: String, userId: String, password: String): Response<LogInResponse> {
        val logInReq = LogInRequest(serialNumber, userId, password)
        return api.logIn(logInReq)
    }

    suspend fun getInfo(infoRequest: InfoRequest): InfoResponse {
        return api.climbstationInfo(infoRequest)
    }

    suspend fun setSpeed(speedRequest: SpeedRequest): ClimbStationResponse {
        Log.d("speedCheck","start")
        return api.setSpeed(speedRequest)
    }

    suspend fun setAngle(angleRequest: AngleRequest): ClimbStationResponse {
        Log.d("AngleCheck","start angle")
        return api.setAngle(angleRequest)
    }

    suspend fun setOperation(operationRequest: OperationRequest): ClimbStationResponse {
        return api.operation(operationRequest)
    }

}