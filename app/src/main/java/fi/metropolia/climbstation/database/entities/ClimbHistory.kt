package fi.metropolia.climbstation.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Schema of climbing history
 *
 * @author Minji Choi
 *
 */
@Entity(tableName = "climb_history_table")
data class ClimbHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val dateTime: Long,
    val level: String,
    val climbedLength: Int,
    val goalLength: Int,
    val duration: String,
    val speed: Int,
    val burntCalories: Double
)
