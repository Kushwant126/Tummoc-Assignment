package com.example.tummocassignment.fragments

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tummocassignment.R
import com.example.tummocassignment.dataModel.Vehicle
import com.example.tummocassignment.dataModel.VehicleDetails
import com.example.tummocassignment.databinding.FragmentAddVehicleDetailsBinding
import com.example.tummocassignment.extensions.debounceClickListener
import com.example.tummocassignment.extensions.setCustomFonts
import com.example.tummocassignment.extensions.showToastMessageNew
import com.example.tummocassignment.viewModel.OnBoardViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Period
import java.util.Calendar

@AndroidEntryPoint
class AddVehicleFragment : Fragment() {
    private var _binding: FragmentAddVehicleDetailsBinding? =null
    private val binding get() = _binding!!
    private val viewModel: OnBoardViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddVehicleDetailsBinding.inflate(inflater,container,false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    var selectedBrandName: String? = null
    var selectedModelName: String? = null

    val brandList = listOf(
        VehicleDetails().apply { name = "Tata"; vehicleIconRes = R.drawable.tata },
        VehicleDetails().apply { name = "Honda"; vehicleIconRes = R.drawable.honda },
        VehicleDetails().apply { name = "Hero"; vehicleIconRes = R.drawable.hero },
        VehicleDetails().apply { name = "Bajaj"; vehicleIconRes = R.drawable.bajaj },
        VehicleDetails().apply { name = "Yamaha"; vehicleIconRes = R.drawable.yamaha },
//        VehicleDetails().apply { name = "Others"; showImage = false }
    )

    val vehicleModelsMap = mapOf(
        "Tata" to listOf("Nexon", "Harrier", "Safari", "Punch", "Tiago", "Altroz", "Tigor").map { VehicleDetails().apply { name = it; showImage = false } },
        "Honda" to listOf("City", "Amaze", "WR-V", "Elevate", "Jazz", "Civic", "Accord").map { VehicleDetails().apply { name = it; showImage = false } },
        "Hero" to listOf("Splendor Plus", "HF Deluxe", "Passion Pro", "Xtreme 160R", "Glamour", "Maestro Edge 110", "Pleasure Plus").map { VehicleDetails().apply { name = it; showImage = false } },
        "Bajaj" to listOf("Pulsar 150", "Pulsar 200NS", "Avenger 220", "Platina", "Dominar 400", "CT 110", "Chetak").map { VehicleDetails().apply { name = it; showImage = false } },
        "Yamaha" to listOf("FZ-S FI", "MT-15", "R15 V4", "Ray ZR 125", "Fascino 125", "Aerox 155", "FZ-X").map { VehicleDetails().apply { name = it; showImage = false } },
        "Others" to emptyList()
    )


    val fuelTypeList = listOf(
        VehicleDetails().apply { name = "Petrol";showImage = false },
        VehicleDetails().apply { name = "Electric";showImage = false },
        VehicleDetails().apply { name = "Diesel" ;showImage = false },
        VehicleDetails().apply { name = "CNG";showImage = false }
    )


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.includedHeaderLayout.fragmentBackIV.debounceClickListener { requireActivity().supportFragmentManager.popBackStack() }

        val bold = ResourcesCompat.getFont(requireContext(), R.font.satoshi_bold)!!
        val regular = ResourcesCompat.getFont(requireContext(), R.font.satoshi_regular)!!
        binding.includeBrand.editText.setCustomFonts(bold, regular)
        binding.includeModel.editText.setCustomFonts(bold, regular)
        binding.includeFuelType.editText.setCustomFonts(bold, regular)
        binding.includeVehicleNumber.editText.setCustomFonts(bold, regular)
        binding.includeVehicleNumber.editText.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        binding.includeYOP.editText.setCustomFonts(bold, regular)
        binding.includeYOP.editText.inputType = InputType.TYPE_CLASS_NUMBER
        binding.includeOwnerName.editText.setCustomFonts(bold, regular)

        binding.includeBrand.editText.debounceClickListener {
            ShowVehicleScreenPopup( context = requireContext(), title = "Select Vehicle Brand", list = brandList) { selected ->
                if (selectedBrandName != selected.name) {
                    binding.includeModel.editText.setText("")  // Clear model
                    binding.includeFuelType.editText.setText("")  // Clear fuel type
                    selectedModelName = null // Also clear saved model name if needed

                    vehicleModelsMap[selectedBrandName]?.forEach { it.isSelected = false }
                    fuelTypeList.forEach { it.isSelected = false }
                }
                selectedBrandName = selected.name
                binding.includeBrand.editText.setText(selected.name)
            }.show()
        }
        binding.includeModel.editText.debounceClickListener {
            if (!validateAndShowToast(selectedBrandName to "Please select the Brand...")) return@debounceClickListener

            val models = vehicleModelsMap[selectedBrandName] ?: emptyList()
            ShowVehicleScreenPopup( context = requireContext(), title = "Select Vehicle Model", list = models) { selected ->
                if (selectedBrandName != selected.name){
                    binding.includeFuelType.editText.setText("")
                    fuelTypeList.forEach { it.isSelected = false }
                }
                selectedModelName = selected.name
                binding.includeModel.editText.setText(selected.name)
            }.show()
        }
        binding.includeFuelType.editText.debounceClickListener {
            if (!validateAndShowToast(selectedBrandName to "Please select the Brand...", selectedModelName to "Please select the Model..." ))
                return@debounceClickListener
            ShowVehicleScreenPopup( context = requireContext(), title = "Select Fuel Type", list = fuelTypeList) { selected ->
                binding.includeFuelType.editText.setText(selected.name)
            }.show()
        }

        binding.includeAddNewVehicle.buttonMCV.debounceClickListener {
            val brand = binding.includeBrand.editText.text?.toString()?.trim()
            val model = binding.includeModel.editText.text?.toString()?.trim()
            val vehicleNumber = binding.includeVehicleNumber.editText.text?.toString()?.trim()
            val fuelType = binding.includeFuelType.editText.text?.toString()?.trim()
            val year = binding.includeYOP.editText.text?.toString()?.trim()
            val ownerName = binding.includeOwnerName.editText.text?.toString()?.trim()

            // Check if any field is empty
            if (model.isNullOrEmpty() || brand.isNullOrEmpty() || vehicleNumber.isNullOrEmpty() || fuelType.isNullOrEmpty()
                || year.isNullOrEmpty() || ownerName.isNullOrEmpty()) {
                requireContext().showToastMessageNew("Please fill all the details")
                return@debounceClickListener
            }

            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            if (year.toIntOrNull() !in 2000..currentYear) {
                requireContext().showToastMessageNew("Enter a valid Year of Purchased between 2000 and $currentYear")
                return@debounceClickListener
            }

            val vehicle = Vehicle().apply {
                this.ownerName = ownerName
                this.model = model
                this.brand = brand
                this.vehicleNumber = vehicleNumber
                this.fuelType = fuelType
                this.yearOfPurchase = year
                this.vehicleAge = calculateVehicleAgeFromYear(year)
            }
//            viewModel.addVehicle(vehicle)
            viewModel.addVehicleDB(vehicle)
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun validateAndShowToast(vararg checks: Pair<String?, String>): Boolean {
        for ((value, message) in checks) {
            if (value.isNullOrEmpty()) {
                requireContext().showToastMessageNew(message)
                return false
            }
        }
        return true
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateVehicleAgeFromYear(year: String): String {
        return try {
            val vehicleDate = LocalDate.of(year.toInt(), 1, 1)
            val currentDate = LocalDate.now()
            val period = Period.between(vehicleDate, currentDate)
            "${period.years} years, ${period.months} months"
        } catch (e: Exception) { "Unknown" }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}