package fi.metropolia.climbstation

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class ClimbStationDB: RoomDatabase() {

// Dao for database
//    abstract fun historyDao(): HistoryDao
//    abstract fun difficultyLevelDao():DifficultyLevelDao

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