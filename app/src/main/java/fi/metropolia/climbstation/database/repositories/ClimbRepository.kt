package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.Climb
import fi.metropolia.climbstation.database.dao.ClimbDao

class ClimbRepository (private val climbDao: ClimbDao){

    suspend fun addClimbHistory(climb: Climb):Long{
        return climbDao.addClimb(climb)
    }

   val getClimbHistory:LiveData<List<Climb>> = climbDao.readAllData()


}