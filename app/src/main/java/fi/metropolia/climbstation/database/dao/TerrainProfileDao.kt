package fi.metropolia.climbstation.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import fi.metropolia.climbstation.database.entities.TerrainProfile

@Dao
interface TerrainProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTerrainProfile(terrainProfile: TerrainProfile): Long

    @Query("SELECT * FROM terrain_profile_table ORDER BY id ASC")
    suspend fun getTerrainProfiles(): List<TerrainProfile>

    @Query("SELECT * FROM terrain_profile_table WHERE name= :name")
    suspend fun getTerrainProfileByName(name: String): TerrainProfile

    @Query("SELECT * FROM terrain_profile_table WHERE id= :id")
    suspend fun getTerrainProfileById(id: Int): TerrainProfile

    @Update
    suspend fun updateTerrainProfile(terrainProfile: TerrainProfile)
}