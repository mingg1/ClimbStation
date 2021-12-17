package fi.metropolia.climbstation.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fi.metropolia.climbstation.util.Phase

@Entity(tableName = "terrain_profile_table")
data class TerrainProfile
    (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val phases: List<Phase>,
    val custom: Int
)

class PhasesConverter {
    @TypeConverter
    fun fromString(value: String?): List<Phase> {
        val listType = object : TypeToken<List<Phase>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Phase>): String {
        return Gson().toJson(list)
    }
}