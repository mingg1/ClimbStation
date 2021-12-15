package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.dao.ClimbHistoryDao
import fi.metropolia.climbstation.database.entities.TerrainProfile

class ClimbHistoryRepository (private val climbHistoryDao: ClimbHistoryDao){

    suspend fun addClimbHistory(climbHistory: ClimbHistory):Long{
        return climbHistoryDao.addClimb(climbHistory)
    }

   val getClimbHistory:LiveData<List<ClimbHistory>> = climbHistoryDao.readAllData()

    suspend fun getClimbHistoryById(id: Long): ClimbHistory =
        climbHistoryDao.getClimbHistoryById(id)
}