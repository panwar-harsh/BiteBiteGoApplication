package com.example.food_ordering_app.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.FoodInformationActivity
import com.example.food_ordering_app.R
import com.example.food_ordering_app.ModelClass.FoodItemDetails
import com.example.food_ordering_app.fragment.HomeFragment

class PopularAdapter(var context : Context, var FoodArrayList : ArrayList<FoodItemDetails>)
      : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

         //To Create new view instance if layout manager fails
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {

        val inflater = LayoutInflater.from( parent.context)
        val itemView = inflater.inflate(R.layout.popular_item,parent,false)
             return PopularViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  FoodArrayList.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {

        val currentFoodItem = FoodArrayList[position]

        holder.foodName.text = currentFoodItem.FoodName
        holder.foodPrice.text = currentFoodItem.FoodPrice
        Glide.with(context).load(currentFoodItem.FoodImage).into(holder.foodImage)


        // Set click listener for the item
        holder.itemView.setOnClickListener {
            // Start FoodInformationActivity (if needed)
            val intent = Intent(context, FoodInformationActivity::class.java)
            intent.putExtra("foodName", currentFoodItem.FoodName)
            intent.putExtra("foodImage", currentFoodItem.FoodImage)
            context.startActivity(intent)
        }

    }
   // It holds view so that views are not created everytime,so memory can be saved
    class PopularViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){

        val foodName = itemView.findViewById<TextView>(R.id.FoodNameid)
       val foodPrice = itemView.findViewById<TextView>(R.id.pricePopularId)
       val foodImage = itemView.findViewById<ImageView>(R.id.foodImagePopular)

    }
}

