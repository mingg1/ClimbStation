package fi.metropolia.climbstation.database

import androidx.lifecycle.LiveData

class ClimbRepository (private val climbInterface: ClimbInterface){
    val readAllData: LiveData<List<Climb>> = climbInterface.readAllData()

    suspend fun addClimb(climb: Climb){
        climbInterface.addClimb(climb)
    }

}