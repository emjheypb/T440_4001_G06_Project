package com.example.team6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class SearchResultAdapter (
    private val rentalProperties: List<RentalProperty>) : RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val tvPropertyName: TextView = itemView.findViewById(R.id.tvPropertyName)
        val ivShortlist: ImageView = itemView.findViewById(R.id.ivShortlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout_basic_search, parent, false)
        return SearchResultViewHolder(view)
    }

    override fun getItemCount(): Int = rentalProperties.size

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        // Bind property details to respective TextViews
        val property = rentalProperties[position]
        holder.tvPropertyName.text = property.propertyName

        // Set the visibility of the star icon based on the user's login status
        //    if (userLoggedIn) {
        //        holder.ivShortlist.visibility = View.VISIBLE
                // Handle click events for shortlisting
        //        holder.ivShortlist.setOnClickListener {
                    // Implement your logic for adding to the shortlist
                    // e.g., addPropertyToShortlist(property)
        //       }
        //    } else {
        //        holder.ivShortlist.visibility = View.GONE
        //    }
    }

}
