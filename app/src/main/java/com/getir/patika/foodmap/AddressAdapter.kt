package com.getir.patika.foodmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

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
    }

    class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addressLayout = view.findViewById<RelativeLayout>(R.id.layout_address)
        val addressTv = view.findViewById<TextView>(R.id.tvAddress)
    }
}
