package com.example.food_ordering_app.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.ModelClass.ListItemDetails
import com.example.food_ordering_app.ModelClass.RecentBuyItemModel
import com.example.food_ordering_app.R
import com.example.food_ordering_app.RecentOrderItemsActivity

class RecentBuyItemAdapter(var context : RecentOrderItemsActivity, var RecentBuyItemList : ArrayList<ListItemDetails>)
    : RecyclerView.Adapter<RecentBuyItemAdapter.RecentBuyViewHolder>() {
    class RecentBuyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

         val recentBuyItemImage = itemView.findViewById<ImageView>(R.id.foodImage)
         val recentBuyItemName = itemView.findViewById<TextView>(R.id.foodName)
         val recentBuyItemPrice = itemView.findViewById<TextView>(R.id.foodPrice)
         val recentBuyItemQuantity = itemView.findViewById<TextView>(R.id.Quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentBuyViewHolder {
      val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.recent_buy_item,parent,false)
        return RecentBuyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
      return RecentBuyItemList.size
    }

    override fun onBindViewHolder(holder: RecentBuyViewHolder, position: Int) {
        val currentItem = RecentBuyItemList[position]

        holder.recentBuyItemName.text = currentItem.foodName
        holder.recentBuyItemPrice.text = currentItem.foodPrice
        holder.recentBuyItemQuantity.text = currentItem.foodQuantity.toString()
        val uriString = currentItem.foodImage
        val uri = Uri.parse(uriString)
        Glide.with(context).load(uri).into(holder.recentBuyItemImage)
    }
}

