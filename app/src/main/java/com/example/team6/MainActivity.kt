package com.example.team6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.team6.databinding.ActivityMainBinding
import android.content.SharedPreferences
import com.example.team6.models.ContactDetails
import com.example.team6.models.PropertySpecifications
import com.example.team6.models.Rentals


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //lateinit var sharedPreferences: SharedPreferences
    //lateinit var prefEditor: SharedPreferences.Editor
    private var datasource:MutableList<Rentals> = mutableListOf<Rentals>(
        Rentals(propertyType = "Condo",
    ownerName = "Meridian Condo",
    ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
    imageURL = "https://shared-s3.property.ca/public/images/buildings/993/park-lane-2-3-pemberton-ave-normal-5da8a8528c9ff9.14275300.jpg",
    propertyName = "10 Northtown Way, North York, M2N7L4",
    specifications = PropertySpecifications(3, 2, 2),
    description = "A beautiful house for rent",
    address = "10 Northtown Way",
    postalCode = "M2N7L4",
    city = "North York",
    isAvailableForRent = true
    ), Rentals(
        propertyType = "House",
        ownerName = "Meridian Condo",
        ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
        propertyName = "15 Northtown Way, North York, M2N7L4",
        imageURL = "https://shared-s3.property.ca/public/images/buildings/9211/eleven-altamont-towns-11-altamont-rd-normal-60958d3242ef51.39708519.jpeg",
        specifications = PropertySpecifications(3, 2, 2),
        description = "A beautiful house for rent",
        address = "15 Northtown Way",
        postalCode = "M2N7L4",
        city = "North York",
        isAvailableForRent = true
    ), Rentals(
        propertyType = "Apartment",
        ownerName = "Meridian Condo",
        ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
        propertyName = "25 Northtown Way, North York, M2N7L4",
        imageURL = "https://shared-s3.property.ca/public/images/buildings/366/meridian-7-15-greenview-ave-272-274-duplex-ave-normal-1.jpg",
        specifications = PropertySpecifications(3, 2, 2),
        description = "A beautiful house for rent",
        address = "25 Northtown Way",
        postalCode = "M2N7L4",
        city = "North York",
        isAvailableForRent = true
    ),
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Shared Preferences
        //this.sharedPreferences = getSharedPreferences("FILTERED_LIST", MODE_PRIVATE)
        // Configure Shared Preferences in Editor Mode
        //this.prefEditor = this.sharedPreferences.edit()

        // Search Handler with Button
        binding.search.setOnClickListener {
            // If you want to perform search when the button is clicked
            val searchText = binding.searchBarEditText.text.toString().trim()
            performSearch(searchText)
        }
    }

    private fun performSearch(query: String) {
        val filteredList = datasource.filter {
            it.searchAble(query) }

        if (filteredList.isEmpty()) {
            Log.d("SearchResult-Sent", "No search results found.")
        }

        // Convert the list of Rentals objects to a list of JSON strings
        val rentalPropertyStringList = filteredList.map {
            it.toJson()
        }

        // Open SearchResultActivity and pass the filtered list
        val intent = Intent(this, SearchResult::class.java)
        intent.putExtra("FILTERED_LIST", rentalPropertyStringList.toTypedArray())
        startActivity(intent)

        Log.d("SearchLogic", "Filtered List Size: ${filteredList.size}")
        Log.d("SearchLogic", "Sample Data: ${filteredList.take(3)}")
    }
}
