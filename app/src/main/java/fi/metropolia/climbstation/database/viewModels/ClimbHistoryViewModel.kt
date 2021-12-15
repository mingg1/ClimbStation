package fi.metropolia.climbstation.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.ClimbStationDB
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.repositories.ClimbHistoryRepository
import kotlinx.coroutines.runBlocking

class ClimbHistoryViewModel(application: Application): AndroidViewModel(application) {

    val getClimbHistoryHistory: LiveData<List<ClimbHistory>>
   private val historyRepository : ClimbHistoryRepository

    init {
        val climbDao = ClimbStationDB.getDatabase(application).climbHistoryDao()
        historyRepository = ClimbHistoryRepository(climbDao)
        getClimbHistoryHistory = historyRepository.getClimbHistory
    }

//    fun getClimbHistory(): LiveData<List<Climb>>{
//        var climbHistories: LiveData<List<Climb>>
//        runBlocking {
//            climbHistories = repository.getClimbHistory()
//        }
//        return climbHistories
//    }

    fun getClimbHistoryById(id:Long): ClimbHistory {
        return runBlocking {
            historyRepository.getClimbHistoryById(id)
        }
    }

    fun addClimbHistory(climbHistory: ClimbHistory){
        runBlocking {
            historyRepository.addClimbHistory(climbHistory)
        }
    }
}