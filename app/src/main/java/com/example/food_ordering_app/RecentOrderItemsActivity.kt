package com.example.food_ordering_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_ordering_app.Adapter.ListAdapter
import com.example.food_ordering_app.Adapter.RecentBuyItemAdapter
import com.example.food_ordering_app.ModelClass.ListItemDetails
import com.example.food_ordering_app.ModelClass.OrderDetails
import com.example.food_ordering_app.databinding.ActivityRecentOrderItemsBinding

class RecentOrderItemsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecentOrderItemsBinding
    private lateinit var foodNames: ArrayList<String>
    private lateinit var foodPrices: ArrayList<String>
    private lateinit var foodImages: ArrayList<String>
    private lateinit var foodQuantities: ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecentOrderItemsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }

        // Retrieve order details from intent
        val recentBuyItem = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentBuyItem?.let { orderDetails ->
            if (orderDetails.isNotEmpty()) {
                val recentOrderItem = orderDetails[0]

                foodNames = recentOrderItem.foodNames as ArrayList<String>
                foodPrices = recentOrderItem.foodPrices as ArrayList<String>
                foodImages = recentOrderItem.foodImages as ArrayList<String>
                foodQuantities = recentOrderItem.foodQuantities as ArrayList<Int>
            }
        }
        val listItems = ArrayList<ListItemDetails>().apply {
            for (i in foodNames.indices) {
                add(
                    ListItemDetails(
                        foodNames[i],
                        foodPrices[i],
                        foodImages[i],
                        foodQuantities[i]
                    )
                )
            }
        }

        // Set up RecyclerView
        setAdapter(listItems)
    }

    private fun setAdapter(listItems: ArrayList<ListItemDetails>) {
        binding.recentBuyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.recentBuyRecyclerView.adapter = RecentBuyItemAdapter(this, listItems)
    }
}
