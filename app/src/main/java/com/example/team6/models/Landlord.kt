package com.example.team6.models

import com.example.team6.MainActivity
import com.example.team6.R
import com.example.team6.landlord.MyListing

class Landlord {
    val menuItems =
        listOf(R.id.mi_ll_home, R.id.mi_ll_properties, R.id.mi_ll_short_list, R.id.mi_ll_account, R.id.mi_ll_logout)
    fun redirect(id: Int) : Class<*> {
        when(id) {
            menuItems[0] -> return MainActivity::class.java
            menuItems[1] -> return MyListing::class.java
            menuItems[2] -> return MainActivity::class.java
            menuItems[3] -> return MainActivity::class.java
            menuItems[4] -> return MainActivity::class.java
        }

        return MainActivity::class.java
    }
}