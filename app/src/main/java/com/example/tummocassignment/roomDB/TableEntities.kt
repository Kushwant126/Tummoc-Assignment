package com.example.tummocassignment.roomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles_table")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val brand: String,
    val model: String,
    val vehicleNumber: String,
    val fuelType: String,
    val yearOfPurchase: String,
    val vehicleAge: String,
    val ownerName: String
)

