package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.Climb
import fi.metropolia.climbstation.database.dao.ClimbDao

class ClimbRepository (private val climbDao: ClimbDao){
    val readAllData: LiveData<List<Climb>> = climbDao.readAllData()

    suspend fun addClimb(climb: Climb){
        climbDao.addClimb(climb)
    }

}