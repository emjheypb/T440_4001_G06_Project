package com.example.team6

import android.app.appsearch.SearchResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team6.databinding.ActivitySearchResultBinding

class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var resultList: MutableList<RentalProperty>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get Results from searchText

        //Add Results to Results List: resultList = retrieveResults()

        //Setup RV
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = SearchResultAdapter(resultList)
    }
}