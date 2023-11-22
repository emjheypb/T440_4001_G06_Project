package com.example.team6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team6.databinding.ActivitySearchResultBinding
import com.example.team6.models.Rentals
import android.content.SharedPreferences
import android.content.res.Resources
import android.view.MenuItem
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso


class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var resultList: MutableList<Rentals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Set up click listeners for toolbar items
        binding.menuIcon.setOnClickListener {
            // Handle menu icon click
            showPopupMenu(it)
        }

        binding.teamNameTextView.setOnClickListener {
            // Handle team name click (redirect or refresh to MainActivity)
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Optional: finish the current activity to clear it from the stack
        }

        binding.loginRegisterButton.setOnClickListener {
            // Handle login/register button click
            // For now, open a dummy login screen
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Retrieve the list of strings from SharedPreferences
        val sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        val rentalPropertyStringList: String? = sharedPreferences.getString("FILTERED_LIST", "")

        // Check if the string is not empty before attempting to deserialize
        val filteredList: List<Rentals> = if (!rentalPropertyStringList.isNullOrEmpty()) {
            Gson().fromJson(
                rentalPropertyStringList,
                object : TypeToken<List<Rentals>>() {}.type
            )
        } else {
            emptyList()
        }

        // Retrieve the list of strings from the intent
        //val rentalPropertyStringList: Array<String>? = intent.getStringArrayExtra("FILTERED_LIST")

        // Convert the list of strings back to a list of Rentals objects
        //val filteredList: List<Rentals> = rentalPropertyStringList?.mapNotNull {
            // Convert each string back to a Rentals object
            //Rentals.fromJson(it)
       // } ?: emptyList()

        // Initialize resultList with filteredList or an empty list
        resultList = filteredList.toMutableList()

        // Setup RecyclerView
        setupRecyclerView()

        // Click listener to show more details when a row is clicked
        binding.rv.addOnItemClickListener { position, _ ->
            // Handle item click, e.g., show more details
            showDetails(resultList[position])
        }
    }


    // Add the following method to show PopupMenu
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.options_menu_items)

        // Set up click listener for each menu item
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.mi_shortlisted -> {
                    // Handle Shortlisted Listings click
                    // For now, open a dummy shortlisted screen
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.mi_addListings -> {
                    // Handle Post Listings click
                    // For now, open a dummy post listings screen
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                else -> false
            }
        }

        // Show the PopupMenu
        popupMenu.show()
    }

    private fun setupRecyclerView() {
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = RentalsAdapter(resultList) { position ->
            // Handle item click here, if needed
            showDetails(resultList[position])
        }
    }

    private fun RecyclerView.addOnItemClickListener(onClickListener: (position: Int, view: View) -> Unit) {
        this.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewAttachedToWindow(view: View) {
                view.setOnClickListener {
                    val holder = getChildViewHolder(view)
                    onClickListener.invoke(holder.adapterPosition, view)
                }
            }

            override fun onChildViewDetachedFromWindow(view: View) {
                // Implement if needed
            }
        })
    }

    private fun showDetails(property: Rentals) {
        // Initialize
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_rental_details, null)
        dialogBuilder.setView(dialogView)

        // Set data to image, property name, and description
        val ivPropertyImage: ImageView = dialogView.findViewById(R.id.ivPropertyImage)
        val tvPropertyName: TextView = dialogView.findViewById(R.id.tvPropertyName)
        val tvDescription: TextView = dialogView.findViewById(R.id.tvDescription)

        // Load the image using resource ID
        val resources: Resources = ivPropertyImage.context.resources
        val resourceId: Int = resources.getIdentifier(property.imageURL, "drawable", ivPropertyImage.context.packageName)
        ivPropertyImage.setImageResource(resourceId)

        tvPropertyName.text = property.propertyName
        tvDescription.text = property.description

        // Set data to specifications
        val tvBedrooms: TextView = dialogView.findViewById(R.id.tvBedrooms)
        val tvBathrooms: TextView = dialogView.findViewById(R.id.tvBathrooms)
        val tvParkingLots: TextView = dialogView.findViewById(R.id.tvParkingLots)

        val tvBedroomsText = property.specifications.bedrooms.toString() + " Beds"
        val tvBathroomsText = property.specifications.bathrooms.toString() + " Baths"
        val tvParkingLotsText = property.specifications.parkingLots.toString() + " Parking"

        tvBedrooms.text = tvBedroomsText
        tvBathrooms.text = tvBathroomsText
        tvParkingLots.text = tvParkingLotsText

        // Set owner information
        val tvOwnerInfo: TextView = dialogView.findViewById(R.id.tvOwnerInfo)
        tvOwnerInfo.text = "Owner: ${property.ownerName}\nContact: ${property.ownerContactDetails.email}, ${property.ownerContactDetails.phoneNumber}"

        // Set shortlist click listener
        val ivShortlistDialog: ImageView = dialogView.findViewById(R.id.ivShortlistDialog)
        ivShortlistDialog.setOnClickListener {
            // Handle shortlist logic here
            // You can add the property to the shortlist
            // and update the UI accordingly
            Toast.makeText(
                this,
                "${property.propertyName} Added to Shortlist}",
                Toast.LENGTH_SHORT
            ).show()
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()


    }
    }
