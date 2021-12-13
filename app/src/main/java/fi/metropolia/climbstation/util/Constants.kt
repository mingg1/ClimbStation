package fi.metropolia.climbstation.util

class Constants {

    companion object {
        val CLIMB_MODES = listOf("Looping", "To next level", "Random")
        const val BASE_URL = "http://192.168.241.1:8800/"
        const val REQ_PACKET_NUM = "1"
        const val SERIAL_NUM = "20110001"
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