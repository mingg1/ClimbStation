package network

import com.google.gson.annotations.SerializedName


interface BaseResponse : BaseKeys {
    val response: String
}

data class LogInResponse(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String,
    @SerializedName("clientKey") val clientKey: String
) : BaseResponse

data class InfoResponse(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String,
    @SerializedName("SpeedNow") val speedNow: String,
    @SerializedName("AngleNow") val angleNow: String,
    @SerializedName("Length") val length: String
) : BaseResponse

data class ClimbStationResponses(
    @SerializedName("PacketId") override val packetId: String,
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("Response") override val response: String
) : BaseResponse