package com.example.team6.models

import android.util.Log
import com.example.team6.enums.PropertyType
import com.google.gson.Gson
import java.io.Serializable

class Rentals (
    val propertyType: PropertyType,
    val owner: User,
    val propertyName: String,
    val imageURL: String,
    val specifications: PropertySpecifications,
    val description: String,
    val address: String,
    val postalCode: String,
    val city: String,
    val isAvailableForRent: Boolean,
) : Serializable {
    fun searchAble(query: String): Boolean {
        val queryNum = query.toIntOrNull()
        if(queryNum!=null){
            val searchInstance =
                propertyName.contains(query, ignoreCase = true)
                        || city.contains(query, ignoreCase = true)
                        || address.contains(query, ignoreCase = true)
                        || postalCode.contains(query, ignoreCase = true)
                        || owner.name.contains(query, ignoreCase = true)
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
                        || owner.name.contains(query, ignoreCase = true)
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

    override fun toString(): String {
        return "Rentals(propertyType=$propertyType, owner=$owner, propertyName='$propertyName', imageURL='$imageURL', specifications=$specifications, description='$description', address='$address', postalCode='$postalCode', city='$city', isAvailableForRent=$isAvailableForRent)"
    }

    companion object {
        // Function to create Rentals from JSON
        fun fromJson(json: String): Rentals {
            return Gson().fromJson(json, Rentals::class.java)
        }
    }

}
