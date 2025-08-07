package com.example.tummocassignment.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicles_table")
    fun getAllVehicles(): LiveData<List<VehicleEntity>>

    @Query("DELETE FROM vehicles_table")
    suspend fun deleteAllVehicles()
}

