package com.example.food_ordering_app.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.FoodInformationActivity
import com.example.food_ordering_app.MenuBottomSheetFragment
import com.example.food_ordering_app.R
import com.example.food_ordering_app.ModelClass.MenuItemModel


class MenuAdapter(
    val context: Context,
    private val menuArrayList: List<MenuItemModel>
)
    : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    class MenuViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val menuFoodName = itemView.findViewById<TextView>(R.id.menuFoodName)
        val menuFoodPrice = itemView.findViewById<TextView>(R.id.menuFoodPrice)
        val menuAddToCart = itemView.findViewById<TextView>(R.id.menuAddToCart)
        val menuFoodImage = itemView.findViewById<ImageView>(R.id.menuFoodImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.menu_item,parent,false)
        return MenuViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {

        val currentMenuItem = menuArrayList[position]
        holder.menuFoodName.text = currentMenuItem.foodName
        holder.menuFoodPrice.text = currentMenuItem.foodPrice.toString()

        val Uri = Uri.parse(currentMenuItem.foodImage)
        Glide.with(context).load(Uri).into(holder.menuFoodImage)
        // Set click listener for the item
        holder.itemView.setOnClickListener {
            // Notify the listener about the item click
            openDetailActivity(position)
        }
    }


    private fun openDetailActivity(position: Int) {
        val currentItem = menuArrayList[position]
        val intent = Intent(context, FoodInformationActivity::class.java).apply {
            putExtra("menuFoodName", currentItem.foodName)
            putExtra("menuFoodImage", currentItem.foodImage) // Corrected
            putExtra("menuFoodDescription", currentItem.shortDescription)
            putExtra("menuFoodIngredients", currentItem.ingredient)
            putExtra("menuFoodPrice", currentItem.foodPrice) // Corrected
        }
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return menuArrayList.size
    }
}


