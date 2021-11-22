package fi.metropolia.climbstation.network

import com.google.gson.annotations.SerializedName
import fi.metropolia.climbstation.util.Constants.Companion.REQUEST
import fi.metropolia.climbstation.util.Constants.Companion.REQ_PACKET_NUM

// keys used in both response and request
interface BaseKey {
    val packetId: String
    val packetNumber: String
}

// keys used in every requests
interface BaseRequest : BaseKey {
    val climbStationSerialNo: String
}

interface ClientRequest : BaseRequest {
    val clientKey: String
}

data class LogInRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("UserID") val userId: String,
    @SerializedName("password") val password: String,
    @SerializedName("PacketID") override val packetId: String = "2a",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : BaseRequest

data class InfoRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("ClimbingData") val climbingData: String = REQUEST,
    @SerializedName("PacketID") override val packetId: String = "2b",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest

data class OperationRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Operation") val operation: String,
    @SerializedName("PacketID") override val packetId: String = "2c",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest

data class SpeedRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Speed") val speed: String,  // mm per sec
    @SerializedName("PacketID") override val packetId: String = "2d",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest

data class AngleRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Angle") val angle: String, // in degrees , -45 to +45
    @SerializedName("PacketID") override val packetId: String = "2e",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest

data class BreakRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Break") val breakOperation: String,
    //ON- Controller will apply  the Engine break
    //OFF- Controller will release the Engine break
    @SerializedName("PacketID") override val packetId: String = "2i",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest

data class LogOutRequest(
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Logout") val logOut: String = REQUEST,
    @SerializedName("PacketID") override val packetId: String = "2g",
    @SerializedName("PacketNumber") override val packetNumber: String = REQ_PACKET_NUM
) : ClientRequest
