package com.example.tummocassignment.roomDB

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.tummocassignment.dataModel.Vehicle
import javax.inject.Inject

class VehicleRepository @Inject constructor(private val vehicleDao: VehicleDao) {

    fun getAllVehicles(): LiveData<List<Vehicle>> {
        return vehicleDao.getAllVehicles().map { list ->
            list.map { it.toVehicle() }
        }
    }
//    fun getAllVehiclesNew(): LiveData<List<Vehicle>> = vehicleDao.getAllVehicles()

    suspend fun insertVehicle(vehicle: Vehicle) {
        vehicleDao.insertVehicle(vehicle.toEntity())
    }

    suspend fun deleteAllVehicles() {
        vehicleDao.deleteAllVehicles()
    }
}





