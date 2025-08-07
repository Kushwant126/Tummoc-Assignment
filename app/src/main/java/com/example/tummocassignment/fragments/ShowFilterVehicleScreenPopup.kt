package com.example.tummocassignment.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.R
import com.example.tummocassignment.dataModel.FilterDM
import com.example.tummocassignment.databinding.DialogueBottomSheetBinding
import com.example.tummocassignment.databinding.SelectVehicleTypeCard1Binding
import com.example.tummocassignment.extensions.debounceClickListener

class ShowFilterVehicleScreenPopup(
    private val context: Context,
    private val title: String,
    private val vehicleCategoryList: List<FilterDM>,
    private val brandList: List<FilterDM>,
    private val vehicleTypes: List<FilterDM>,
    private val onApplyFilters: (List<FilterDM>, List<FilterDM>) -> Unit,
    private val onClearFilters: () -> Unit)
{
    private lateinit var dialog: Dialog
    private lateinit var binding: DialogueBottomSheetBinding
    lateinit var adapter1: VehicleFilterAdapter1

    @SuppressLint("SetTextI18n")
    fun show() {
        binding = DialogueBottomSheetBinding.inflate(LayoutInflater.from(context))
        dialog = Dialog(context)
        dialog.setContentView(binding.root)

        dialog.window?.apply {
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
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

        binding.bottomButtonLL.visibility = View.VISIBLE
        binding.selectVehicleRV1.visibility = View.VISIBLE
        binding.selectVehicleRV1.layoutManager = LinearLayoutManager(context)
        binding.selectVehicleRV.setBackgroundResource(R.color._F8FAFC)
        binding.selectVehicleRV.layoutManager = LinearLayoutManager(context)

        val adapter2 = VehicleFilterAdapter2(emptyList()) {}
        binding.selectVehicleRV.adapter = adapter2

        adapter1 = VehicleFilterAdapter1(vehicleCategoryList) { selectedFilter ->
            vehicleCategoryList.forEach { it.isSelected = it == selectedFilter }
            when (selectedFilter.title) {
                "Brand" -> adapter2.updateData(brandList)
                "Fuel Type" -> adapter2.updateData(vehicleTypes)
            }
//            adapter1.notifyDataSetChanged()
            adapter2.notifyDataSetChanged()
        }
        binding.selectVehicleRV1.adapter = adapter1

        binding.clearAllButtonMCV.setOnClickListener {
            brandList.forEach { it.isSelected = false }
            vehicleTypes.forEach { it.isSelected = false }
            onClearFilters() // Notify parent
            popUpDismiss()
        }
        binding.applyButtonMCV.debounceClickListener {
            val selectedBrands = brandList.filter { it.isSelected }
            val selectedFuelTypes = vehicleTypes.filter { it.isSelected }

            onApplyFilters(selectedBrands, selectedFuelTypes)
            popUpDismiss()
        }

    }


    class VehicleFilterAdapter1(private val vehicles: List<FilterDM>, private val onVehicleSelected: (FilterDM) -> Unit) :
        RecyclerView.Adapter<VehicleFilterAdapter1.VehicleViewHolder>() {

        // Initially select the first item
        private var selectedPosition = vehicles.indexOfFirst { it.isSelected }.takeIf { it >= 0 } ?: 0

        // Ensure only one is selected
//        init { vehicles.forEachIndexed { index, item -> item.isSelected = index == selectedPosition } }
        init {
            vehicles.forEachIndexed { index, item -> item.isSelected = index == selectedPosition }
            onVehicleSelected(vehicles[selectedPosition]) // trigger initially
        }
        inner class VehicleViewHolder(val binding: SelectVehicleTypeCard1Binding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SelectVehicleTypeCard1Binding.inflate(inflater, parent, false)
            return VehicleViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VehicleViewHolder, @SuppressLint("RecyclerView") position: Int) {
            val vehicle = vehicles[position]
            holder.binding.filter = vehicle
            vehicle.checkStatus1()

            holder.binding.root.setOnClickListener {
                if (selectedPosition != position) {
                    val previousPosition = selectedPosition
                    selectedPosition = position

                    vehicles[previousPosition].isSelected = false
                    vehicles[previousPosition].checkStatus1()

                    vehicles[position].isSelected = true
                    vehicles[position].checkStatus1()

                    notifyItemChanged(previousPosition)
                    notifyItemChanged(position)

                    onVehicleSelected(vehicles[position])
                }
            }
        }

        override fun getItemCount(): Int = vehicles.size
    }

    class VehicleFilterAdapter2(private var vehicles: List<FilterDM>, private val onVehicleSelected: (FilterDM) -> Unit) :
        RecyclerView.Adapter<VehicleFilterAdapter2.VehicleViewHolder>() {

        inner class VehicleViewHolder(val binding: SelectVehicleTypeCard1Binding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = SelectVehicleTypeCard1Binding.inflate(inflater, parent, false)
            return VehicleViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
            val vehicle = vehicles[position]
            holder.binding.filter = vehicle
            vehicle.checkStatus2()

            holder.itemView.setOnClickListener {
                vehicle.isSelected = !vehicle.isSelected
                vehicle.checkStatus2()
                notifyItemChanged(position)
                onVehicleSelected(vehicle)
            }
        }

        override fun getItemCount(): Int = vehicles.size

        fun updateData(newList: List<FilterDM>) {
            vehicles = newList
            notifyDataSetChanged()
        }
    }
}