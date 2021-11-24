package fi.metropolia.climbstation.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fi.metropolia.climbstation.database.entities.Climb

@Dao
interface ClimbDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClimb(climb: Climb)

    @Query("SELECT * FROM climb_table ORDER BY uid ASC")
    fun readAllData(): LiveData<List<Climb>>
}