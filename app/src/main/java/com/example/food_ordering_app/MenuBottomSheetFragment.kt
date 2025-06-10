package com.example.food_ordering_app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_ordering_app.Adapter.MenuAdapter
import com.example.food_ordering_app.databinding.FragmentMenuBottomSheetBinding
import com.example.food_ordering_app.ModelClass.MenuItemModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {

    private  lateinit var binding : FragmentMenuBottomSheetBinding
    private lateinit var myRecyclerView: RecyclerView
    private var menuArrayList: ArrayList<MenuItemModel> = arrayListOf()
    private  lateinit var database : FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentMenuBottomSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.backButton.setOnClickListener{
            dismiss()
        }
        RetriveMenuItem()

    }

    private fun RetriveMenuItem() {
      database = FirebaseDatabase.getInstance()
        val foodReference : DatabaseReference = database.reference.child("menu")

        //Fetch data from database

        foodReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val foodItem = foodSnapshot.getValue(MenuItemModel::class.java)
                    if (foodItem != null) {
                        menuArrayList.add(foodItem)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Firebase Error", error.message)
            }

        })

    }

    private fun setAdapter() {
        myRecyclerView=binding.menuRecyclerView
        myRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        myRecyclerView.adapter = MenuAdapter(requireContext(),menuArrayList)
    }

}
