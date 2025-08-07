package com.example.tummocassignment.roomDB

import com.example.tummocassignment.dataModel.Vehicle
import com.example.tummocassignment.roomDB.VehicleEntity

fun VehicleEntity.toVehicle(): Vehicle {
    return Vehicle().apply {
        ownerName = this@toVehicle.ownerName
        model = this@toVehicle.model
        brand = this@toVehicle.brand
        vehicleNumber = this@toVehicle.vehicleNumber
        fuelType = this@toVehicle.fuelType
        yearOfPurchase = this@toVehicle.yearOfPurchase
        vehicleAge = this@toVehicle.vehicleAge
    }
}

fun Vehicle.toEntity(): VehicleEntity {
    return VehicleEntity(
        ownerName = ownerName.toString(),
        model = model.toString(),
        brand = brand.toString(),
        vehicleNumber = vehicleNumber.toString(),
        fuelType = fuelType.toString(),
        yearOfPurchase = yearOfPurchase.toString(),
        vehicleAge = vehicleAge.toString()
    )
}
