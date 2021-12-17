package fi.metropolia.climbstation.util

import fi.metropolia.climbstation.util.Constants.Companion.ATHLETE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.BEGINNER_MODE
import fi.metropolia.climbstation.util.Constants.Companion.CONQUEROR_MODE
import fi.metropolia.climbstation.util.Constants.Companion.EASY_MODE
import fi.metropolia.climbstation.util.Constants.Companion.ENDURANCE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.POWER_MODE
import fi.metropolia.climbstation.util.Constants.Companion.PRO_ATHLETE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.STRENGTH_MODE
import fi.metropolia.climbstation.util.Constants.Companion.SUPERHUMAN_MODE
import fi.metropolia.climbstation.util.Constants.Companion.WARM_UP_MODE
import java.util.ArrayList

val baseDistances = (1..6).toList()
val baseProfile = makeProfile(baseDistances[0], 15)
val profile112 = makeProfile(baseDistances[0], 12)
val profile110 = makeProfile(baseDistances[0], 10)
val profile15 = makeProfile(baseDistances[0], 5)
val profile10 = makeProfile(baseDistances[0], 0)
val profile2minus10 = makeProfile(baseDistances[1], -10)
val profile210 = makeProfile(baseDistances[1], 10)
val profile212 = makeProfile(baseDistances[1], 12)
val profile215 = makeProfile(baseDistances[1], 15)
val profile23 = makeProfile(baseDistances[1], 3)
val profile25 = makeProfile(baseDistances[1], 5)
val profile3minus5 = makeProfile(baseDistances[2], -5)
val profile3minus10 = makeProfile(baseDistances[2], -10)
val profile3minus15 = makeProfile(baseDistances[2], -15)
val profile30 = makeProfile(baseDistances[2], 0)
val profile35 = makeProfile(baseDistances[2], 5)
val profile45 = makeProfile(baseDistances[3], 5)
val profile4Minus5 = makeProfile(baseDistances[3], -5)
val profile4minus15 = makeProfile(baseDistances[3], -15)
val profile4minus35 = makeProfile(baseDistances[3], -35)

fun makeProfile(distance: Int, angle: Int) = Phase(distance, angle)

class Phase(val distance: Int, val angle: Int)

class DifficultyLevel(val name: String, val profiles: List<Phase>)

object TerrainProfilesObject {
    val terrainProfiles: MutableList<DifficultyLevel> = ArrayList()

    init {
        terrainProfiles.add(
            DifficultyLevel(
                BEGINNER_MODE, listOf(
                    baseProfile,
                    baseProfile,
                    baseProfile,
                    profile112,
                    profile110,
                    profile110,
                    profile110,
                    profile112,
                    profile15,
                    baseProfile
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                WARM_UP_MODE, listOf(
                    baseProfile, profile15, profile210, profile25, profile112,
                    profile210, profile215, profile10, profile25, baseProfile
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                EASY_MODE, listOf(
                    baseProfile,
                    profile10,
                    profile25,
                    profile10,
                    profile25,
                    profile212,
                    Phase(
                        baseDistances[1], -3
                    ),
                    profile23,
                    profile110,
                    profile212
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                ENDURANCE_MODE, listOf(
                    profile210, Phase(
                        baseDistances[0], -4
                    ), profile30, profile23, profile3minus5, profile3minus5,
                    Phase(baseDistances[0], 5), profile2minus10,
                    Phase(baseDistances[0], 2), profile210
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                STRENGTH_MODE, listOf(
                    profile210,
                    Phase(baseDistances[1], -6),
                    Phase(baseDistances[2], -2),
                    profile25,
                    Phase(baseDistances[2], -8),
                    profile30,
                    profile2minus10,
                    Phase(baseDistances[1], 8),
                    profile3minus15,
                    profile35
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                POWER_MODE, listOf(
                    profile25,
                    profile3minus10,
                    profile4Minus5,
                    Phase(baseDistances[0], 8),
                    profile4minus15,
                    Phase(baseDistances[1], 7),
                    profile3minus15,
                    Phase(baseDistances[1], 4),
                    Phase(baseDistances[2], -18),
                    profile3minus15
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                ATHLETE_MODE, listOf(
                    profile35,
                    Phase(baseDistances[1], -15),
                    profile3minus10,
                    profile15,
                    Phase(baseDistances[4], -12),
                    Phase(baseDistances[2], -20),
                    profile23,
                    Phase(baseDistances[1], -25),
                    profile3minus5,
                    profile3minus10
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                PRO_ATHLETE_MODE,
                listOf(
                    profile45,
                    Phase(baseDistances[2], -22),
                    Phase(baseDistances[3], 7),
                    Phase(baseDistances[4], -30),
                    profile45,
                    Phase(baseDistances[2], -33),
                    Phase(baseDistances[2], 8),
                    Phase(baseDistances[4], -22),
                    Phase(baseDistances[1], 0),
                    Phase(baseDistances[1], -25)
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                SUPERHUMAN_MODE,
                listOf(
                    Phase(baseDistances[3], 0),
                    Phase(baseDistances[2], -40),
                    Phase(baseDistances[4], 5),
                    Phase(baseDistances[5], -35),
                    Phase(baseDistances[4], 8),
                    Phase(baseDistances[4], -42),
                    profile4minus15,
                    Phase(baseDistances[3], 3),
                    profile4minus35,
                    Phase(baseDistances[2], -40),
                )
            )
        )
        terrainProfiles.add(
            DifficultyLevel(
                CONQUEROR_MODE,
                listOf(
                    profile4Minus5,
                    Phase(baseDistances[2], -45),
                    Phase(baseDistances[3], 8),
                    Phase(baseDistances[4], -40),
                    profile45,
                    Phase(baseDistances[3], -43),
                    Phase(baseDistances[4], -20),
                    profile3minus10,
                    Phase(baseDistances[5], -38),
                    profile210
                )
            )
        )
    }
}