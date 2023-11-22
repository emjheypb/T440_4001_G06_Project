package com.example.team6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.team6.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.example.team6.models.ContactDetails
import com.example.team6.models.PropertySpecifications
import com.example.team6.models.Rentals
import com.example.team6.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    val gson = Gson()

    private val sampleLandlord = User("Meidian Condo", "meridian@rent.com", "mer1d1an", "Landlord")

    private var datasource: MutableList<Rentals> = mutableListOf<Rentals>(
        Rentals(
            propertyType = "Condo",
            ownerID = "meridian@rent.com",
            ownerName = "Meridian Condo",
            ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
            imageURL = "@drawable/rentals1",
            propertyName = "10 Northtown Way, North York, M2N7L4",
            specifications = PropertySpecifications(3, 1, 1),
            description = "The apartments offer large one and two-bedroom suites for rent with sizes ranging from 650 sq. ft. to 850 sq. ft. which is ample space for your living needs.  The apartments feature dishwashers and in suite laundry making it ideal for saving you time. There is vinyl and tile flooring throughout the unit.",
            address = "10 Northtown Way",
            postalCode = "M2N7L4",
            city = "North York",
            isAvailableForRent = true
        ),
        Rentals(
            propertyType = "House",
            ownerID = "meridian@rent.com",
            ownerName = "Meridian Condo",
            ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
            propertyName = "15 Northtown Way, North York, M2N7L4",
            imageURL = "@drawable/rentals2",
            specifications = PropertySpecifications(3, 2, 2),
            description = "A beautiful house for rent",
            address = "15 Northtown Way",
            postalCode = "M2N7L4",
            city = "North York",
            isAvailableForRent = true
        ),
        Rentals(
            propertyType = "Apartment",
            ownerID = "meridian@rent.com",
            ownerName = "Meridian Condo",
            ownerContactDetails = ContactDetails("owner1@example.com", "123-456-7890"),
            propertyName = "25 Northtown Way, North York, M2N7L4",
            imageURL = "@drawable/rentals3",
            specifications = PropertySpecifications(3, 3, 0),
            description = "Located close to downtown and the stunning waterfront Cunard Apartments (Lowrise) are located in a highly desirable area in Toronto, North York. The apartments are perfectly catered to a wide range of residents from working professionals to families to everyone in between.",
            address = "25 Northtown Way",
            postalCode = "M2N7L4",
            city = "North York",
            isAvailableForRent = true
        ),
    )
    private var registeredUsers: MutableList<User> = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        sharedPreferences = getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        refreshUsers()

        // Search Handler with Button
        binding.search.setOnClickListener {
            // If you want to perform search when the button is clicked
            val searchText = binding.searchBarEditText.text.toString().trim()
            performSearch(searchText)
        }

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
    }

    private fun performSearch(query: String) {
        val filteredList = datasource.filter {
            it.searchAble(query)
        }

        if (filteredList.isEmpty()) {
            Log.d("SearchResult-Sent", "No search results found.")
        }

        // Clear previous search results from SharedPreferences
        prefEditor.remove("FILTERED_LIST")
        prefEditor.apply()

        // Convert the list of Rentals objects to a list of JSON strings
        val rentalPropertyStringList = Gson().toJson(filteredList)

        // Open SearchResultActivity and pass the filtered list
        //intent.putExtra("FILTERED_LIST", rentalPropertyStringList.toTypedArray())

        // Save filtered list to SharedPreferences
        prefEditor.putString("FILTERED_LIST", rentalPropertyStringList)
        prefEditor.apply()

        // Open SearchResultActivity
        val intent = Intent(this, SearchResult::class.java)
        startActivity(intent)

        Log.d("SearchLogic", "Filtered List Size: ${filteredList.size}")
        Log.d("SearchLogic", "Sample Data: ${filteredList.take(3)}")
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

    private fun refreshUsers() {
        var usersDS = sharedPreferences.getString("USER_LIST", "")
        val typeToken = object : TypeToken<List<User>>() {}.type
        registeredUsers.clear()
        if (usersDS != "") registeredUsers.addAll(
            gson.fromJson<List<User>>(usersDS, typeToken).toMutableList()
        )
        var userExists = false
        for (user in registeredUsers) {
            if (user.email == sampleLandlord.email) {
                userExists = true
                break
            }
        }

        if(!userExists) {
            registeredUsers.add(sampleLandlord)
            val usersJSON = gson.toJson(registeredUsers)
            prefEditor.putString("USER_LIST", usersJSON)
            prefEditor.apply()
        }
    }
}
