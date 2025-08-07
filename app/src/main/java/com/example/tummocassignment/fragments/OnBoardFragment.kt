package com.example.tummocassignment.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tummocassignment.R
import com.example.tummocassignment.dataModel.FilterDM
import com.example.tummocassignment.dataModel.Vehicle
import com.example.tummocassignment.databinding.FragmentOnBoardBinding
import com.example.tummocassignment.databinding.VehiclesListCardBinding
import com.example.tummocassignment.extensions.debounceClickListener
import com.example.tummocassignment.viewModel.OnBoardViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardFragment : Fragment() {

    private var _binding: FragmentOnBoardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnBoardViewModel by activityViewModels()
    private lateinit var adapter: VehicleAdapter
    private var vehicleList: List<Vehicle> = emptyList()

    @SuppressLint("DefaultLocale")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnBoardBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Observe or use Room data via viewModel here
        adapter = VehicleAdapter(emptyList())
        binding.vehiclesListRV.layoutManager = LinearLayoutManager(requireContext())
        binding.vehiclesListRV.adapter = adapter

        /*viewModel.vehicles.observe(viewLifecycleOwner) { list ->
            Log.d("VehiclesList", "Received ${list.size} vehicles:")
            list.forEach { vehicle -> Log.d("VehiclesList", vehicle.toString()) }
            Log.d("VehiclesList", "")

            vehicleList = list
            Log.d("VehiclesList1", "Saved vehicleList: ${vehicleList.size} items")
            vehicleList.forEach { Log.d("VehiclesList1", it.toString()) }
            Log.d("VehiclesList1", "")


            if (list.isNullOrEmpty()) {
                binding.noVehiclesTV.visibility = View.VISIBLE
                binding.vehiclesListRV.visibility = View.GONE
            } else {
                binding.noVehiclesTV.visibility = View.GONE
                binding.vehiclesListRV.visibility = View.VISIBLE
                adapter.updateVehicles(list)
            }
        }

        viewModel.vehiclesDB.observe(viewLifecycleOwner) { vehicleListDB ->
            vehicleListDB.forEach { Log.d("VehicleLog", it.toString()) }
            vehicleList = vehicleListDB
            if (vehicleList.isEmpty()) {
                binding.noVehiclesTV.visibility = View.VISIBLE
                binding.vehiclesListRV.visibility = View.GONE
            } else {
                binding.noVehiclesTV.visibility = View.GONE
                binding.vehiclesListRV.visibility = View.VISIBLE
                adapter.updateVehicles(vehicleList)
            }
        }*/

        viewModel.vehiclesDB.observe(viewLifecycleOwner) { list ->
            vehicleList = list // Save for external use

            if (list.isNullOrEmpty()) {
                binding.noVehiclesTV.visibility = View.VISIBLE
                binding.vehiclesListRV.visibility = View.GONE
            } else {
                binding.noVehiclesTV.visibility = View.GONE
                binding.vehiclesListRV.visibility = View.VISIBLE
                adapter.updateVehicles(list)
            }
            binding.totalVehiclesTV.text = String.format("%04d", vehicleList.size)
            binding.totalEVTV.text = String.format("%04d", vehicleList.count { it.fuelType.equals("Electric", ignoreCase = true) })
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.includeAddNewVehicle.buttonMCV.debounceClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left, R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right)
                .replace(R.id.mainFrameLayout, AddVehicleFragment()).addToBackStack(null).commit()
        }
        setStatusBar(R.color.black, false) // Dark background, light icons
//        binding.includeAddNewVehicle.buttonMCV.performClick()

        binding.filterMCV.debounceClickListener {
            ShowFilterVehicleScreenPopup(
                context = requireContext(),
                title = "Filter",
                vehicleCategoryList = vehicleCategoryList,
                brandList = brandList,
                vehicleTypes = vehicleTypes,
                onApplyFilters = { selectedBrands, selectedFuels ->
                    var filteredList = vehicleList.filter {
                        (selectedBrands.isEmpty() || it.brand in selectedBrands.map { b -> b.title }) &&
                                (selectedFuels.isEmpty() || it.fuelType in selectedFuels.map { f -> f.title }) }

                    binding.filterMCV.strokeColor = ContextCompat.getColor(requireContext(), R.color._1381FF)
                    if (filteredList.isEmpty()) {
                        binding.noVehiclesTV.visibility = View.VISIBLE
                        binding.vehiclesListRV.visibility = View.GONE
                    } else {
                        binding.noVehiclesTV.visibility = View.GONE
                        binding.vehiclesListRV.visibility = View.VISIBLE
                        adapter.updateVehicles(filteredList)
                    }
                },
                onClearFilters = {
                    binding.filterMCV.strokeColor = ContextCompat.getColor(requireContext(), R.color._2694A3B8)
                    adapter.updateVehicles(vehicleList)
                }
            ).show()
        }
    }
    val brandList = listOf("Tata", "Honda", "Hero", "Bajaj","Yamaha").map { FilterDM().apply { title = it } }
    val vehicleTypes = listOf("Petrol", "Electric", "Diesel", "CNG").map { FilterDM().apply { title = it } }
    val vehicleCategoryList = listOf("Brand", "Fuel Type").map { FilterDM().apply { title = it; checkBoxShowImage = false } }


    private fun setStatusBar(colorRes: Int, lightIcons: Boolean) {
        val window = requireActivity().window
        window.statusBarColor = ContextCompat.getColor(requireContext(), colorRes)
        ViewCompat.getWindowInsetsController(window.decorView)?.isAppearanceLightStatusBars = lightIcons
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        setStatusBar(R.color.white, true)
    }


    class VehicleAdapter(private var vehicles: List<Vehicle>) : RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder>() {

        class VehicleViewHolder(val binding: VehiclesListCardBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(vehicle: Vehicle) {
                binding.vehicle = vehicle
                binding.executePendingBindings()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehicleViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VehiclesListCardBinding.inflate(inflater, parent, false)
            return VehicleViewHolder(binding)
        }

        override fun onBindViewHolder(holder: VehicleViewHolder, position: Int) {
            holder.bind(vehicles[position])
        }

        override fun getItemCount(): Int = vehicles.size

        fun updateVehicles(newList: List<Vehicle>) {
            vehicles = newList
            notifyDataSetChanged() // for performance, use DiffUtil
        }
    }
}
