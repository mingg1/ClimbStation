package fi.metropolia.climbstation.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Class for network viewmodel factory
 *
 * @author Minji Choi
 *
 */
class ClimbStationViewModelFactory(private val climbStationRepository: ClimbStationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClimbStationViewModel(climbStationRepository) as T
    }
}