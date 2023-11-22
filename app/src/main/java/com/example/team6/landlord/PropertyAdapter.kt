package com.example.team6.landlord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.team6.R
import com.example.team6.models.Rentals

class PropertyAdapter(
    private val properties: MutableList<Rentals>,
    private val rowClickHandler: (Int) -> Unit
) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {
    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder (itemView) {
        init {
            itemView.setOnClickListener {
                rowClickHandler(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout_ll_my_listing, parent, false)
        return PropertyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val curr = properties[position]
        val context = holder.itemView.context

        val tvTitle = holder.itemView.findViewById<TextView>(R.id.tv_ll_property_title)
        val tvDetail = holder.itemView.findViewById<TextView>(R.id.tv_ll_property_description)
        val iv = holder.itemView.findViewById<ImageView>(R.id.iv_ll_property)

        tvTitle.text = curr.propertyName
        tvDetail.text = curr.description
        iv.setImageResource(context.resources.getIdentifier(curr.imageURL, "drawable", context.packageName))
    }
}