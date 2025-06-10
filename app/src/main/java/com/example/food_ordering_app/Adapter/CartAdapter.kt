package com.example.food_ordering_app.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_ordering_app.ModelClass.CartItems
import com.example.food_ordering_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    val context: Context,
    private val cartArrayList: MutableList<CartItems>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    // Firebase authentication instance
    private val auth = FirebaseAuth.getInstance()

    // Initialize Firebase and cart item quantities
    init {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartArrayList.size

        // Initialize CartItemQuantities with default value 1 for each item
        CartItemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object {
        var CartItemQuantities: IntArray = intArrayOf() // Stores quantities of cart items
        private lateinit var cartItemReference: DatabaseReference // Firebase reference for cart items
    }

    // ViewHolder for CartAdapter
    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val FoodName: TextView = itemView.findViewById(R.id.FoodNameCart)
        val FoodPrice: TextView = itemView.findViewById(R.id.FoodPriceCart)
        val FoodImage: ImageView = itemView.findViewById(R.id.FoodImageCart)
        val QuantityCart: TextView = itemView.findViewById(R.id.QuantityCart)
        val MinusButtonCart: ImageButton = itemView.findViewById(R.id.MinusButtonCart)
        val PlusButtonCart: ImageButton = itemView.findViewById(R.id.PlusButtonCart)
        val DeleteButtonCart: ImageButton = itemView.findViewById(R.id.DeleteButtonCart)
    }

    // Inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(itemView)
    }

    // Return the number of items in the cart
    override fun getItemCount(): Int {
        return cartArrayList.size
    }

    // Bind data to the views
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val currentCartItem = cartArrayList[position]

        // Initialize quantity for the current item
        var quantity = CartItemQuantities[position]

        // Set food name, price, and image
        holder.FoodName.text = currentCartItem.foodName
        val uriString = currentCartItem.foodImage
        val uri = Uri.parse(uriString)
        Glide.with(context).load(uri).into(holder.FoodImage)
        holder.FoodPrice.text = currentCartItem.foodPrice.toString()

        // Set initial quantity
        holder.QuantityCart.text = quantity.toString()

        // Handle minus button click
        holder.MinusButtonCart.setOnClickListener {
            decreaseQuantity(position, holder)
        }

        // Handle plus button click
        holder.PlusButtonCart.setOnClickListener {
            increaseQuantity(position, holder)
        }

        // Handle delete button click
        holder.DeleteButtonCart.setOnClickListener {
            deleteItem(position)
        }
    }

    // Decrease the quantity of an item
    private fun decreaseQuantity(position: Int, holder: CartViewHolder) {
        if (CartItemQuantities[position] > 1) {
            CartItemQuantities[position]--
            cartArrayList[position].foodQuantity = CartItemQuantities[position]
            holder.QuantityCart.text = CartItemQuantities[position].toString()
        }
    }

    // Increase the quantity of an item
    private fun increaseQuantity(position: Int, holder: CartViewHolder) {
        CartItemQuantities[position]++
        cartArrayList[position].foodQuantity = CartItemQuantities[position]
        holder.QuantityCart.text = CartItemQuantities[position].toString()
    }

    // Delete an item from the cart
    private fun deleteItem(position: Int) {
        val positionRetrieve = position

        // Get the unique key of the item to delete
        getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
            if (uniqueKey != null) {
                removeItem(position, uniqueKey)
            }
        }
    }

    // Remove an item from Firebase and the local list
    private fun removeItem(position: Int, uniqueKey: String) {
        cartItemReference.child(uniqueKey).removeValue().addOnCompleteListener {
            cartArrayList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartArrayList.size)
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }

    // Get the unique key of an item at a specific position
    private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
        cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var uniqueKey: String? = null
                snapshot.children.forEachIndexed { index, dataSnapshot ->
                    if (index == positionRetrieve) {
                        uniqueKey = dataSnapshot.key
                        return@forEachIndexed
                    }
                }
                onComplete(uniqueKey)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error fetching cart item", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Get the updated quantities of all items in the cart
    fun getUpdatedItemsQuantities(): ArrayList<Int> {
        return ArrayList(CartItemQuantities.toList())
    }
}

