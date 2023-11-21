package com.example.team6.models

import android.util.Log
import com.google.gson.Gson

class Rentals (
    val propertyType: String,
    val ownerName: String,
    val ownerContactDetails: ContactDetails,
    val propertyName: String,
    val imageURL: String,
    val specifications: PropertySpecifications,
    val description: String,
    val address: String,
    val postalCode: String,
    val city: String,
    val isAvailableForRent: Boolean,
) {
    fun searchAble(query: String): Boolean {
        val queryNum = query.toIntOrNull()
        if(queryNum!=null){
            val searchInstance =
                propertyName.contains(query, ignoreCase = true)
                        || city.contains(query, ignoreCase = true)
                        || address.contains(query, ignoreCase = true)
                        || postalCode.contains(query, ignoreCase = true)
                        || ownerName.contains(query, ignoreCase = true)
                        || specifications.bedrooms == (queryNum)
                        || specifications.bathrooms == (queryNum)
                        || specifications.parkingLots == (queryNum)
            if(searchInstance){
                Log.d("Rentals-Search", "Search Instance Existed for $query")
            } else {
                Log.d("Rentals-Search", "Search Instance Non-existed for $query")
            }
            return searchInstance
        } else {
            val searchInstance =
                propertyName.contains(query, ignoreCase = true)
                        || city.contains(query, ignoreCase = true)
                        || address.contains(query, ignoreCase = true)
                        || postalCode.contains(query, ignoreCase = true)
                        || ownerName.contains(query, ignoreCase = true)
            if(searchInstance){
                Log.d("Rentals-Search", "Search Instance Existed for $query")
            } else {
                Log.d("Rentals-Search", "Search Instance Non-existed for $query")
            }
        return searchInstance
    }
}
    // Function to convert Rentals to JSON
    fun toJson(): String {
        return Gson().toJson(this)
    }

    companion object {
        // Function to create Rentals from JSON
        fun fromJson(json: String): Rentals {
            return Gson().fromJson(json, Rentals::class.java)
        }
    }
}
