package com.example.food_ordering_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food_ordering_app.ModelClass.CartItems
import com.example.food_ordering_app.ModelClass.OrderDetails
import com.example.food_ordering_app.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var name: String
    private lateinit var address: String
    private lateinit var phone: String
    private lateinit var email: String
    private lateinit var foodNameList: ArrayList<String>
    private lateinit var foodPriceList: ArrayList<String>
    private lateinit var foodImageList: ArrayList<String>
    private lateinit var foodDescriptionList: ArrayList<String>
    private lateinit var foodIngredientList: ArrayList<String>
    private lateinit var totalAmount : String
    private lateinit var cartItemQuantity: ArrayList<Int>
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var binding: ActivityPayOutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        userId = auth.currentUser?.uid ?: ""

        // Get user details from Firebase
        setUserData()


        //Get User details from Firebase
        val intent = intent
        foodNameList = intent.getStringArrayListExtra("foodName") as ArrayList<String>
        foodPriceList = intent.getStringArrayListExtra("foodPrice") as ArrayList<String>
        foodImageList = intent.getStringArrayListExtra("foodImage") as ArrayList<String>
        foodDescriptionList = intent.getStringArrayListExtra("shortDescription") as ArrayList<String>
        foodIngredientList = intent.getStringArrayListExtra("ingredients") as ArrayList<String>
        cartItemQuantity = intent.getIntegerArrayListExtra("foodQunatities") as ArrayList<Int>
        totalAmount = CalculateTotalAmount().toString() + "$"
        binding.payActivityUserTotalAmount.setText(totalAmount)

        binding.backButton.setOnClickListener{
            finish()
        }
        binding.placeOrderButton.setOnClickListener() {

            name = binding.payActivityUserName.text.toString().trim()
            address = binding.payActivityUserAddress.text.toString().trim()
            phone = binding.payActivityUserPhone.text.toString().trim()

            if(name.isBlank() || address.isBlank() || phone.isBlank()){
                Toast.makeText(this,"Please fill all details",Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }
        }
    }

    private fun placeOrder() {
        val time = System.currentTimeMillis()
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId, name, foodNameList, foodImageList, foodPriceList, cartItemQuantity,
            address, totalAmount, phone, time, itemPushKey, false, false
        )
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)

        orderReference.setValue(orderDetails).addOnSuccessListener {
            val bottomsheetDialog = CongratsBottomSheetFragment()
            bottomsheetDialog.show(supportFragmentManager, "test")
            removeItemFromcart()
            addOrderToHistory(orderDetails)
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails).addOnSuccessListener {

            }.addOnFailureListener{
                Toast.makeText(this,"Failed to Order",Toast.LENGTH_SHORT).show()
            }

    }

    private fun removeItemFromcart() {
        val cartItemReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemReference.removeValue()
    }

    private fun CalculateTotalAmount(): Int? {
     var totalAmount = 0
        for (i in 0 until foodPriceList.size){
           var price = foodPriceList[i]
            val  lastChar = price.last()
           val priceListValue = if(lastChar == '$'){
               price.dropLast(1).toInt()
           }
            else{
                price.toInt()
           }
            val quantity = cartItemQuantity[i]

            totalAmount += priceListValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
            if (userId.isNotEmpty()) {
                val userReference: DatabaseReference = databaseReference.child("user").child(userId)
                userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val userName = snapshot.child("name").getValue(String::class.java)
                            val userAddress = snapshot.child("address").getValue(String::class.java)
                            val userPhone = snapshot.child("phone").getValue(String::class.java)

                            // Set user data to the corresponding views
                            binding.payActivityUserName.setText(userName)
                            binding.payActivityUserAddress.setText(userAddress)
                            binding.payActivityUserPhone.setText(userPhone)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error properly (e.g., show a toast or log)
                    }
                })
            }


    }
}


