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
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.team6.enums.MembershipType
import com.example.team6.enums.SharedPrefRef
import com.example.team6.models.Landlord
import com.example.team6.models.User
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso


class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var resultList: MutableList<Rentals>
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor

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
        this.sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        val rentalPropertyStringList: String? = sharedPreferences.getString("FILTERED_LIST", "")
        this.prefEditor = sharedPreferences.edit()

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
        val user = Gson().fromJson(this.sharedPreferences.getString(SharedPrefRef.CURRENT_USER.value, ""), User::class.java)
        if (user != null && user.membership == MembershipType.LANDLORD) {
            popupMenu.inflate(R.menu.menu_landlord_options)
        } else {
            popupMenu.inflate(R.menu.options_menu_items)
        }

        // Set up click listener for each menu item
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.mi_shortlisted -> {
                    // Handle Shortlisted Listings click
                    val isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false)

                    // Check for null before accessing user membership
                    if (isLoggedIn) {
                        startActivity(Intent(this, ShortListedActivity::class.java))
                    }
                    else {
                        Toast.makeText(
                            this,
                            "LOGIN TO SHORTLIST!",
                            Toast.LENGTH_SHORT
                        ).show()
                        Handler().postDelayed(Runnable {
                            startActivity(Intent(this, LoginActivity::class.java))
                        }, 1000)
                    }
                    true
                }

                R.id.mi_addListings -> {
                    // Handle Post Listings click
                    Toast.makeText(
                        this,
                        "LOGIN AS LANDLORD TO POST!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Handler().postDelayed(Runnable {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }, 1000)
                    true
                }
                R.id.mi_logout -> {
                    this.prefEditor.remove(SharedPrefRef.CURRENT_USER.value)
                    this.prefEditor.putBoolean("IS_LOGGED_IN", false)
                    this.prefEditor.apply()

                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                in Landlord().menuItems -> {
                    if(item.itemId == R.id.mi_ll_logout) {
                        this.prefEditor.remove(SharedPrefRef.CURRENT_USER.value)
                        this.prefEditor.putBoolean("IS_LOGGED_IN", false)
                        this.prefEditor.apply()
                    }
                    startActivity(Intent(this, Landlord().redirect(item.itemId)))
                    finish()
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
        tvOwnerInfo.text = "Owner: ${property.owner.name}\nContact: ${property.owner.email}, ${property.owner.phoneNumber}"

        val isLoggedIn: Boolean = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        Log.d("LOGGED IN?", "$isLoggedIn")

        if(isLoggedIn){
            val btnContact : Button = dialogView.findViewById(R.id.btn_contact)
            btnContact.isVisible=true
            btnContact.setOnClickListener {
                Log.d("EMAIL", "Attempting to send mail")
                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:") //look for only applications that can send emails
                    putExtra(Intent.EXTRA_EMAIL, "${property.owner.email}")
                    putExtra(Intent.EXTRA_SUBJECT, "Rental Inquiry")
                    putExtra(Intent.EXTRA_TEXT, "Hi, I am interested in this property!")
                }

                if (emailIntent.resolveActivity(packageManager) != null){
                    startActivity(emailIntent)
                }

            }
        }

        // Set shortlist click listener
        val ivShortlistDialog: ImageView = dialogView.findViewById(R.id.ivShortlistDialog)
        ivShortlistDialog.setOnClickListener {
            //validate if user is logged in, if yes then update the sharedpref to save the property in favorites


            if(isLoggedIn){
                val gson = Gson()
                val loggedInUserFromSP = sharedPreferences.getString("LOGGED_IN_USER", "")
                val loggedInUser : User = gson.fromJson(loggedInUserFromSP, User::class.java)

                val userListFromSP = sharedPreferences.getString("USER_LIST", "")
                val typeToken = object : TypeToken<MutableList<User>>(){}.type
                val userList = gson.fromJson<MutableList<User>>(userListFromSP, typeToken)

                for(u in userList){
                    if(u.email==loggedInUser.email) {
                        // Initialize rentalFavs if it's null
                        if(u.rentalFavs == null) {
                            u.rentalFavs = mutableListOf()
                        }
                        u.rentalFavs.add(property)
                        loggedInUser.rentalFavs.add(property)
                        // Retrieve the last item added to rentalFavs
                        val lastItem = u.rentalFavs.lastOrNull()

                        // Log the property name of the last item
                        lastItem?.let {
                            Log.d("YourTag", "Last property added to rentalFavs for user ${u.email}: ${it.propertyName}")
                        }

                        break
                    }
                }
                val loggedInUserUpdated = gson.toJson(loggedInUser)
                this.prefEditor.putString("LOGGED_IN_USER", loggedInUserUpdated)
                val userListUpdated = gson.toJson(userList)
                this.prefEditor.putString("USER_LIST", userListUpdated)
                this.prefEditor.apply()
                Toast.makeText(
                    this,
                    "${property.propertyName} Added to Favorites!",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else{
                Toast.makeText(
                    this,
                    "LOGIN TO SHORTLIST!",
                    Toast.LENGTH_SHORT
                ).show()
                Handler().postDelayed(Runnable {
                    startActivity(Intent(this, LoginActivity::class.java))
                }, 1000)
            }
        }

        val alertDialog = dialogBuilder.create()
        alertDialog.show()


    }
    }
