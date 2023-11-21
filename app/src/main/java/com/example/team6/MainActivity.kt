package com.example.team6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.example.team6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Search Handler with Enter
        binding.searchBarEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                // The Enter key was pressed, perform the search operation
                searchText = binding.searchBarEditText.text.toString().trim()
                performSearch()
                return@setOnEditorActionListener true
            }
            false
        }

        //Search Handler with Button
        binding.search.setOnClickListener {
            // If you want to perform search when the button is clicked
            searchText = binding.searchBarEditText.text.toString().trim()
            performSearch()
        }

    }

    private fun performSearch() {
        //
    }



}