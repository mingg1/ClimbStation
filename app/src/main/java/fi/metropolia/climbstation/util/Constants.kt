package fi.metropolia.climbstation.util

import android.content.Context
import fi.metropolia.climbstation.IniFileLoader


class Config(context: Context) : IniFileLoader(context) {
    val configReader = load("climbstation.conf")
    val mainVariables = getSectionDataMap("main")
    val baseUrl = mainVariables?.get("httpserverHost")
    val modes = mainVariables?.get("climbModes")
    val id = mainVariables?.get("idUser")
    val password = mainVariables?.get("pwUser")
}

class Constants {
    companion object {
        const val REQ_PACKET_NUM = "1"
        const val REQUEST = "request"
        const val BEGINNER_MODE = "Beginner"
        const val WARM_UP_MODE = "Warm up"
        const val EASY_MODE = "Easy"
        const val ENDURANCE_MODE = "Endurance"
        const val STRENGTH_MODE = "Strength"
        const val POWER_MODE = "Power"
        const val ATHLETE_MODE = "Athlete"
        const val PRO_ATHLETE_MODE = "Pro Athlete"
        const val SUPERHUMAN_MODE = "Superhuman"
        const val CONQUEROR_MODE = "Conqueror"
    }
}