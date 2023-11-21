package com.example.team6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team6.models.Rentals
import com.squareup.picasso.Picasso



class RentalsAdapter (
    var rentals: MutableList<Rentals>,
    private val itemClickListener: (position: Int) -> Unit,
) : RecyclerView.Adapter<RentalsAdapter.RentalsViewHolder>() {
        inner class RentalsViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
            val tvPropertyName: TextView = itemView.findViewById(R.id.tvPropertyName)
            val tvPropertyImage: ImageView = itemView.findViewById(R.id.tvPropertyImage)
            val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
            val ivShortlist: ImageView = itemView.findViewById(R.id.ivShortlist)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RentalsViewHolder {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_basic_search, parent, false)
            return RentalsViewHolder(view)
        }

        override fun getItemCount(): Int {
            return rentals.size
        }

        //Descirbe what going to put in Textviews
        override fun onBindViewHolder(holder: RentalsViewHolder, position: Int) {
            // Bind property details to respective TextViews
            val property = rentals[position]
            holder.tvPropertyName.text = property.propertyName
            holder.tvDetail.text = "${property.propertyType} ${property.specifications.bedrooms}B${property.specifications.bathrooms}B${property.specifications.parkingLots}P"

            // Load the image using Picasso
            Picasso.get().load(property.imageURL).into(holder.tvPropertyImage)
            holder.ivShortlist.visibility = View.VISIBLE
            // Set click listener
            holder.itemView.setOnClickListener{
                itemClickListener.invoke(position)
            }
        }

    }