package com.example.tummocassignment.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummocassignment.dataModel.Vehicle
import com.example.tummocassignment.roomDB.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toMutableList

@HiltViewModel
class OnBoardViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _vehicles = MutableLiveData<List<Vehicle>>()
    val vehicles: LiveData<List<Vehicle>> = _vehicles
    fun addVehicle(vehicle: Vehicle) {
        val currentList = _vehicles.value?.toMutableList() ?: mutableListOf()
        currentList.add(vehicle)
        _vehicles.value = currentList
    }

    val vehiclesDB: LiveData<List<Vehicle>> = vehicleRepository.getAllVehicles()
    fun addVehicleDB(vehicle: Vehicle) {
        viewModelScope.launch {
            vehicleRepository.insertVehicle(vehicle)
        }
    }

    init {
//        loadVehicles()
    }

    private fun loadVehicles() {
        _vehicles.value = listOf(
//            Vehicle("Activa 4G", "Honda", "MH12AB1234", "Petrol", "2018", "6 years 3 months"),
//            Vehicle("Swift", "Maruti", "MH14XY5678", "Diesel", "2016", "8 years 1 month"),
//            Vehicle("Activa 4G", "Honda", "KA01AB1234", "Petrol", "2018", "6 years 2 months"),
//            Vehicle("Model 3", "Tesla", "MH12XY9876", "Electric", "2021", "3 years 1 month"),
//            Vehicle("Model 4", "Tesla", "MH12XY9876", "Electric", "2021", "3 years 1 month")
        )
    }


}
