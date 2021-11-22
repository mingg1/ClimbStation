package fi.metropolia.climbstation

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

val baseDistances = (1..5).toList()
val baseProfile = makeProfile(baseDistances[0], 15)
val angle12 = makeProfile(baseDistances[0], 12)
val angle10 = makeProfile(baseDistances[0], 10)
val angle5 = makeProfile(baseDistances[0], 5)
val profile10 = makeProfile(baseDistances[0],0)
val profile210 = makeProfile(baseDistances[1], 10)
val profile215 = makeProfile(baseDistances[1], 15)
val profile25 = makeProfile(baseDistances[1], 5)

fun makeProfile(distance: Int, angle: Int) = TerrainProfile(distance, angle)

class TerrainProfile(distance: Int, angle: Int) {
    val distance: Int
    val angle: Int

    init {
        this.distance = distance
        this.angle = angle
    }
}

class DifficultyLevel(val name: String, val profiles: List<TerrainProfile>)

object TerrainProfiles {
    val difficultyLevels: MutableList<DifficultyLevel> = ArrayList()

    init {
        difficultyLevels.add(
            DifficultyLevel(
                BEGINNER_MODE, listOf(
                    baseProfile,
                    baseProfile,
                    baseProfile,
                    angle12,
                    angle10,
                    angle10,
                    angle10,
                    angle12,
                    angle5,
                    baseProfile
                )
            )
        )
        difficultyLevels.add(
            DifficultyLevel(
                WARM_UP_MODE, listOf(
                    baseProfile, angle5, profile210, profile25, angle12,
                    profile210, profile215, profile10, profile25, baseProfile
                )
            )
        )
        difficultyLevels.add(DifficultyLevel(EASY_MODE, listOf(baseProfile)))
        difficultyLevels.add(DifficultyLevel(ENDURANCE_MODE, listOf(baseProfile)))
        difficultyLevels.add(DifficultyLevel(STRENGTH_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(POWER_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(ATHLETE_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(PRO_ATHLETE_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(SUPERHUMAN_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(CONQUEROR_MODE, listOf()))
    }
}