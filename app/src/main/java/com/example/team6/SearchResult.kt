package com.example.team6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.team6.databinding.ActivitySearchResultBinding
import com.example.team6.models.Rentals
import android.content.SharedPreferences


class SearchResult : AppCompatActivity() {
    private lateinit var binding: ActivitySearchResultBinding
    private lateinit var resultList: MutableList<Rentals>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the list of strings from the intent
        val rentalPropertyStringList: ArrayList<String>? = intent.getStringArrayListExtra("FILTERED_LIST")

        // Convert the list of strings back to a list of RentalProperty objects
        val filteredList: List<Rentals> = rentalPropertyStringList?.mapNotNull {
            // Convert each string back to a RentalProperty object
            Rentals.fromBundleList(it)
        } ?: emptyList()

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
        // Implement logic to show more details for the selected property
        // For example, you can start a new activity or show a dialog with property details
    }
}
