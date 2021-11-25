package fi.metropolia.climbstation.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "climb_table")
data class Climb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val dateTime: Long,
    val level: String,
    val length: Int,
    val duration: String,
    val speed: Int,
    val burntCalories: Double
)
