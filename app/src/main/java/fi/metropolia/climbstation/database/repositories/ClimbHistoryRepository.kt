package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.dao.ClimbHistoryDao

class ClimbHistoryRepository (private val climbHistoryDao: ClimbHistoryDao){

    suspend fun addClimbHistory(climbHistory: ClimbHistory):Long{
        return climbHistoryDao.addClimb(climbHistory)
    }

   val getClimbHistory:LiveData<List<ClimbHistory>> = climbHistoryDao.readAllData()


}