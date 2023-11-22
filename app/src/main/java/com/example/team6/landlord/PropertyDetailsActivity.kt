package com.example.team6.landlord

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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PropertyDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPropertyDetailsBinding

    private lateinit var property: Rentals
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityPropertyDetailsBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        val toolbar = this.binding.menuToolbar

        if (intent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                property = intent.getSerializableExtra(ExtrasRef.CURR_PROPERTY.toString(), Rentals::class.java)!!
            } else {
                property = intent.getSerializableExtra(ExtrasRef.CURR_PROPERTY.toString()) as Rentals
            }
            position = intent.getIntExtra(ExtrasRef.ROW.toString(), 0)

            val propertyTypes = mutableListOf<String>()
            for(type in PropertyType.entries) {
                propertyTypes.add(type.description)
            }

            toolbar.setTitle(property.address)

            this.binding.etAddress.setText(property.address)
            this.binding.etCity.setText(property.city)
            this.binding.etPostalCode.setText(property.postalCode)
            this.binding.etDescription.setText(property.description)

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
            val address = this.binding.etAddress.text.toString()
            val city = this.binding.etCity.text.toString()
            val postCode = this.binding.etPostalCode.text.toString()
            val type = PropertyType.valueOf(this.binding.spinnerType.selectedItem.toString())
            val description = this.binding.etDescription.text.toString()
            val isAvailable = this.binding.swIsRented.isChecked
            val propertyName = "$address, $city, $postCode"
            val specifications = PropertySpecifications(1, 1, 1)

            startActivity(Intent(this@PropertyDetailsActivity, Landlord().redirect(item.itemId)))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}