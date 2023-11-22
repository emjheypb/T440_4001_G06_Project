package com.example.team6.landlord

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.get
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.team6.R
import com.example.team6.databinding.ActivityMyListingBinding
import com.example.team6.enums.ExtrasRef
import com.example.team6.enums.SharedPrefRef
import com.example.team6.models.Landlord
import com.example.team6.models.Rentals
import com.example.team6.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyListingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyListingBinding

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private val gson = Gson()

    private lateinit var adapter: PropertyAdapter
    private val myListings = mutableListOf<Rentals>()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val intent : Intent = result.data!!
                val updatedObject = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra("TODO", Rentals :: class.java)
                } else {
                    intent.getSerializableExtra("TODO") as Rentals
                }
                val pos = intent.getIntExtra("TODO_POS", -1)
                myListings.set(pos, updatedObject!!)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.binding = ActivityMyListingBinding.inflate(layoutInflater)
        setContentView(this.binding.root)

        val toolbar = this.binding.menuToolbar
        toolbar.setTitle("My Listing")
        toolbar.setTitleTextColor(getColor(R.color.white))
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences(SharedPrefRef.SHARED_PREF_NAME.value, MODE_PRIVATE)
        prefEditor = sharedPreferences.edit()

        val user = gson.fromJson(
            this.sharedPreferences.getString(SharedPrefRef.CURRENT_USER.value, ""),
            User::class.java
        )
        val propertiesDS = this.sharedPreferences.getString(SharedPrefRef.PROPERTIES_LIST.value, "")
        val typeToken = object : TypeToken<List<Rentals>>() {}.type
        myListings.clear()
        if (propertiesDS != "") {
            for (property in gson.fromJson<List<Rentals>>(propertiesDS, typeToken)
                .toMutableList()) {
                if (property.owner.email == user.email) myListings.add(property)
            }
        }

        adapter = PropertyAdapter(
            myListings,
            { pos -> rowClicked(pos) }
        )

        this.binding.rvProperties.adapter = adapter
        binding.rvProperties.layoutManager = LinearLayoutManager(this)
        binding.rvProperties.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
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
            if (item.itemId == R.id.mi_ll_logout) {
                this.prefEditor.remove(SharedPrefRef.CURRENT_USER.value)
                this.prefEditor.apply()
            }

            startActivity(Intent(this@MyListingActivity, Landlord().redirect(item.itemId)))
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun rowClicked(position: Int) {
        val intent = Intent(this@MyListingActivity, PropertyDetailsActivity::class.java)
        intent.putExtra(ExtrasRef.CURR_PROPERTY.description, myListings[position])
        intent.putExtra(ExtrasRef.ROW.description, position)
        startForResult.launch(intent)
    }
}