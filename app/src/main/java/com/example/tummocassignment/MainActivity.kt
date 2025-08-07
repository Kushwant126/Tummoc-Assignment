package com.example.tummocassignment

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tummocassignment.databinding.ActivityMainBinding
import com.example.tummocassignment.viewModel.OnBoardViewModel
import com.example.tummocassignment.fragments.OnBoardFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: OnBoardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.root.fitsSystemWindows = true
        binding.lifecycleOwner = this
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(binding.mainFrameLayout.id, OnBoardFragment()).commit()
    }
}