package fi.metropolia.climbstation.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ClimbInterface {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClimb(climb: Climb)

    @Query("SELECT * FROM climb_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Climb>>
}