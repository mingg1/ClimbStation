package fi.metropolia.climbstation

import java.util.ArrayList

val baseProfile = TerrainProfile(1,15)

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

object DifficultyLevels {
    val difficultyLevels: MutableList<DifficultyLevel> = ArrayList()

    init {
        difficultyLevels.add(DifficultyLevel("Beginner", listOf(baseProfile, baseProfile, baseProfile)))
        difficultyLevels.add(DifficultyLevel("Warm up"))
        difficultyLevels.add(DifficultyLevel("Easy"))
        difficultyLevels.add(DifficultyLevel("Endurance"))
        difficultyLevels.add(DifficultyLevel("Strength"))
        difficultyLevels.add(DifficultyLevel("Power"))
        difficultyLevels.add(DifficultyLevel("Athlete"))
        difficultyLevels.add(DifficultyLevel("Pro Athlete"))
        difficultyLevels.add(DifficultyLevel("Superhuman"))
        difficultyLevels.add(DifficultyLevel("Conqueror"))
    }
}