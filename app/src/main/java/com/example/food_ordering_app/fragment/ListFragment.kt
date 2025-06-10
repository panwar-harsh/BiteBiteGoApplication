package com.example.food_ordering_app.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food_ordering_app.Adapter.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.ModelClass.ListItemDetails
import com.example.food_ordering_app.ModelClass.OrderDetails
import com.example.food_ordering_app.RecentOrderItemsActivity
import com.example.food_ordering_app.databinding.FragmentListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ListFragment : Fragment() {
    private lateinit var binding : FragmentListBinding
    private lateinit var myRecyclerView : RecyclerView
    private var ListOfOrderedItems : MutableList<OrderDetails> = mutableListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var userId : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the list here (if needed)
        ListOfOrderedItems = arrayListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        //Retrive and display the user order histoty

        binding.RecentBuyItemsCardView.setOnClickListener {
            SeeItemsRecentBuy()
        }
        RetrieveBuyHistory()
        return binding.root
    }

    private fun SeeItemsRecentBuy() {
        ListOfOrderedItems.firstOrNull()?.let { recentBuy->
            val intent = Intent(requireContext(), RecentOrderItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItem", ArrayList(ListOfOrderedItems))
            startActivity(intent)
        }
    }

    private fun RetrieveBuyHistory() {
        binding.RecentBuyItems.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""

        val butItemReference = database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = butItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded || context == null) return

                for(buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let { ListOfOrderedItems.add(it) }
                }
                ListOfOrderedItems.reverse()
                if(ListOfOrderedItems.isNotEmpty()){
                    setDataInRecentBuyItem()
                    setPreviouslyBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load buy history: ${error.message}", Toast.LENGTH_SHORT).show()
            }


        })
    }

    private fun setDataInRecentBuyItem() {
        binding.RecentBuyItems.visibility = View.VISIBLE
        val recentOrderItem = ListOfOrderedItems.firstOrNull()
        if (recentOrderItem != null) {
            binding.listFoodName.text = recentOrderItem.foodNames?.firstOrNull() ?: ""
            binding.listFoodPrice.text = recentOrderItem.foodPrices?.firstOrNull() ?: ""
            val image = recentOrderItem.foodImages?.firstOrNull() ?: ""
            val uri = Uri.parse(image)
            Glide.with(this).load(uri).into(binding.listFoodImage)
        }
    }

    private fun setPreviouslyBuyItemRecyclerView() {
        val listItems = ArrayList<ListItemDetails>().apply {
            for (i in 1 until ListOfOrderedItems.size) {
                val item = ListOfOrderedItems[i]
                add(
                    ListItemDetails(
                        item.foodNames?.firstOrNull() ?: "",
                        item.foodPrices?.firstOrNull() ?: "",
                        item.foodImages?.firstOrNull() ?: "",
                        item.foodQuantities?.firstOrNull() ?: 0
                    )
                )
            }
        }
        // Initialize RecyclerView
         if (!isAdded) return
        myRecyclerView = binding.listRecyclerView
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myRecyclerView.adapter = ListAdapter(requireContext(), listItems)
    }
}

