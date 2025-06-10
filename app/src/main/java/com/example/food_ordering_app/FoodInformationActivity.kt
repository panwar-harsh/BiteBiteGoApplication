package com.example.food_ordering_app

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.food_ordering_app.ModelClass.CartItems
import com.example.food_ordering_app.databinding.ActivityFoodInformationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FoodInformationActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFoodInformationBinding
    private lateinit var auth : FirebaseAuth

    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodShortDescription: String? = null
    private var foodIngredients: String? = null
    private var foodImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initalize auth
        auth = Firebase.auth
         foodName = intent.getStringExtra("menuFoodName")
         foodPrice = intent.getStringExtra("menuFoodPrice")
         foodShortDescription = intent.getStringExtra("menuFoodDescription")
         foodIngredients = intent.getStringExtra("menuFoodIngredients")
         foodImage = intent.getStringExtra("menuFoodImage")

         with(binding){
             infoFoodName.text = foodName
             shortDescription.text = foodShortDescription
             ingredient.text = foodIngredients
             Glide.with(this@FoodInformationActivity).load(Uri.parse(foodImage)).into(infoFoodImage)
         }
        binding.infoBackButton.setOnClickListener {
            finish()
        }

        binding.infoAddToCart.setOnClickListener{
            AddItemToCart()
        }


    }

    private fun AddItemToCart() {
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        //Create a cartItems model
        val cartItem = CartItems(
            foodName,
            foodPrice,
            foodShortDescription,
            foodIngredients,
            foodImage,
            1
        )
        database.child("user").child(userId).child("CartItems").push().setValue(cartItem)
            .addOnSuccessListener {
                Toast.makeText(this,"Item Added Into Cart Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(this,"Item Not Added ",Toast.LENGTH_SHORT).show()

            }
    }

}

