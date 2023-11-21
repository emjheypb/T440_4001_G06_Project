package com.example.team6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.team6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            performSearch()
        }

    }

    private fun performSearch() {
        //
    }



}