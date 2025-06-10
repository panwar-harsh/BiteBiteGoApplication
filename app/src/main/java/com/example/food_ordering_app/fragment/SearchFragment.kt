package com.example.food_ordering_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_ordering_app.Adapter.MenuAdapter
import com.example.food_ordering_app.ModelClass.MenuItemModel
import com.example.food_ordering_app.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.ArrayList

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private var originalMenuArrayList: ArrayList<MenuItemModel> = ArrayList()
    private lateinit var database: FirebaseDatabase
    private lateinit var myRecyclerView: RecyclerView
    private lateinit var adapter: MenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //Retrieve Menu items from database
        RetrieveMenuItem()

        // Set up the SearchView
        setUpSearchView()

        return binding.root
    }

    private fun RetrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        //Reference to the menu node
        val menuReference = database.reference.child("menu")
        menuReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return
                originalMenuArrayList.clear()
                for(foodSnapshot in snapshot.children){
                    val menuItem = foodSnapshot.getValue(MenuItemModel::class.java)
                    menuItem?.let {
                        originalMenuArrayList.add(it)
                    }
                }
                ShowMenu()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load menu: ${error.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun ShowMenu() {
        val filteredMenuItem = ArrayList(originalMenuArrayList)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItemModel>) {
        if (!isAdded || context == null) return
        adapter = MenuAdapter(requireContext(), filteredMenuItem)
        binding.menuRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }


    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter the list based on the search query
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String?) {

        val filteredMenuItem = originalMenuArrayList.filter {
            it.foodName?.contains(query.toString(),ignoreCase = true) == true
        }
        setAdapter(filteredMenuItem)
    }
}


