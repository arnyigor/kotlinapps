package com.arny.kotlinapps.presentation.shoplist

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arny.kotlinapps.R

class ShopListItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val tvName: TextView = view.findViewById(R.id.tvItemName)
    val tvCount: TextView = view.findViewById(R.id.tvItemCount)
}