package com.example.tummocassignment.dataModel

class Vehicle {
    var ownerName: String? = null
    var model: String? = null
    var brand: String? = null
    var vehicleNumber: String? = null
    var fuelType: String? = null
    var yearOfPurchase: String? = null
    var vehicleAge: String? = null

    override fun toString(): String {
        return "Vehicle(ownerName=$ownerName, model=$model, brand=$brand, vehicleNumber=$vehicleNumber, fuelType=$fuelType, yearOfPurchase=$yearOfPurchase, vehicleAge=$vehicleAge)"
    }

}