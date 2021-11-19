package fi.metropolia.climbstation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Climb::class], version = 1, exportSchema = false)
abstract class ClimbDatabase: RoomDatabase() {
    abstract fun climbInterface(): ClimbInterface

    companion object{

        private var INSTANCE: ClimbDatabase? = null

        fun getDatabase(context: Context): ClimbDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClimbDatabase::class.java,
                    "climb_database"

                ).build()
                INSTANCE =instance
                return instance
            }
        }
    }


}