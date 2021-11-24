package fi.metropolia.climbstation.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "climb_table")
data class Climb(
    @PrimaryKey(autoGenerate = true)
    val uid: Long,
    val dateTime: Long,
    val level: String,
    val length: Int,
    val duration: Int,
    val speed: Int,
    val burntCalories: Double
)
