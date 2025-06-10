package com.example.food_ordering_app.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.runtime.snapshots.Snapshot
import com.example.food_ordering_app.ModelClass.User
import com.example.food_ordering_app.R
import com.example.food_ordering_app.databinding.ActivityPayOutBinding
import com.example.food_ordering_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var auth = FirebaseAuth.getInstance()
    private var database = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        setUserData()
        binding.saveInfoBtn.setOnClickListener{
            val name = binding.name.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()
            val email = binding.email.text.toString()

            updateUserData(name,address,email,phone)
        }
        binding.apply {
            name.isEnabled = false
            address.isEnabled = false
            phone.isEnabled = false
            email.isEnabled = false
        }
        binding.editProfileBtn.setOnClickListener{
            binding.apply {
                name.isEnabled = !name.isEnabled
                address.isEnabled = !address.isEnabled
                phone.isEnabled = !phone.isEnabled
                email.isEnabled = !email.isEnabled
            }
        }

        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid
        if(userId!=null){
            val userReference = database.getReference("user").child(userId)
            val userData = hashMapOf("name" to name,"address" to address,
                "phone" to phone,"email" to email
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Updated Successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(),"Profile Updation Failed",Toast.LENGTH_SHORT).show()

            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun setUserData() {
        val userId = auth.currentUser?.uid
        if(userId!=null) {
         val userReference = database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        if (!isAdded) return
                        val userProfile = snapshot.getValue(User::class.java)
                        if(userProfile!=null){
                          binding.name.setText(userProfile.userName)
                            binding.address.setText(userProfile.Address)
                            binding.phone.setText(userProfile.Phone)
                            binding.email.setText(userProfile.eMail)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

}

