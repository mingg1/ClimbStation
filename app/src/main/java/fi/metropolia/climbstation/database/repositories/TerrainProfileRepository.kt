package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.dao.TerrainProfileDao
import fi.metropolia.climbstation.database.entities.TerrainProfile

class TerrainProfileRepository(private val terrainProfileDao: TerrainProfileDao) {

    suspend fun addTerrainProfile(terrainProfile: TerrainProfile): Long {
        return terrainProfileDao.addTerrainProfile(terrainProfile)
    }

    suspend fun getTerrainProfiles(): List<TerrainProfile> = terrainProfileDao.getTerrainProfiles()
    suspend fun getTerrainProfileByName(name: String): TerrainProfile =
        terrainProfileDao.getTerrainProfileByName(name)

    suspend fun getTerrainProfileById(id: Long): TerrainProfile =
        terrainProfileDao.getTerrainProfileById(id)

    suspend fun getBaseTerrainProfiles(): List<TerrainProfile> =
        terrainProfileDao.getBaseTerrainProfiles()

     fun getCustomTerrainProfiles(): LiveData<List<TerrainProfile>> =
        terrainProfileDao.getCustomTerrainProfiles()

    suspend fun updateTerrainProfile(terrainProfile: TerrainProfile) =
        terrainProfileDao.updateTerrainProfile(terrainProfile)

    suspend fun deleteTerrainProfile(terrainProfile: TerrainProfile) = terrainProfileDao.deleteTerrainProfile(terrainProfile)
}