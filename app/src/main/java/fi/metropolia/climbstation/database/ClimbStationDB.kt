package fi.metropolia.climbstation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fi.metropolia.climbstation.database.dao.ClimbDao
import fi.metropolia.climbstation.database.entities.Climb

@Database(entities = [Climb::class], version = 1, exportSchema = false)
abstract class ClimbStationDB : RoomDatabase() {

    abstract fun climbDao(): ClimbDao

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