package fi.metropolia.climbstation.database.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.ClimbStationDB
import fi.metropolia.climbstation.database.entities.TerrainProfile
import fi.metropolia.climbstation.database.repositories.TerrainProfileRepository
import kotlinx.coroutines.runBlocking

class TerrainProfileViewModel(application: Application):AndroidViewModel(application) {
    private val terrainProfileRepository: TerrainProfileRepository
    init {
        val terrainProfileDao = ClimbStationDB.getDatabase(application).terrainProfileDao()
        terrainProfileRepository = TerrainProfileRepository(terrainProfileDao)
    }

    fun getTerrainProfiles():List<TerrainProfile>{
        return runBlocking {
            terrainProfileRepository.getTerrainProfiles()
        }
    }

    fun addTerrainProfile(terrainProfile: TerrainProfile){
        runBlocking {
            terrainProfileRepository.addTerrainProfile(terrainProfile)
        }
    }

    fun getTerrainProfileByName(name:String):TerrainProfile{
        return runBlocking {
            terrainProfileRepository.getTerrainProfileByName(name)
        }
    }

    fun getTerrainProfileById(id:Int):TerrainProfile{
        return runBlocking {
            terrainProfileRepository.getTerrainProfileById(id)
        }
    }

    fun getCustomTerrainProfiles(custom:Int):LiveData<List<TerrainProfile>>{
        return runBlocking {
            terrainProfileRepository.getCustomTerrainProfiles(custom)
        }
    }

    fun updateTerrainProfile(terrainProfile: TerrainProfile){
        return runBlocking {
            terrainProfileRepository.updateTerrainProfile(terrainProfile)
        }
    }

    fun deleteTerrainProfile(terrainProfile: TerrainProfile){
        return runBlocking {
            terrainProfileRepository.deleteTerrainProfile(terrainProfile)
        }
    }
}
