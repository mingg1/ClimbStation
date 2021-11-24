package fi.metropolia.climbstation.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import fi.metropolia.climbstation.database.entities.Climb
import fi.metropolia.climbstation.database.ClimbDatabase
import fi.metropolia.climbstation.database.repositories.ClimbRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClimbViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Climb>>
    private val repository : ClimbRepository

    init {
        val climbInterface = ClimbDatabase.getDatabase(application).climbDao()
        repository = ClimbRepository(climbInterface)
        readAllData = repository.readAllData
    }

    fun addClimb(climb: Climb){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClimb(climb)
        }
    }
}