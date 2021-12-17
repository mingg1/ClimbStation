package fi.metropolia.climbstation.network

import com.google.gson.annotations.SerializedName

/**
 * Collection of network responses
 *
 * @author Minji Choi
 *
 */
interface BaseResponse : BaseKey {
    val response: String
}

data class LogInResponse(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String,
    val clientKey: String
) : BaseResponse

data class InfoResponse(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String,
    @SerializedName("SpeedNow") val speedNow: String,
    @SerializedName("AngleNow") val angleNow: String,
    @SerializedName("Length") val length: String
) : BaseResponse

data class ClimbStationResponse(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String
) : BaseResponse
