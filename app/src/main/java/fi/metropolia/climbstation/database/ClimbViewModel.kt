package fi.metropolia.climbstation.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClimbViewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<Climb>>
    private val repository : ClimbRepository

    init {
        val climbInterface = ClimbDatabase.getDatabase(application).climbInterface()
        repository = ClimbRepository(climbInterface)
        readAllData = repository.readAllData
    }

    fun addClimb(climb: Climb){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addClimb(climb)
        }
    }
}