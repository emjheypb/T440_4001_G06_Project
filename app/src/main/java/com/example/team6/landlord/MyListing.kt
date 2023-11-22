package com.example.team6.landlord

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import com.example.team6.R
import com.example.team6.databinding.ActivityMyListingBinding
import com.example.team6.models.Landlord
import com.example.team6.models.Rentals
import com.google.gson.Gson

class MyListing : AppCompatActivity() {
    private lateinit var binding: ActivityMyListingBinding

    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    private val gson = Gson()

    private val myListings = mutableListOf<Rentals>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMyListingBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        val toolbar = this.binding.menuToolbar
        toolbar.setTitle("My Listing")
        toolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_landlord_options,
            menu
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId in Landlord().menuItems) {
            startActivity(Intent(this@MyListing, Landlord().redirect(item.itemId)))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}