package com.example.team6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.team6.databinding.ActivitySearchResultBinding

class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var resultList: MutableList<RentalProperty>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Results from searchText

        //Add Results to Results List

        //Setup RV


    }
}