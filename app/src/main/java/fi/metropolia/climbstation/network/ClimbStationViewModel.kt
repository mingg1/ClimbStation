package fi.metropolia.climbstation.network

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ClimbStationViewModel(private val repository: ClimbStationRepository) : ViewModel() {
    val loginResponse: MutableLiveData<LogInResponse> = MutableLiveData()
    val infoResponse: MutableLiveData<InfoResponse> = MutableLiveData()
    val speedResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()
    val angleResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()
    val operationResponse: MutableLiveData<ClimbStationResponse> = MutableLiveData()

    fun logIn() {
        viewModelScope.launch {
            val response = repository.logIn("20110001","user","climbstation")
            loginResponse.value = response
        }
    }

    fun getInfo(serialNumber: String, clientKey: String){
        viewModelScope.launch {
            val infoRequest = InfoRequest(serialNumber,clientKey)
            val response = repository.getInfo(infoRequest)
            infoResponse.value = response
        }
    }

    fun setSpeed(serialNumber: String, clientKey: String, speed:String){
        viewModelScope.launch {
            val speedRequest = SpeedRequest(serialNumber,clientKey,speed)
            val response = repository.setSpeed(speedRequest)
            speedResponse.value = response
        }
    }

    fun setAngle(serialNumber: String, clientKey: String, angle:String){
        viewModelScope.launch {
            val angleRequest = AngleRequest(serialNumber,clientKey,angle)
            val response = repository.setAngle(angleRequest)
            angleResponse.value = response
        }
    }

    fun setOperation(serialNumber: String, clientKey: String, operation:String) {
        viewModelScope.launch {
            val operationRequest = OperationRequest(serialNumber,clientKey,operation)
            val response = repository.setOperation(operationRequest)
            operationResponse.value = response
        }
    }
}