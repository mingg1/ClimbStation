package fi.metropolia.climbstation.database.repositories

import androidx.lifecycle.LiveData
import fi.metropolia.climbstation.database.dao.TerrainProfileDao
import fi.metropolia.climbstation.database.entities.TerrainProfile

class TerrainProfileRepository(private val terrainProfileDao: TerrainProfileDao) {

    suspend fun addTerrainProfile(terrainProfile: TerrainProfile): Long {
        return terrainProfileDao.addTerrainProfile(terrainProfile)
    }

    suspend fun getTerrainProfiles(): List<TerrainProfile> = terrainProfileDao.getTerrainProfiles()
    suspend fun getTerrainProfileByName(name: String):TerrainProfile = terrainProfileDao.getTerrainProfileByName(name)
    suspend fun getTerrainProfileById(id: Int):TerrainProfile = terrainProfileDao.getTerrainProfileById(id)
    suspend fun updateTerrainProfile(terrainProfile: TerrainProfile) =
        terrainProfileDao.updateTerrainProfile(terrainProfile)

}