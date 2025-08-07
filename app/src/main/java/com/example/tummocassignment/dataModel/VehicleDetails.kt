package com.example.tummocassignment.dataModel

import androidx.annotation.ColorRes
import com.example.tummocassignment.R
import com.example.tummocassignment.extensions.isTrue

class VehicleDetails {
    var name: String? = null
    var showImage: Boolean = true         // <-- control image visibility
    var vehicleIconRes: Int = R.drawable.tata
    var isSelected: Boolean = false       // <-- for radio button selection

    @ColorRes
    var cardBackGroundColor: Int = R.color._EBEBEB
    var cardElevation: Int? = 0

    @ColorRes
    var cardStrokeColor: Int? = R.color._3CB1F7

    @ColorRes
    var radioTintColor: Int? = R.color._CBD5E1
    fun checkStatus() {
        if (isSelected.isTrue()) setStatusOn() else setStatusOff()
    }

    fun setStatusOn(): VehicleDetails {
        cardStrokeColor = R.color._3CB1F7
        cardBackGroundColor = R.color._101381FF
        radioTintColor = R.color._3CB1F7
        cardElevation = 0
        return this
    }

    fun setStatusOff(): VehicleDetails {
        cardStrokeColor = R.color._E2E8F0
        cardBackGroundColor = R.color.white
        radioTintColor = R.color._CBD5E1
        cardElevation = 0
        return this
    }
}