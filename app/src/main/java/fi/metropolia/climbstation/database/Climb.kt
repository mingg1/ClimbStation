package fi.metropolia.climbstation.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "climb_table")
data class Climb(
    @PrimaryKey(autoGenerate = true)
    val uid: Int,
    val dateTime: Int,
    val level: String,
    val length: Int,
    val duration: Int
    )
