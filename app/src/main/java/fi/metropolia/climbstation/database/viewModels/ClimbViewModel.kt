package fi.metropolia.climbstation.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.Climb
import fi.metropolia.climbstation.database.ClimbStationDB
import fi.metropolia.climbstation.database.repositories.ClimbRepository
import kotlinx.coroutines.runBlocking

class ClimbViewModel(application: Application): AndroidViewModel(application) {

    val getClimbHistory: LiveData<List<Climb>>
   private val repository : ClimbRepository

    init {
        val climbDao = ClimbStationDB.getDatabase(application).climbDao()
        repository = ClimbRepository(climbDao)
        getClimbHistory = repository.getClimbHistory
    }

//    fun getClimbHistory(): LiveData<List<Climb>>{
//        var climbHistories: LiveData<List<Climb>>
//        runBlocking {
//            climbHistories = repository.getClimbHistory()
//        }
//        return climbHistories
//    }

    fun addClimbHistory(climb: Climb){
        runBlocking {
            repository.addClimbHistory(climb)
        }
    }
}