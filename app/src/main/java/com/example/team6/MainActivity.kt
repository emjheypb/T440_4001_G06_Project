package com.example.team6

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.team6.databinding.ActivityMainBinding
import android.content.SharedPreferences
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.team6.enums.MembershipType
import com.example.team6.enums.PropertyType
import com.example.team6.models.ContactDetails
import com.example.team6.models.Landlord
import com.example.team6.models.PropertySpecifications
import com.example.team6.models.Rentals
import com.example.team6.enums.SharedPrefRef
import com.example.team6.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var prefEditor: SharedPreferences.Editor
    private val gson = Gson()

    private val sampleLandlord = User("Meridian Condo", "meridian@rent.com","012-345-6789", "mer1d1an", MembershipType.LANDLORD, mutableListOf())
    private var datasource: MutableList<Rentals> = mutableListOf<Rentals>(
        Rentals(
            propertyType = PropertyType.CONDO,
            owner = sampleLandlord,
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
            propertyType = PropertyType.HOUSE,
            owner = sampleLandlord,
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
            propertyType = PropertyType.APARTMENT,
            owner = sampleLandlord,
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

        sharedPreferences = getSharedPreferences(SharedPrefRef.SHARED_PREF_NAME.value, MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        refreshSamples()

        // Search Handler with Button
        binding.search.setOnClickListener {
            // If you want to perform search when the button is clicked
            val searchText = binding.searchBarEditText.text.toString().trim()
            performSearch(searchText)
        }

        // Set up click listeners for toolbar items
        binding.menuIcon.setOnClickListener {
            // Handle menu icon click
            showPopupMenuOnClick(it)
        }

        binding.teamNameTextView.setOnClickListener {
            // Handle team name click (redirect or refresh to MainActivity)
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Optional: finish the current activity to clear it from the stack
        }

        binding.loginRegisterButton.setOnClickListener {
            // Handle login/register button click
            handleLoginButtoncClick()
        }
    }

    override fun onResume() {
        binding.loginRegisterButton.isVisible = !sharedPreferences.getBoolean(SharedPrefRef.IS_LOGGED_IN.value,false)
        super.onResume()
    }

    // Call this method when need to show PopupMenu
    private fun showPopupMenuOnClick(view: View) {
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        Log.d("PopUp Logged_IN", "$isLoggedIn")
        if (isLoggedIn) {
            showPopupMenu(view)
        } else {
            Toast.makeText(this, "Please log in to access this feature", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun handleLoginButtoncClick() {
        val isLoggedIn: Boolean = sharedPreferences.getBoolean("IS_LOGGED_IN", false)
        Log.d("Login Status for loginRegisterButton", "$isLoggedIn")

        if (isLoggedIn) {
            // Update UI
            binding.loginRegisterButton.visibility = View.GONE

        } else {
            // User is not logged in, open the login screen
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
        prefEditor.putString(SharedPrefRef.FILTERED_LIST.value, rentalPropertyStringList)
        prefEditor.apply()

        // Open SearchResultActivity
        val intent = Intent(this, SearchResult::class.java)
        startActivity(intent)

        Log.d("SearchLogic", "Filtered List Size: ${filteredList.size}")
        Log.d("SearchLogic", "Sample Data: ${filteredList.take(3)}")
    }

    private fun handleLogout() {
        this.prefEditor.remove(SharedPrefRef.CURRENT_USER.value)
        this.prefEditor.putBoolean("IS_LOGGED_IN", false)
        this.prefEditor.apply()

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    // Logic to deal with PopupMenu with regard to login status
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val user = gson.fromJson(this.sharedPreferences.getString(SharedPrefRef.CURRENT_USER.value, ""), User::class.java)
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
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
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
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    }, 1000)
                    true
                }
                R.id.mi_logout -> {
                    // Handle Logout click
                    handleLogout()
                    true
                }
                in Landlord().menuItems -> {
                    if(item.itemId == R.id.mi_ll_logout) {
                        this.prefEditor.remove(SharedPrefRef.CURRENT_USER.value)
                        this.prefEditor.putBoolean("IS_LOGGED_IN", false)
                        this.prefEditor.apply()
                    }
                    startActivity(Intent(this, Landlord().redirect(item.itemId)))
//                    finish()
                    true
                }
                else -> false }}
        // Show the PopupMenu
        popupMenu.show() }

    private fun refreshSamples() {
        val usersDS = sharedPreferences.getString(SharedPrefRef.USERS_LIST.value, "")
        val usersToken = object : TypeToken<List<User>>() {}.type
        registeredUsers.clear()
        if (usersDS != "") registeredUsers.addAll(
            gson.fromJson<List<User>>(usersDS, usersToken).toMutableList()
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
            prefEditor.putString(SharedPrefRef.USERS_LIST.value, usersJSON)
            prefEditor.apply()
        }


        val propertiesDS = sharedPreferences.getString(SharedPrefRef.PROPERTIES_LIST.value, "")
        if (propertiesDS != "") {
            datasource.clear()
            val propertiesToken = object : TypeToken<List<Rentals>>() {}.type
            datasource.addAll(gson.fromJson<List<Rentals>>(propertiesDS, propertiesToken).toMutableList())
        }

        val propertiesJSON = gson.toJson(datasource)
        prefEditor.putString(SharedPrefRef.PROPERTIES_LIST.value, propertiesJSON)
        prefEditor.apply()
    }
}
