package fi.metropolia.climbstation.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.entities.ClimbHistory
import fi.metropolia.climbstation.database.ClimbStationDB
import fi.metropolia.climbstation.database.repositories.ClimbHistoryRepository
import kotlinx.coroutines.runBlocking

/**
 * Viewmodel for climbing history
 *
 * @author Minji Choi
 *
 */
class ClimbHistoryViewModel(application: Application) : AndroidViewModel(application) {

    val getClimbHistoryHistory: LiveData<List<ClimbHistory>>
    private val historyRepository: ClimbHistoryRepository

    init {
        val climbDao = ClimbStationDB.getDatabase(application).climbHistoryDao()
        historyRepository = ClimbHistoryRepository(climbDao)
        getClimbHistoryHistory = historyRepository.getClimbHistory
    }

    fun getClimbHistoryById(id: Long): ClimbHistory {
        return runBlocking {
            historyRepository.getClimbHistoryById(id)
        }
    }

    fun addClimbHistory(climbHistory: ClimbHistory) {
        runBlocking {
            historyRepository.addClimbHistory(climbHistory)
        }
    }
}