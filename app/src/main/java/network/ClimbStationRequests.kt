package network

import com.google.gson.annotations.SerializedName

// keys used in both response and request
interface BaseKeys {
    val packetId: String
    val packetNumber: String
}

// keys used in every requests
interface BaseRequest : BaseKeys {
    override val packetNumber: String
        get() = "1"
    val climbStationSerialNo: String
}

interface ClientRequest : BaseRequest {
    val clientKey: String
}

data class LogInRequest(
    @SerializedName("PacketID") override val packetId: String = "2a",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("UserID") val userId: String,
    @SerializedName("password") val password: String
) : BaseRequest

data class InfoRequest(
    @SerializedName("PacketID") override val packetId: String = "2b",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("ClimbingData") val climbingData: String = "request",
) : ClientRequest

data class OperationRequest(
    @SerializedName("PacketID") override val packetId: String = "2c",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Operation") val operation: String,
) : ClientRequest

data class SpeedRequest(
    @SerializedName("PacketID") override val packetId: String = "2d",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Speed") val speed: String // mm per sec
) : ClientRequest

data class AngleRequest(
    @SerializedName("PacketID") override val packetId: String = "2e",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Angle") val angle: String // in degrees , -45 to +45
) : ClientRequest

data class BreakRequest(
    @SerializedName("PacketID") override val packetId: String = "2i",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Break") val breakOperation: String
    //ON- Controller will apply  the Engine break
    //OFF- Controller will release the Engine break
) : ClientRequest

data class LogOutRequest(
    @SerializedName("PacketID") override val packetId: String = "2g",
    @SerializedName("PacketNumber") override val packetNumber: String,
    @SerializedName("ClimbstationSerialNo") override val climbStationSerialNo: String,
    @SerializedName("clientKey") override val clientKey: String,
    @SerializedName("Logout") val logOut: String = "request"
) : ClientRequest
