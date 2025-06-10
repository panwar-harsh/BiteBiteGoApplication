package com.example.food_ordering_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.ModelClass.ListItemDetails
import com.example.food_ordering_app.R
import com.example.food_ordering_app.RecentOrderItemsActivity


class ListAdapter(
    var context: Context,
    private var ListArrayList: ArrayList<ListItemDetails>
)
    : RecyclerView.Adapter<ListAdapter.ListViewHolder>(){
    class ListViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val listFoodName = itemView.findViewById<TextView>(R.id.listFoodName)
        val listFoodPrice = itemView.findViewById<TextView>(R.id.listFoodPrice)
        val listFoodImage = itemView.findViewById<ImageView>(R.id.listFoodImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item,parent,false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListAdapter.ListViewHolder, position: Int) {

        val currentItem = ListArrayList[position]

        holder.listFoodName.text = currentItem.foodName
        holder.listFoodPrice.text = currentItem.foodPrice
        // Load image using Glide

        val uriString = currentItem.foodImage
        val uri = Uri.parse(uriString)
        Glide.with(context).load(uri).into(holder.listFoodImage)

    }

    override fun getItemCount(): Int {
       return ListArrayList.size
    }
}


