package fi.metropolia.climbstation

import fi.metropolia.climbstation.util.Constants.Companion.ATHLETE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.BEGINNER_MODE
import fi.metropolia.climbstation.util.Constants.Companion.EASY_MODE
import fi.metropolia.climbstation.util.Constants.Companion.ENDURANCE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.POWER_MODE
import fi.metropolia.climbstation.util.Constants.Companion.PRO_ATHLETE_MODE
import fi.metropolia.climbstation.util.Constants.Companion.STRENGTH_MODE
import fi.metropolia.climbstation.util.Constants.Companion.WARM_UP_MODE
import java.util.ArrayList

val distances = (1..5).toList()
val baseProfile = TerrainProfile(distances[0], 15)
val angle12 = TerrainProfile(distances[0], 12)
val angle10 = TerrainProfile(distances[0], 10)
val angle5 = TerrainProfile(distances[0], 5)

class TerrainProfile(distance: Int, angle: Int) {
    val distance: Int
    val angle: Int

    init {
        this.distance = distance
        this.angle = angle
    }
}

class DifficultyLevel(name: String, profiles: List<TerrainProfile>) {
    val name: String
    val profiles: List<TerrainProfile>

    init {
        this.name = name
        this.profiles = profiles
    }
}

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
        difficultyLevels.add(DifficultyLevel(WARM_UP_MODE, listOf(baseProfile,)))
        difficultyLevels.add(DifficultyLevel(EASY_MODE, listOf(baseProfile)))
        difficultyLevels.add(DifficultyLevel(ENDURANCE_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(STRENGTH_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(POWER_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(ATHLETE_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel(PRO_ATHLETE_MODE, listOf()))
        difficultyLevels.add(DifficultyLevel("Superhuman", listOf()))
        difficultyLevels.add(DifficultyLevel("Conqueror", listOf()))
    }
}