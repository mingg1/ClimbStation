package fi.metropolia.climbstation.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fi.metropolia.climbstation.database.entities.ClimbHistory

@Dao
interface ClimbHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addClimb(climbHistory: ClimbHistory):Long

    @Query("SELECT * FROM climb_history_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<ClimbHistory>>
}