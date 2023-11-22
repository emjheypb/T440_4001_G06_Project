package com.example.team6.models

import com.example.team6.enums.MembershipType
import java.io.Serializable

class User(val name:String, val email: String, val phoneNumber: String, val password: String, val membership: MembershipType, var rentalFavs: MutableList<Rentals>) :
    Serializable {
    override fun toString(): String {
        return "user(name='$name', email='$email', phoneNumber='$phoneNumber', password='$password', membership='${membership.description}, rentalFavs:'$rentalFavs'')"
    }

}