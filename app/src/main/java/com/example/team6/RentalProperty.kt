package com.example.team6

class RentalProperty(
    val propertyType: String,
    val ownerName: String,
    val ownerContactDetails: ContactDetails,
    val specifications: PropertySpecifications,
    val description: String,
    val address: String,
    val isAvailableForRent: Boolean,
    // Add any additional information properties here
) {
    data class ContactDetails(
        val email: String,
        val phoneNumber: String
    )

    data class PropertySpecifications(
        val bedrooms: Int,
        val bathrooms: Int,
        val parkingLots: Int,
        // Add any additional specifications properties here
    )
}
