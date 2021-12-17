package fi.metropolia.climbstation.database

import android.content.Context
import androidx.room.*
import fi.metropolia.climbstation.database.dao.ClimbHistoryDao
import fi.metropolia.climbstation.database.dao.TerrainProfileDao
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.entities.PhasesConverter
import fi.metropolia.climbstation.database.entities.TerrainProfile

@Database(
    entities = [(ClimbHistory::class), (TerrainProfile::class)],
    version = 1,
    exportSchema = false
)
@TypeConverters(PhasesConverter::class)
abstract class ClimbStationDB : RoomDatabase() {

    abstract fun climbHistoryDao(): ClimbHistoryDao
    abstract fun terrainProfileDao(): TerrainProfileDao

    companion object {
        @Volatile
        private var INSTANCE: ClimbStationDB? = null

        fun getDatabase(context: Context): ClimbStationDB {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClimbStationDB::class.java,
                    "climb_station_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}