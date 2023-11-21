package com.example.team6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.team6.databinding.ActivityShortlistedSearchBinding

class ShortlistedSearch : AppCompatActivity() {
    private lateinit var binding: ActivityShortlistedSearchBinding
    private lateinit var resultList: MutableList<RentalProperty>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShortlistedSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Results from searchText

        //Add Results to Results List

        //Setup RV


    }
}