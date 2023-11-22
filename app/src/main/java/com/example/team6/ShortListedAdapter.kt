package com.example.team6

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team6.models.Rentals

class ShortlistedAdapter(
    var rentals: MutableList<Rentals>,
    private val itemClickListener: (position: Int) -> Unit
) : RecyclerView.Adapter<ShortlistedAdapter.ShortlistedViewHolder>() {

    inner class ShortlistedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPropertyName: TextView = itemView.findViewById(R.id.tvPropertyName)
        val tvPropertyImage: ImageView = itemView.findViewById(R.id.tvPropertyImage)
        val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
        val deleteButton: Button = itemView.findViewById(R.id.del_rentalFavs)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortlistedViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_shortlisted, parent, false)
        return ShortlistedViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rentals.size
    }

    override fun onBindViewHolder(holder: ShortlistedViewHolder, position: Int) {
        // Bind property details to respective TextViews
        val property = rentals[position]
        holder.tvPropertyName.text = property.propertyName
        holder.tvDetail.text = "${property.propertyType} ${property.specifications.bedrooms}B${property.specifications.bathrooms}B${property.specifications.parkingLots}P"

        // Load the image
        val context = holder.itemView.context
        val res = context.resources.getIdentifier(property.imageURL, "drawable", context.packageName)
        val tvPropertyImage = holder.itemView.findViewById<ImageView>(R.id.tvPropertyImage)
        tvPropertyImage.setImageResource(res)

        // Set click listener for item click
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }
}
