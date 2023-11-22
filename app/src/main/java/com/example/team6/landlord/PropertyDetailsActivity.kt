package com.example.team6.landlord

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import com.example.team6.R
import android.content.Intent
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.example.team6.databinding.ActivityPropertyDetailsBinding
import com.example.team6.enums.ExtrasRef
import com.example.team6.enums.PropertyType
import com.example.team6.enums.SharedPrefRef
import com.example.team6.models.Landlord
import com.example.team6.models.PropertySpecifications
import com.example.team6.models.Rentals
import com.example.team6.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PropertyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyDetailsBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private val gson = Gson()

    private lateinit var property: Rentals
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityPropertyDetailsBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        sharedPreferences = getSharedPreferences(SharedPrefRef.SHARED_PREF_NAME.value, MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        val toolbar = this.binding.menuToolbar

        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                property = intent.getSerializableExtra(ExtrasRef.CURR_PROPERTY.toString(), Rentals::class.java)!!
            } else {
                property = intent.getSerializableExtra(ExtrasRef.CURR_PROPERTY.toString()) as Rentals
            }
            position = intent.getIntExtra(ExtrasRef.ROW.toString(), -1)

            val propertyTypes = mutableListOf<String>()
            for(type in PropertyType.entries) {
                propertyTypes.add(type.description)
            }

            toolbar.setTitle(property.address)

            this.binding.etAddress.setText(property.address)
            this.binding.etCity.setText(property.city)
            this.binding.etPostalCode.setText(property.postalCode)
            this.binding.etDescription.setText(property.description)
            this.binding.etBedrooms.setText(property.specifications.bedrooms.toString())
            this.binding.etBathrooms.setText(property.specifications.bathrooms.toString())
            this.binding.etParking.setText(property.specifications.parkingLots.toString())

            this.binding.spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, propertyTypes)
            this.binding.spinnerType.setSelection(propertyTypes.indexOf(property.propertyType.description))

            this.binding.swIsRented.isChecked = property.isAvailableForRent
        } else {
            finish()
        }

        toolbar.setTitleTextColor(getColor(com.example.team6.R.color.white))
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_save_option,
            menu
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mi_save) {
            var isError = false

            val address = this.binding.etAddress.text.toString()
            if(address == "") {
                isError = true
                this.binding.etAddress.error = "Required Field"
            }
            val city = this.binding.etCity.text.toString()
            if(city == "") {
                isError = true
                this.binding.etCity.error = "Required Field"
            }
            val postCode = this.binding.etPostalCode.text.toString()
            if(postCode == "") {
                isError = true
                this.binding.etPostalCode.error = "Required Field"
            }
            val type = PropertyType.valueOf(this.binding.spinnerType.selectedItem.toString().uppercase())
            val description = this.binding.etDescription.text.toString()
            val isAvailable = this.binding.swIsRented.isChecked
            val propertyName = "$address, $city, $postCode"
            val bedrooms = this.binding.etBedrooms.text.toString().toIntOrNull() ?: 0
            val bathrooms = this.binding.etBathrooms.text.toString().toIntOrNull() ?: 0
            val parking = this.binding.etParking.text.toString().toIntOrNull() ?: 0
            val specifications = PropertySpecifications(bedrooms, bathrooms, parking)
            val currUser = gson.fromJson(this.sharedPreferences.getString(SharedPrefRef.CURRENT_USER.value, null), User::class.java)

            if (isError) return false

            val propertyToAdd = Rentals(type, currUser, propertyName, property.imageURL, specifications, description, address, postCode, city, isAvailable)
            saveProperty(propertyToAdd, position)

            intent.putExtra(ExtrasRef.CURR_PROPERTY.description, propertyToAdd)
            intent.putExtra(ExtrasRef.ROW.description, position)
            setResult(Activity.RESULT_OK, intent)
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveProperty(propertyToAdd: Rentals, pos: Int) {
        val propertiesDS = this.sharedPreferences.getString(SharedPrefRef.PROPERTIES_LIST.value, "")
        var myListings = mutableListOf<Rentals>()
        val typeToken = object : TypeToken<List<Rentals>>() {}.type
        if (propertiesDS != "") myListings.addAll(gson.fromJson<List<Rentals>>(propertiesDS, typeToken).toMutableList())

        if(pos == -1) {
            myListings.add(propertyToAdd)
        } else {
            myListings.removeAt(pos)
            myListings.add(pos, propertyToAdd)
        }

        val myListingJSON = gson.toJson(myListings)
        prefEditor.putString(SharedPrefRef.PROPERTIES_LIST.value, myListingJSON)

        prefEditor.apply()
    }
}