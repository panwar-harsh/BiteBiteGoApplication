package com.example.food_ordering_app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_ordering_app.Adapter.CartAdapter
import com.example.food_ordering_app.ModelClass.CartItems
import com.example.food_ordering_app.PayOutActivity
import com.example.food_ordering_app.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private  var cartArrayList: ArrayList<CartItems> = arrayListOf()
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        RetrieveCartItems()

        binding.proceedButton.setOnClickListener{
            GetOrderItemDetails()
        }

        return binding.root
    }

    private fun GetOrderItemDetails() {
        userId = auth.currentUser?.uid ?: ""
       val orderIdReference : DatabaseReference = database.reference.child("user").child(userId)
           .child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val shortDescription = mutableListOf<String>()
        val ingredients = mutableListOf<String>()
        val foodQunatities = cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(foodSnapshot in snapshot.children) {
                 // Get the CartItems to respective list
                    val orderItem = foodSnapshot.getValue(CartItems::class.java)
                    //Add item details in to array
                    orderItem?.foodName?.let { foodName.add(it) }
                    orderItem?.foodPrice?.let { foodPrice.add(it) }
                    orderItem?.foodImage?.let { foodImage.add(it) }
                    orderItem?.shortDescription?.let { shortDescription.add(it) }
                    orderItem?.ingredients?.let { ingredients.add(it) }
                }
                orderNow(foodName,foodPrice,foodImage,shortDescription,ingredients,foodQunatities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Order making Failed", Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        shortDescription: MutableList<String>,
        ingredients: MutableList<String>,
        foodQunatities:MutableList<Int>
    ) {
        if(isAdded && context!=null){
            val intent = Intent(requireContext(),PayOutActivity :: class.java)
            intent.putExtra("foodName",foodName as ArrayList<String>)
            intent.putExtra("foodPrice",foodPrice as ArrayList<String>)
            intent.putExtra("foodImage",foodImage as ArrayList<String>)
            intent.putExtra("shortDescription",shortDescription as ArrayList<String>)
            intent.putExtra("ingredients",ingredients as ArrayList<String>)
            intent.putExtra("foodQunatities",foodQunatities as ArrayList<Int>)
            startActivity(intent)
        }
    }


    private fun RetrieveCartItems() {
        //Database Reference to the Firebase
         database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid?:""

        val foodReference : DatabaseReference = database.reference.child("user").child(userId).child("CartItems")

        //Fetch Data from database
        foodReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return
                cartArrayList.clear()
                for (foodSnapshot in snapshot.children) {
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)
                    cartItems?.let { cartArrayList.add(it) }
                }

                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data Fetching Failed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setAdapter() {

        if (!isAdded || context == null) return

        myRecyclerView = binding.recyclerViewId
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartAdapter = CartAdapter(requireContext(), cartArrayList)
        myRecyclerView.adapter = cartAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

