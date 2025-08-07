package com.example.tummocassignment.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.R
import com.example.tummocassignment.dataModel.VehicleDetails
import com.example.tummocassignment.databinding.DialogueBottomSheetBinding
import com.example.tummocassignment.databinding.SelectVehicleCardBinding
import com.example.tummocassignment.extensions.debounceClickListener
import kotlin.collections.forEach

class ShowVehicleScreenPopup (
    private val context: Context,
    private val title: String,
    private val list: List<VehicleDetails>,
    private val onItemSelected: (VehicleDetails) -> Unit)
{
    private lateinit var dialog: Dialog
    private lateinit var binding: DialogueBottomSheetBinding

    @SuppressLint("SetTextI18n")
    fun show() {
        binding = DialogueBottomSheetBinding.inflate(LayoutInflater.from(context))
        dialog = Dialog(context)
        dialog.setContentView(binding.root)

        dialog.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
            attributes = attributes?.apply {
                windowAnimations = R.style.DialogBottomAnimation
                gravity = Gravity.BOTTOM
            }
        }

        updateUI(binding)

        val slideUpAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        binding.root.startAnimation(slideUpAnimation)

        binding.transparentBackground.setOnClickListener { popUpDismiss() }
        binding.includedHeaderLayout.closeIV.debounceClickListener { popUpDismiss() }

        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun popUpDismiss() {
        val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)
        binding.root.startAnimation(slideDown)
        binding.root.postDelayed({ dialog.dismiss() }, 300)
    }

    private fun updateUI(binding: DialogueBottomSheetBinding) {
        binding.headerTitleNew = title
        binding.selectVehicleRV.layoutManager = LinearLayoutManager(context) // <-- Add this
        val adapter = VehicleDetailsAdapter(list) {
            onItemSelected(it)
            dialog.dismiss()
        }
        binding.selectVehicleRV.adapter = adapter
    }

    class VehicleDetailsAdapter(private val vehicles: List<VehicleDetails>, private val onVehicleSelected: (VehicleDetails) -> Unit) :
        RecyclerView.Adapter<VehicleDetailsAdapter.VehicleViewHolder>() {

        private var selectedPosition = vehicles.indexOfFirst { it.isSelected }

        inner class VehicleViewHolder(val binding: SelectVehicleCardBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SelectVehicleCardBinding.inflate(inflater, parent, false)
            return VehicleViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
            val vehicle = vehicles[position]
            holder.binding.vehicle = vehicle
//            holder.binding.isLogoVisible = showImage
            holder.binding.vehicleRadio.isChecked = position == selectedPosition

//            val checkedColor = ContextCompat.getColor(holder.itemView.context, R.color._3CB1F7)
//            val defaultColor = ContextCompat.getColor(holder.itemView.context, R.color._CBD5E1)
//            val isSelected = position == selectedPosition
//            val colorStateList = ColorStateList.valueOf(if (isSelected) checkedColor else defaultColor)
//            holder.binding.vehicleRadio.buttonTintList = colorStateList

            holder.binding.vehicle!!.checkStatus()
            holder.binding.root.setOnClickListener {
                if (selectedPosition != position) {
                    val previous = selectedPosition
                    selectedPosition = holder.adapterPosition

                    // Update selection status in data list
                    vehicles.forEachIndexed { index, v -> v.isSelected = index == selectedPosition }

                    notifyItemChanged(previous)
                    notifyItemChanged(selectedPosition)

                    onVehicleSelected(vehicle)
                }
            }

            holder.binding.executePendingBindings()
        }

        override fun getItemCount(): Int = vehicles.size
    }
}
