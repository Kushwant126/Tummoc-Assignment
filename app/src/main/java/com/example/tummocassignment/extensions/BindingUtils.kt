package com.example.tummocassignment.extensions

import android.view.View
import com.example.tummocassignment.databinding.AddVehicleButtonBinding


fun AddVehicleButtonBinding.debounceClickListener(listener: View.OnClickListener) {
    buttonMCV.debounceClickListener(listener)
}