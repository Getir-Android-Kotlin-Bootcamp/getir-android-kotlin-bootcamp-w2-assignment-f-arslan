package com.getir.patika.foodmap.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.getir.patika.foodmap.R
import com.google.android.material.divider.MaterialDivider

class AddressAdapter(
    private val addresses: List<AutoCompleteResult>,
    private val onAddressClick: (AutoCompleteResult) -> Unit
) :
    RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val holder = AddressViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
        )
        holder.addressLayout.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos >= 0) {
                onAddressClick(addresses[pos])
            }
        }

        return holder
    }

    override fun getItemCount(): Int = addresses.size

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        holder.addressTv.text = addresses[position].fullText

        val isLastItem = position == addresses.size - 1
        holder.divider.visibility = if (isLastItem) View.GONE else View.VISIBLE
        holder.viewPlaceholder.visibility = if (isLastItem) View.VISIBLE else View.GONE
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addressLayout: RelativeLayout = view.findViewById(R.id.layout_address)
        val addressTv: TextView = view.findViewById(R.id.tvAddress)
        val divider: MaterialDivider = view.findViewById(R.id.address_divider)
        val viewPlaceholder: View = view.findViewById(R.id.view_placeholder)
    }
}
