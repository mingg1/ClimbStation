package fi.metropolia.climbstation.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

/**
 * Class for network viewmodel
 * @param repository required repository
 *
 * @author Minji Choi
 *
 */
class ClimbStationViewModel(private val repository: ClimbStationRepository) : ViewModel() {
    val loginResponse: MutableLiveData<Response<LogInResponse>?> = MutableLiveData()
    val infoResponse: MutableLiveData<InfoResponse> = MutableLiveData()
    val speedResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()
    val angleResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()
    val operationResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()

    // log in to the app
    fun logIn(serialNumber: String, id:String, pw:String) {
        viewModelScope.launch {
          try {
                val response = repository.logIn(serialNumber, id, pw)
                loginResponse.value = response
            } catch (err:Exception){
              loginResponse.value = null
              Log.d("err","$err.toString(), ${loginResponse.value}")
            }
        }
    }

    // get current climb info (angle, length)
    fun getInfo(serialNumber: String, clientKey: String){
        viewModelScope.launch {
            val infoRequest = InfoRequest(serialNumber,clientKey)
            val response = repository.getInfo(infoRequest)
            infoResponse.value = response
        }
    }

    // set speed
    fun setSpeed(serialNumber: String, clientKey: String, speed:String){
        viewModelScope.launch {
            val speedRequest = SpeedRequest(serialNumber,clientKey,speed)
            val response = repository.setSpeed(speedRequest)
            speedResponse.value = response
        }
    }

 // set angle
    fun setAngle(serialNumber: String, clientKey: String, angle:String){
        viewModelScope.launch {
            val angleRequest = AngleRequest(serialNumber,clientKey,angle)
            val response = repository.setAngle(angleRequest)
            angleResponse.value = response
        }
    }

    // start or stop operation
    fun setOperation(serialNumber: String, clientKey: String, operation:String) {
        viewModelScope.launch {
            val operationRequest = OperationRequest(serialNumber,clientKey,operation)
            val response = repository.setOperation(operationRequest)
            operationResponse.value = response
        }
    }
}